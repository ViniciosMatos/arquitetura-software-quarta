import domain.Produto;
import service.ProdutoService;

void main() {
    ProdutoService service = new ProdutoService();

    Produto produto = new Produto(
            "2136627323",
            "Play 5",
            "Sony",
            "VideoGame",
            4000f);

    service.add(produto);


}
