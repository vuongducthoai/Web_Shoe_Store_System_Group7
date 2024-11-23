package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IProductDAO;
import dto.AddressDTO;
import dto.CategoryDTO;
import dto.ProductDTO;
import entity.Cart;
import entity.CartItem;
import entity.Customer;
import dto.ProductDTO;
import entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
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

            //Tang gioi hạn để láy thêm dữ liệu (gấp 3 limit)
            int fetchSize = limit * 2;

            // Phân trang
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(fetchSize);

            // Thực thi truy vấn
            List<Product> products = typedQuery.getResultList();

            // Loại bỏ các sản phẩm trùng tên (giữ lại 1 sản phẩm cho mỗi tên)
            Map<String, Product> uniqueProductsMap = new LinkedHashMap<>();
            for (Product product : products) {
                uniqueProductsMap.putIfAbsent(product.getProductName(), product);
            }

            List<Product> uniqueProducts = new ArrayList<>(uniqueProductsMap.values());
            return uniqueProducts.size() > limit ? uniqueProducts.subList(0, limit) // lay dung so luong
                                : uniqueProducts;
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
            String query = "SELECT COUNT(p) FROM Product p WHERE p.productName = :productName AND p.status = false";
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

