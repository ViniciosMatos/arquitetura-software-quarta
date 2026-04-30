import domain.Preco;
import domain.Produto;
import infra.HibernateUtil;
import service.PrecoService;
import service.ProdutoService;
import service.ServiceInterface;

void main() {
    ProdutoService produtoService = new ProdutoService();
    PrecoService precoService = new PrecoService();

    try {
        boolean menuAtivo = true;
        while (menuAtivo) {
            int opcaoSelecionada = menu();
            switch (opcaoSelecionada) {
                case 1:
                    adicionarProduto(produtoService, precoService);
                    break;
                case 2:
                    listarProdutos(produtoService);
                    break;
                case 3:
                    editarProdutos(produtoService, precoService);
                    break;
                case 4:
                    deletarProdutos(produtoService);
                    break;
                case 5:
                    editarPreco(produtoService, precoService);
                    break;
                case 6:
                    listarPrecos(precoService);
                    break;
                case 0:
                    menuAtivo = false;
                    break;
            }
        }
    } finally {
        HibernateUtil.shutdown();
    }
}

public void adicionarProduto(ServiceInterface produtoService, ServiceInterface precoService) {
    String sku = IO.readln("Digite a SKU do produto: ");
    String nome = IO.readln("Digite o nome do produto: ");
    String marca = IO.readln("Digite a marca do produto: ");
    String descricao = IO.readln("Digite a descricao do produto: ");
    Float preco = Float.parseFloat(IO.readln("Digite o preco do produto: "));

    Produto produto = new Produto(sku, nome, marca, descricao, preco);
    produtoService.add(produto);

    Date dataAtual = new Date();
    Preco novoPreco = new Preco(dataAtual, preco, produto);
    precoService.add(novoPreco);
}

public void listarProdutos(ServiceInterface service) {
    service.list();
}

public void editarProdutos(ServiceInterface produtoService, ServiceInterface precoService) {
    System.out.println("Atualmente temos os seguintes produtos cadastrados: ");
    produtoService.list();
    int indice = Integer.parseInt(IO.readln("Digite o indice do produto que deseja editar"));

    Produto produto = (Produto) produtoService.findByIndex(indice);
    produto.setSku(IO.readln("Informe o novo SKU do produto: "));
    produto.setNome(IO.readln("Informe o novo nome do produto: "));
    produto.setDescricao(IO.readln("Informe a nova descricao do produto: "));
    produto.setMarca(IO.readln("Informe a nova marca do produto: "));
    produto.setPreco(Float.parseFloat(IO.readln("Informe o novo preco do produto: ")));


    produtoService.edit(produto, produto.getId());

    Date dataAtual = new Date();
    Preco novoPreco = new Preco(dataAtual, produto.getPreco(), produto);
    precoService.add(novoPreco);
}

public void deletarProdutos(ServiceInterface service) {
    System.out.println("Atualmente temos os seguintes produtos cadastrados: ");
    service.list();
    int indice = Integer.parseInt(IO.readln("Digite o indice do produto que deseja deletar: "));
    Produto produto = (Produto) service.findByIndex(indice);
    service.remove(produto);
}

public void editarPreco(ServiceInterface produtoService, ServiceInterface precoService) {
    produtoService.list();
    int indice = Integer.parseInt(IO.readln("Digite o indice do produto que deseja alterar"));

    Produto produto = (Produto) produtoService.findByIndex(indice);
    produto.setPreco(Float.parseFloat(IO.readln("Digite o novo preço: ")));
    produtoService.edit(produto, produto.getId());

    Date dataAtual = new Date();
    Preco novoPreco = new Preco(dataAtual, produto.getPreco(), produto);
    precoService.add(novoPreco);
}

public void listarPrecos(ServiceInterface precoService){
    precoService.list();
}


public Integer menu() {
    System.out.println("Digite a opção desejada: ");
    System.out.println("1 = Adicionar um novo produto");
    System.out.println("2 = Listar os produtos");
    System.out.println("3 = Editar um produto");
    System.out.println("4 = Deletar um produto");
    IO.println("5 = Editar um preço");
    IO.println("6 = Listar o Histórico de Preços");
    System.out.println("0 = Sair");

    int opcao = Integer.parseInt(IO.readln());
    return opcao;
}
