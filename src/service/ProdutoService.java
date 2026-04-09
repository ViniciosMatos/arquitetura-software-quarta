package service;

import domain.EntityInterface;
import domain.Produto;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class ProdutoService implements ServiceInterface {
    ArrayList<Produto> produtos = new ArrayList<>();

    @Override
    public void add(EntityInterface entity) {
        IO.println("Salvando o produto");
        produtos.add((Produto) entity);
    }

    @Override
    public void remove(EntityInterface entity) {
        IO.println("Excluindo o produto");
        produtos.remove(entity);
    }

    @Override
    public void list() {
        for (int i = 0; i < produtos.size(); i++) {
            System.out.printf("\nIndice: %s\n", i);
            System.out.printf("Id: %s\n", produtos.get(i).getId());
            System.out.printf("SKU: %s\n", produtos.get(i).getSku());
            System.out.printf("Nome: %s\n", produtos.get(i).getNome());
            System.out.printf("Descricao: %s\n", produtos.get(i).getDescricao());
            System.out.printf("Marca: %s\n", produtos.get(i).getMarca());
            System.out.printf("preço: %s\n", produtos.get(i).getPreco());
            System.out.println("---------------------------------\n");
        }
    }

    @Override
    public EntityInterface findByIndex(int index) {
        return produtos.get(index);
    }

    @Override
    public void edit(EntityInterface entity, UUID id) {
        IO.println("editando o produto");
        for (int i = 0; i < this.produtos.size(); i++) {
            if(this.produtos.get(i).getId().equals(id)) {
                this.produtos.set(i, ((Produto) entity));
            }
        }
    }
}
