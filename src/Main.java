import domain.Produto;
import service.ProdutoService;

void main() {
    ProdutoService service = new ProdutoService();

    UUID id = new UUID(1, 1);
    Produto produto = new Produto(
            "2136627323",
            "Play 5",
            "Sony",
            "VideoGame",
            4000f);

    produto.setId(id);
    //adicionei o produto a lista
    service.add(produto);
    //listei os produtos
    service.list();

    produto.setNome("Playstation 5");
    service.edit(produto, id);
    service.list();

    service.remove(produto);
    service.list();
}



