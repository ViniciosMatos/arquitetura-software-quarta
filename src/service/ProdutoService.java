package service;

import domain.EntityInterface;
import domain.Produto;
import infra.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.UUID;

public class ProdutoService implements ServiceInterface {

    @Override
    public void add(EntityInterface entity) {
        IO.println("Salvando o produto");
        Produto produto = (Produto) entity;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(produto);
            tx.commit();
        }
    }

    @Override
    public void remove(EntityInterface entity) {
        IO.println("Excluindo o produto");
        Produto produto = (Produto) entity;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Produto managed = session.get(Produto.class, produto.getId());
            if (managed != null) {
                session.remove(managed);
            }
            tx.commit();
        }
    }

    @Override
    public void list() {
        List<Produto> produtos = listar();
        for (int i = 0; i < produtos.size(); i++) {
            Produto p = produtos.get(i);
            System.out.printf("\nIndice: %s\n", i);
            System.out.printf("Id: %s\n", p.getId());
            System.out.printf("SKU: %s\n", p.getSku());
            System.out.printf("Nome: %s\n", p.getNome());
            System.out.printf("Descricao: %s\n", p.getDescricao());
            System.out.printf("Marca: %s\n", p.getMarca());
            System.out.printf("preço: %s\n", p.getPreco());
            System.out.println("---------------------------------\n");
        }
    }

    @Override
    public EntityInterface findByIndex(int index) {
        List<Produto> produtos = listar();
        return produtos.get(index);
    }

    @Override
    public void edit(EntityInterface entity, UUID id) {
        IO.println("editando o produto");
        Produto atualizado = (Produto) entity;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Produto managed = session.get(Produto.class, id);
            if (managed != null) {
                managed.setSku(atualizado.getSku());
                managed.setNome(atualizado.getNome());
                managed.setMarca(atualizado.getMarca());
                managed.setDescricao(atualizado.getDescricao());
                managed.setPreco(atualizado.getPreco());
            }
            tx.commit();
        }
    }

    private List<Produto> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Produto order by nome", Produto.class)
                    .getResultList();
        }
    }
}
