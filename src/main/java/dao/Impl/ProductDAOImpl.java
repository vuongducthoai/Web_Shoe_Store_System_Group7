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

            for (Object[] result : results) {
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




    public boolean AddProduct(Product product) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

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

    public List<ProductDTO> getListProductDTO() {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<ProductDTO> productDTOList = new ArrayList<>();
        try {
            String sql = "SELECT p.productId, p.productName, p.color, p.description, p.image, p.price, p.size, c.categoryID, c.categoryName " +
                    "     FROM Product p INNER JOIN Category c ON p.categoryID= c.categoryID " +
                    "      WHERE p.status = 1 order by p.productID";


            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> results = query.getResultList();


            for (Object[] result : results) {
                ProductDTO productDTO = new ProductDTO();
                int productID = (int) result[0];
                String productName = (String) result[1];
                String Color = (String) result[2];
                String productDescription = (String) result[3];
                byte[] image = (byte[]) result[4];
                // byte[] image = convertBase64ToByteArray(imageString);
                double price = (double) result[5];
                int size = (int) result[6];
                int categoryID = result[7] != null ? (int) result[7] : 0;
                String categoryName = result[8] != null ? (String) result[8] : "";
                if (categoryID != 0) {
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTO.setCategoryId(categoryID);
                    categoryDTO.setCategoryName(categoryName);
                    productDTO.setCategoryDTO(categoryDTO);
                }
                productDTO.setProductId(productID);
                productDTO.setProductName(productName);
                productDTO.setPrice(price);
                productDTO.setDescription(productDescription);
                productDTO.setImage(image);
                productDTO.setColor(Color);
                productDTO.setSize(size);
                productDTOList.add(productDTO);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return productDTOList;
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

    public List<ProductDTO> getListProductByName(String name) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<ProductDTO> productDTOList = new ArrayList<>();
        try {
            String sql = "SELECT p.productId, p.productName, p.color, p.description, p.image, p.price, p.size, c.categoryID, c.categoryName " +
                    "FROM Product p " +
                    "INNER JOIN Category c ON p.categoryID = c.categoryID " +
                    "WHERE p.productName = ? and p.status=1"; // Sử dụng tham số cho productName

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, name);
            List<Object[]> results = query.getResultList();


            for (Object[] result : results) {
                ProductDTO productDTO = new ProductDTO();
                int productID = (int) result[0];
                String productName = (String) result[1];
                String Color = (String) result[2];
                String productDescription = (String) result[3];
                byte[] image = (byte[]) result[4];
                // byte[] image = convertBase64ToByteArray(imageString);
                double price = (double) result[5];
                int size = (int) result[6];
                int categoryID = result[7] != null ? (int) result[7] : 0;
                String categoryName = result[8] != null ? (String) result[8] : "";
                if (categoryID != 0) {
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTO.setCategoryId(categoryID);
                    categoryDTO.setCategoryName(categoryName);
                    productDTO.setCategoryDTO(categoryDTO);
                }
                productDTO.setProductId(productID);
                productDTO.setProductName(productName);
                productDTO.setPrice(price);
                productDTO.setDescription(productDescription);
                productDTO.setImage(image);
                productDTO.setColor(Color);
                productDTO.setSize(size);
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
    public List<ProductDTO> findByName(String name) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<ProductDTO> productDTOList = new ArrayList<>();
        try {
            String sql = "SELECT p.productId, p.productName, p.color, p.description, p.image, p.price, p.size, c.categoryID, c.categoryName " +
                    "FROM Product p " +
                    "INNER JOIN Category c ON p.categoryID = c.categoryID " +
                    "WHERE p.productName = ?"; // Sử dụng tham số cho productName

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, name);
            List<Object[]> results = query.getResultList();


            for (Object[] result : results) {
                ProductDTO productDTO = new ProductDTO();
                int productID = (int) result[0];
                String productName = (String) result[1];
                String Color = (String) result[2];
                String productDescription = (String) result[3];
                byte[] image = (byte[]) result[4];
                // byte[] image = convertBase64ToByteArray(imageString);
                double price = (double) result[5];
                int size = (int) result[6];
                int categoryID = result[7] != null ? (int) result[7] : 0;
                String categoryName = result[8] != null ? (String) result[8] : "";
                if (categoryID != 0) {
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTO.setCategoryId(categoryID);
                    categoryDTO.setCategoryName(categoryName);
                    productDTO.setCategoryDTO(categoryDTO);
                }
                productDTO.setProductId(productID);
                productDTO.setProductName(productName);
                productDTO.setPrice(price);
                productDTO.setDescription(productDescription);
                productDTO.setImage(image);
                productDTO.setColor(Color);
                productDTO.setSize(size);
                productDTOList.add(productDTO);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return productDTOList;
    }


    public List<Product> findRandomProducts(String currentProductName, int categoryID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // JPQL truy vấn các sản phẩm cùng CategoryID nhưng khác tên
            String jpql = "SELECT DISTINCT p FROM Product p WHERE p.category.categoryID = :categoryID " +
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


    public Map<Integer, Integer> getQuantitiesByColor(String color, String productName) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        Map<Integer, Integer> sizeQuantityMap = new HashMap<>();

        try {
            // Native SQL query trực tiếp truy vấn bảng dữ liệu
            String sql = "SELECT p.size, COUNT(*) AS quantity " +
                    "FROM Product p " +
                    "WHERE p.color = ? AND p.productName = ? and p.status=1 " +
                    "GROUP BY p.size";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, color);
            query.setParameter(2, productName);

            // Lấy kết quả và ánh xạ vào Map
            List<Object[]> results = query.getResultList();
            for (Object[] result : results) {
                int size = (int) result[0];
                int quantity = ((Number) result[1]).intValue();
                sizeQuantityMap.put(size, quantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return sizeQuantityMap;
    }

    public void updateProductByCommonInfo(String productName, double productPrice, String categoryName, String productDescription) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Lấy categoryId dựa trên categoryName
            String getCategoryIdSql = "SELECT c.categoryID FROM Category c WHERE c.categoryName = ?";
            Query getCategoryIdQuery = entityManager.createNativeQuery(getCategoryIdSql);
            getCategoryIdQuery.setParameter(1, categoryName);

            Integer categoryId = (Integer) getCategoryIdQuery.getSingleResult();

            // Cập nhật thông tin sản phẩm
            String updateSql = "UPDATE Product p " +
                    "SET p.price = ?, " +
                    "    p.categoryID = ?, " +
                    "    p.description = ? " +
                    "WHERE p.productName = ? ";
            Query updateQuery = entityManager.createNativeQuery(updateSql);
            updateQuery.setParameter(1, productPrice);
            updateQuery.setParameter(2, categoryId);
            updateQuery.setParameter(3, productDescription);
            updateQuery.setParameter(4, productName);

            int rowsUpdated = updateQuery.executeUpdate();

            transaction.commit();

            if (rowsUpdated == 0) {
                System.out.println("Không có sản phẩm nào được cập nhật với tên: " + productName);
            } else {
                System.out.println("Đã cập nhật thành công " + rowsUpdated + " sản phẩm.");
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public List<String> getColorsByProduct(String productName) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<String> colors = new ArrayList<>();
        try {
            // Truy vấn danh sách màu của sản phẩm
            String sql = "SELECT DISTINCT p.color FROM Product p WHERE p.productName = ? and p.status=1";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, productName);

            // Lấy kết quả và ép kiểu thành danh sách chuỗi
            @SuppressWarnings("unchecked")
            List<String> result = query.getResultList();
            colors.addAll(result);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi nếu có
        } finally {
            entityManager.close();
        }
        return colors;
    }

    public void deleteProductByColor(String productName, String color) throws Exception {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Cập nhật trạng thái sản phẩm thành 0 (không hoạt động) dựa trên tên sản phẩm và màu sắc
            String updateStatusSql = "UPDATE Product p SET p.status = 0 WHERE p.productName = ? AND p.color = ?";
            Query updateStatusQuery = entityManager.createNativeQuery(updateStatusSql);
            updateStatusQuery.setParameter(1, productName);
            updateStatusQuery.setParameter(2, color);

            int rowsUpdated = updateStatusQuery.executeUpdate();

            // Kiểm tra nếu không có sản phẩm nào được cập nhật
            if (rowsUpdated == 0) {
                throw new Exception("No product found with the name: " + productName + " and color: " + color);
            }

            // Commit transaction
            transaction.commit();
        } catch (Exception e) {
            // Rollback transaction nếu có lỗi
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public boolean updateImage(String color, String productName, byte[] imageBytes) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = null;

        boolean success = false;  // Biến để theo dõi kết quả

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Cập nhật hình ảnh cho sản phẩm dựa trên tên và màu
            String updateImageSql = "UPDATE Product p SET p.image = ? WHERE p.productName = ? AND p.color = ?";
            Query updateImageQuery = entityManager.createNativeQuery(updateImageSql);
            updateImageQuery.setParameter(1, imageBytes);
            updateImageQuery.setParameter(2, productName);
            updateImageQuery.setParameter(3, color);

            int rowsUpdated = updateImageQuery.executeUpdate();

            // Kiểm tra nếu có ít nhất 1 dòng được cập nhật
            if (rowsUpdated > 0) {
                success = true;  // Đánh dấu thành công
            }

            // Commit giao dịch
            transaction.commit();
        } catch (Exception e) {
            // Rollback giao dịch nếu có lỗi
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();  // Ghi lại thông báo lỗi nếu cần
        } finally {
            entityManager.close();
        }

        return success;  // Trả về true nếu thành công, false nếu có lỗi
    }

    public List<Integer> getSizesByColor(String color, String productName) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();

        List<Integer> sizes = new ArrayList<>();  // Danh sách chứa các kích thước (size)

        try {
            // Truy vấn SQL với DISTINCT để lấy danh sách kích thước duy nhất của sản phẩm theo màu sắc và tên sản phẩm
            String sql = "SELECT DISTINCT p.size FROM Product p WHERE p.color = ? AND p.productName = ? AND p.status = 1";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, color);
            query.setParameter(2, productName);

            // Lấy kết quả trả về và chuyển thành danh sách kích thước
            List<Object> result = query.getResultList();
            for (Object obj : result) {
                sizes.add((Integer) obj);  // Chuyển đối tượng thành kiểu Integer và thêm vào danh sách
            }
        } catch (Exception e) {
            e.printStackTrace();  // In lỗi nếu có
        } finally {
            entityManager.close();
        }

        return sizes;  // Trả về danh sách kích thước
    }

    public void deleteSize(String productName, String color, int size) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = null;

        try {
            // Bắt đầu giao dịch
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Cập nhật trạng thái sản phẩm thành 0 (không hoạt động) dựa trên tên sản phẩm, màu sắc và kích thước
            String updateStatusSql = "UPDATE Product p SET p.status = 0 WHERE p.productName = ? AND p.color = ? AND p.size = ?";
            Query updateStatusQuery = entityManager.createNativeQuery(updateStatusSql);
            updateStatusQuery.setParameter(1, productName);
            updateStatusQuery.setParameter(2, color);
            updateStatusQuery.setParameter(3, size);

            int rowsUpdated = updateStatusQuery.executeUpdate();

            // Kiểm tra nếu không có sản phẩm nào được cập nhật
            if (rowsUpdated == 0) {
                System.out.println("No product found with the name: " + productName + ", color: " + color + " and size: " + size);
            }

            // Commit giao dịch
            transaction.commit();
        } catch (Exception e) {
            // Nếu có lỗi, rollback giao dịch
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // In ra lỗi
        } finally {
            entityManager.close(); // Đóng kết nối EntityManager
        }
    }


    public void reduceProductInstances(String productName, String color, int size, int quantityDifference) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = null;

        try {
            // Bắt đầu giao dịch
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Truy vấn để lấy ID của các sản phẩm theo tên, màu sắc và kích thước
            String getProductIdsSql = "SELECT p.productId FROM Product p WHERE p.productName = ? AND p.color = ? AND p.size = ? AND p.status = 1 LIMIT ?";
            Query getProductIdsQuery = entityManager.createNativeQuery(getProductIdsSql);
            getProductIdsQuery.setParameter(1, productName);
            getProductIdsQuery.setParameter(2, color);
            getProductIdsQuery.setParameter(3, size);
            getProductIdsQuery.setParameter(4, quantityDifference); // Lấy số lượng sản phẩm cần cập nhật trạng thái

            // Lấy danh sách ID sản phẩm
            List<Integer> productIds = getProductIdsQuery.getResultList();



            // Cập nhật trạng thái của sản phẩm đã giảm
            String updateStatusSql = "UPDATE Product p SET p.status = 0 WHERE p.productId = ?";
            Query updateStatusQuery = entityManager.createNativeQuery(updateStatusSql);

            // Dùng vòng lặp để cập nhật từng sản phẩm
            for (Integer productId : productIds) {
                updateStatusQuery.setParameter(1, productId);
                updateStatusQuery.executeUpdate();
            }

            // Commit giao dịch
            transaction.commit();
        } catch (Exception e) {
            // Nếu có lỗi, rollback giao dịch
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }



    public ProductDTO getCommonInfoByName(String productName) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        ProductDTO productDTO = null;

        try {
            // Cập nhật câu lệnh SQL để bao gồm các điều kiện color và status = 1
            String sql = "SELECT p.productName, p.description, p.image, p.price, c.categoryID, c.categoryName " +
                    "FROM Product p " +
                    "INNER JOIN Category c ON p.categoryID = c.categoryID " +
                    "WHERE p.productName = ? AND p.status = 1";

            // Tạo query và thiết lập tham số
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, productName);


            List<Object[]> results = query.getResultList();

            if (!results.isEmpty()) {
                Object[] result = results.get(0);
                productDTO = new ProductDTO();
                productDTO.setProductName((String) result[0]);
                productDTO.setDescription((String) result[1]);
                productDTO.setImage((byte[]) result[2]);
                productDTO.setPrice((double) result[3]);

                // Tạo đối tượng CategoryDTO
                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setCategoryId((Integer) result[4]);
                categoryDTO.setCategoryName((String) result[5]);
                productDTO.setCategoryDTO(categoryDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return productDTO;
    }




}

