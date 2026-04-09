import domain.Produto;
import service.ProdutoService;
import service.ServiceInterface;

void main() {
    ProdutoService service = new ProdutoService();

//    produto.setId(id);
//    //adicionei o produto a lista
//    service.add(produto);
//    //listei os produtos
//    service.list();
//
//    produto.setNome("Playstation 5");
//    service.edit(produto, id);
//    service.list();

    boolean menuAtivo = true;
    while (menuAtivo) {
        int opcaoSelecionada = menu();
        switch (opcaoSelecionada) {
            case 1:
                adicionarProduto(service);
                break;
            case 2:
                listarProdutos(service);
                break;
            case 3:
                editarProdutos(service);
                break;
            case 4:
                deletarProdutos(service);
                break;
            case 0:
                menuAtivo = false;
                break;
        }
    }


}

public void adicionarProduto(ServiceInterface service) {
    UUID id = new UUID(1, 1);

    String sku = IO.readln("Digite a SKU do produto: ");
    String nome = IO.readln("Digite o nome do produto: ");
    String marca = IO.readln("Digite a marca do produto: ");
    String descricao = IO.readln("Digite a descricao do produto: ");
    Float preco = Float.parseFloat(IO.readln("Digite o preco do produto: "));

    Produto produto = new Produto(
            sku,
            nome,
            marca,
            descricao,
            preco);
    produto.setId(id);

    service.add(produto);
}

public void listarProdutos(ServiceInterface service) {
    service.list();
}

public void editarProdutos(ServiceInterface service) {
    System.out.println("Atualmente temos os seguintes produtos cadastrados: ");
    service.list();
    int indice = Integer.parseInt(IO.readln("Digite o indice do produto que deseja editar"));

    Produto produto = (Produto) service.findByIndex(indice);
    produto.setSku(IO.readln("Informe o novo SKU do produto: "));
    produto.setNome(IO.readln("Informe o novo nome do produto: "));
    produto.setDescricao(IO.readln("Informe a nova descricao do produto: "));
    produto.setMarca(IO.readln("Informe a nova marca do produto: "));
    produto.setPreco(Float.parseFloat(IO.readln("Informe o novo preco do produto: ")));


    service.edit(produto, produto.getId());
}

public void deletarProdutos(ServiceInterface service) {

}


public Integer menu() {
    System.out.println("Digite a opção desejada: ");
    System.out.println("1 = Adicionar um novo produto");
    System.out.println("2 = Listar os produtos");
    System.out.println("3 = Editar um produto");
    System.out.println("4 = Deletar um produto");
    System.out.println("0 = Sair");

    int opcao = Integer.parseInt(IO.readln());
    return opcao;
}



