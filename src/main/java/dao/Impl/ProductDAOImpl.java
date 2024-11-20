package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IProductDAO;
import entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ProductDAOImpl implements IProductDAO {


    @Override
    public List<Product> findAllWithPagination(int offset, int limit) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String query = "SELECT p FROM Product p";
            TypedQuery<Product> typedQuery  = entityManager.createQuery(query, Product.class);

            //Phan trang(pagination)
            typedQuery.setFirstResult(offset); // Xac dinh vi tri bat dau
            typedQuery.setMaxResults(limit); // so luong san pham can lay

            //Thuc tri truy van va tra ve ket qua
            return typedQuery.getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            entityManager.close();
        }
        return null;
    }
}
