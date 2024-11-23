package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IProductDAO;
import dto.AddressDTO;
import dto.CategoryDTO;
import dto.ProductDTO;
import entity.Cart;
import entity.CartItem;
import entity.Customer;
import entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductDAOImpl implements IProductDAO {

    @Override
    public List<Product> findAllWithPagination(int offset, int limit) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // JPQL để lấy tất cả các sản phẩm
            String query = "SELECT p FROM Product p";
            TypedQuery<Product> typedQuery  = entityManager.createQuery(query, Product.class);

            // Phân trang
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(limit);

            // Thực thi truy vấn
            List<Product> products = typedQuery.getResultList();

            // Loại bỏ các sản phẩm trùng tên (giữ lại 1 sản phẩm cho mỗi tên)
            Map<String, Product> uniqueProductsMap = new LinkedHashMap<>();
            for (Product product : products) {
                uniqueProductsMap.putIfAbsent(product.getProductName(), product);
            }

            // Trả về danh sách các sản phẩm không trùng tên
            return new ArrayList<>(uniqueProductsMap.values());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            entityManager.close();
        }
        return null;
    }

    @Override
    public int countProductName(String name) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String query = "SELECT COUNT(p) FROM Product p WHERE p.productName = :productName";
            TypedQuery<Long> typedQuery = entityManager.createQuery(query, Long.class);
            typedQuery.setParameter("productName", name);
            Long result = typedQuery.getSingleResult();
            return result.intValue();
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }


    public boolean AddProduct(Product product) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(product);
            transaction.commit();
            return true;
        } catch (Exception e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return false;
    }

    public boolean UpdateProduct(Product product) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(product);
            transaction.commit();
            return true;
        } catch (Exception e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return false;
    }

    public List<ProductDTO> getListProductDTO(){
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String hql = "SELECT p FROM Product p";
            TypedQuery<Product> query = entityManager.createQuery(hql, Product.class);
            List<Product> products = query.getResultList();

            // Chuyển đổi từ Product sang ProductDTO
            List<ProductDTO> productDTOs = new ArrayList<>();
            for (Product product : products) {
                // Kiểm tra nếu category không null
                CategoryDTO categoryDTO = null;
                if (product.getCategory() != null) {
                    categoryDTO = new CategoryDTO(
                            product.getCategory().getCategoryID(), // ID của Category
                            product.getCategory().getCategoryName() // Tên của Category

                    );
                }

                System.out.println(categoryDTO.getCategoryName());

                // Tạo đối tượng ProductDTO
                ProductDTO dto = new ProductDTO(
                        product.getProductID(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getImage(),
                        product.getColor(),
                        product.getSize(),
                        categoryDTO, // Truyền đối tượng CategoryDTO vào đây
                        product.getDescription(),
                        product.isStatus()
                );

                productDTOs.add(dto); // Thêm vào danh sách
            }
            return productDTOs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }
}

