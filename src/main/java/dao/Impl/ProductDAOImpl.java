package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IProductDAO;
import dto.ProductDTO;
import entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.*;
import java.util.stream.Collectors;

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


    @Override
    public List<ProductDTO> findByName(String name) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // Truy vấn các sản phẩm có tên chứa từ khóa
            List<Product> products = entityManager.createQuery(
                            "SELECT p FROM Product p WHERE p.productName = :name", Product.class)
                    .setParameter("name", name) // Tìm kiếm gần đúng
                    .getResultList();

            // Chuyển đổi từ Product (Entity) sang ProductDTO
            return products.stream()
                    .map(product -> new ProductDTO(
                            product.getProductID(),
                            product.getProductName(),
                            product.getPrice(),
                            product.getImage(),
                            product.getColor(),
                            product.getSize(),
                            product.isStatus(),
                            product.getDescription(),
                            null, // cartItemDTOList
                            null, // orderItemDTOList
                            null, // categoryDTO
                            null  // promotionDTO
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }



    public List<Product> findRandomProducts(int offset, int limit, String CurrentProductName) {
        // Lấy danh sách sản phẩm phân trang và không trùng tên
        List<Product> products = findAllWithPagination(offset, limit);

        if (products == null || products.isEmpty()) {
            return Collections.emptyList();
        }

        List<Product> filteredProducts = products.stream()
                .filter(product -> !CurrentProductName.equalsIgnoreCase(product.getProductName()))
                .collect(Collectors.toList());

        if (filteredProducts.size() <= 4) {
            // Nếu có ít hơn hoặc bằng 4 sản phẩm sau khi lọc, trả về danh sách đó
            return filteredProducts;
        }

        // Lấy 4 sản phẩm ngẫu nhiên
        Collections.shuffle(filteredProducts);
        return filteredProducts.subList(0, 4);
    }

}
