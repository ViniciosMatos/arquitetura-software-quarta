package service;

import domain.Preco;
import domain.Produto;
import domain.port.CrawlerPort;

import java.util.Date;

/**
 * Serviço que orquestra o monitoramento de preços via crawler.
 * Usa um CrawlerPort (porta hexagonal) para buscar preços na web,
 * e os serviços de Produto e Preco para persistir os dados.
 */
public class CrawlerService {

    private final CrawlerPort crawlerPort;
    private final ProdutoService produtoService;
    private final PrecoService precoService;

    public CrawlerService(CrawlerPort crawlerPort, ProdutoService produtoService,
                          PrecoService precoService) {
        this.crawlerPort = crawlerPort;
        this.produtoService = produtoService;
        this.precoService = precoService;
    }

    /**
     * Monitora o preço de um produto a partir de uma URL.
     *
     * - Se o produto não existir (por SKU), cria um novo
     * - Se o preço mudou, salva o preço antigo no histórico e atualiza o produto
     * - Se o preço não mudou, não faz nada
     */
    public void monitorar(String url, String nomeProduto, String sku, String marca) {
        System.out.println("\n========================================");
        System.out.println("[Crawler] Iniciando monitoramento...");
        System.out.println("[Crawler] Produto: " + nomeProduto);
        System.out.println("[Crawler] URL: " + url);
        System.out.println("========================================\n");

        // 1. Buscar preço na web via CrawlerPort
        Float precoAtual = crawlerPort.buscarPreco(url);
        if (precoAtual == null) {
            System.out.println("[Crawler] Não foi possível obter o preço. Abortando.");
            return;
        }
        System.out.printf("[Crawler] Preço encontrado: R$ %.2f%n", precoAtual);

        // 2. Buscar ou criar o produto (garante uma única instância por SKU)
        Produto produto = produtoService.findBySku(sku);

        if (produto == null) {
            // Produto não existe — criar novo
            System.out.println("[Crawler] Produto não encontrado no banco. Criando novo...");
            produto = new Produto(sku, nomeProduto, marca, nomeProduto, precoAtual);
            produtoService.add(produto);

            // Re-buscar para ter o ID gerado pelo banco
            produto = produtoService.findBySku(sku);

            // Salvar preço inicial no histórico
            Preco preco = new Preco(new Date(), precoAtual, produto);
            precoService.add(preco);

            System.out.println("[Crawler] Produto criado com sucesso!");
            System.out.printf("[Crawler] Preço inicial registrado: R$ %.2f%n", precoAtual);
        } else {
            // Produto já existe — comparar preço
            Float precoAntigo = produto.getPreco();

            if (!precoAntigo.equals(precoAtual)) {
                System.out.printf("[Crawler] PREÇO ALTERADO! R$ %.2f -> R$ %.2f%n", precoAntigo, precoAtual);

                // Salvar o preço ANTIGO no histórico
                Preco historicoPrecoAntigo = new Preco(new Date(), precoAntigo, produto);
                precoService.add(historicoPrecoAntigo);

                // Atualizar o preço do produto para o novo
                produto.setPreco(precoAtual);
                produtoService.edit(produto, produto.getId());

                System.out.println("[Crawler] Preço antigo salvo no histórico. Produto atualizado.");
            } else {
                System.out.printf("[Crawler] Preço inalterado: R$ %.2f. Nenhuma alteração necessária.%n", precoAtual);
            }
        }

        System.out.println("\n[Crawler] Monitoramento concluído!");
        System.out.println("========================================\n");
    }
}
