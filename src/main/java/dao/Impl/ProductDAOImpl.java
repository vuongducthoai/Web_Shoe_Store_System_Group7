package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IProductDAO;
import dto.CategoryDTO;
import dto.ProductDTO;
import entity.*;
import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;

import static util.ConvertImageStringToByteArray.convertBase64ToByteArray;


public class ProductDAOImpl implements IProductDAO {

    @Override
    public List<ProductDTO> findAllWithPagination(int offset, int limit) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<ProductDTO> productDTOList = new ArrayList<>();
        try {
           //Native SQL Query
            String sql = "SELECT p.productName, MAX(p.price), SUBSTRING_INDEX(GROUP_CONCAT(TO_BASE64(p.image)), ',', 1), " +
                    "MIN(p.description), COUNT(p.productName) " +
                    "FROM Product p " +
                    "WHERE p.status = 1 " +
                    "GROUP BY p.productName";

            Query query = entityManager.createNativeQuery(sql);


            // Phân trang
            query.setFirstResult(offset);
            query.setMaxResults(limit);

            // Thực thi truy vấn
            List<Object[]> results = query.getResultList();

            for(Object[] result : results) {
                ProductDTO productDTO = new ProductDTO();
                String productName = (String) result[0];
                System.out.println(productName);
                double price = (double) result[1];
                String imageString = (String) result[2];
                String description = (String) result[3];
                int quantity = ((Long) result[4]).intValue();

                byte[] image = convertBase64ToByteArray(imageString);

                productDTO.setProductName(productName);
                productDTO.setPrice(price);
                productDTO.setDescription(description);
                productDTO.setImage(image);
                productDTO.setQuantity(quantity);
                productDTOList.add(productDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return productDTOList;
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

            // Kiểm tra và lấy category từ DB
            if (product.getCategory() != null) {
                Category managedCategory = entityManager.find(Category.class, product.getCategory().getCategoryID());
                product.setCategory(managedCategory); // Gán lại category đã được quản lý
            }

            entityManager.merge(product); // Hoặc entityManager.merge(product);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
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

    public ProductDTO getProductByID(int id) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        ProductDTO productDTO = null;

        try {
            // Truy vấn sản phẩm theo ID
            Product product = entityManager.createQuery(
                            "SELECT p FROM Product p WHERE p.id = :id", Product.class)
                    .setParameter("id", id)
                    .getSingleResult();  // Sử dụng getSingleResult để chỉ lấy một kết quả duy nhất

            // Chuyển đổi từ Product sang ProductDTO
            productDTO = new ProductDTO();
            productDTO.setProductId(product.getProductID());
            productDTO.setProductName(product.getProductName());
            productDTO.setPrice(product.getPrice());
            productDTO.setDescription(product.getDescription());
            productDTO.setImage(product.getImage());
        } catch (Exception e) {
            // Trường hợp không tìm thấy sản phẩm với id
            e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }

        return productDTO;
    }

    public List<ProductDTO> findListProductByCategoryID(int id) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // Truy vấn các sản phẩm có tên chứa từ khóa
            List<Product> products = entityManager.createQuery(
                            "SELECT p FROM Product p WHERE p.category.categoryID = :id", Product.class)
                    .setParameter("id", id) // Tìm kiếm gần đúng
                    .getResultList();

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
                            new CategoryDTO(product.getCategory().getCategoryID(), null, null), // categoryDTO
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




    @Override
    public List<ProductDTO> findByName(String name) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // Truy vấn các sản phẩm có tên chứa từ khóa
            List<Product> products = entityManager.createQuery(
                            "SELECT p FROM Product p WHERE p.productName = :name", Product.class)
                    .setParameter("name", name) // Tìm kiếm gần đúng
                    .getResultList();


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
                            new CategoryDTO(product.getCategory().getCategoryID(), null, null), // categoryDTO
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


    public List<Product> findRandomProducts(String currentProductName, int categoryID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // JPQL truy vấn các sản phẩm cùng CategoryID nhưng khác tên
            String jpql = "SELECT p FROM Product p WHERE p.category.categoryID = :categoryID " +
                    "AND p.productName <> :currentProductName";

            TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
            query.setParameter("categoryID", categoryID);
            query.setParameter("currentProductName", currentProductName);

            // Lấy danh sách kết quả
            List<Product> products = query.getResultList();

            if (products.isEmpty()) {
                System.out.println("No products found for category ID: " + categoryID);
                return Collections.emptyList();
            }

            // Trộn danh sách để lấy ngẫu nhiên
            Collections.shuffle(products);

            // Trả về danh sách dù có ít hơn 4 sản phẩm
            return products.size() <= 4 ? products : products.subList(0, 4);
        } catch (Exception e) {
            System.err.println("Error during findRandomProducts:");
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            entityManager.close();
        }
    }
}

