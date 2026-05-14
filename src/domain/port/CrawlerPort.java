package domain.port;

/**
 * Porta (interface) da arquitetura hexagonal para o crawler de preços.
 * Permite trocar a implementação (Jsoup, Selenium, API) sem alterar o domínio.
 */
public interface CrawlerPort {

    /**
     * Busca o preço de um produto a partir de uma URL.
     *
     * @param url URL da página do produto
     * @return o preço encontrado, ou null se não conseguir extrair
     */
    Float buscarPreco(String url);
}
