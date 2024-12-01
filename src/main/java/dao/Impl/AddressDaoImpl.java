package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IAddressDAO;
import dto.AddressDTO;
import entity.Address;
import entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddressDaoImpl implements IAddressDAO {
    @Override
    public AddressDTO getAddressByID(int userID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // Truy vấn addressID từ Customer dựa trên userID
            Integer addressID = entityManager.createQuery(
                            "SELECT c.address.addressID FROM Customer c WHERE c.userID = :userID", Integer.class)
                    .setParameter("userID", userID)
                    .getSingleResult();

            if (addressID == null) {
                System.out.println("No addressID found for userID: " + userID);
                return null;
            }

            // Truy vấn Address dựa trên addressID
            Address addressEntity = entityManager.find(Address.class, addressID);

            if (addressEntity != null) {
                // Chuyển đổi Address entity thành AddressDTO
                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setAddressID(addressEntity.getAddressID());
                addressDTO.setProvince(addressEntity.getProvince());
                addressDTO.setCity(addressEntity.getCity());
                addressDTO.setDistrict(addressEntity.getDistrict());
                addressDTO.setStreetName(addressEntity.getStreetName());
                addressDTO.setHouseNumber(addressEntity.getHouseNumber());
                return addressDTO;
            } else {
                System.out.println("No Address found for addressID: " + addressID);
                return null;
            }
        } catch (jakarta.persistence.NoResultException e) {
            // Không tìm thấy kết quả trong truy vấn
            System.out.println("No Customer found with userID: " + userID);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }
    @Override
    public boolean updateAddress(Address addressEntity) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            // Bắt đầu giao dịch
            transaction.begin();

            // Tìm Address trong cơ sở dữ liệu dựa trên AddressID
            Address existingAddress = entityManager.find(Address.class, addressEntity.getAddressID());

            if (existingAddress != null) {
                // Cập nhật các trường trong Address
                existingAddress.setHouseNumber(addressEntity.getHouseNumber());
                existingAddress.setStreetName(addressEntity.getStreetName());
                existingAddress.setDistrict(addressEntity.getDistrict());
                existingAddress.setCity(addressEntity.getCity());

                // Lưu lại vào cơ sở dữ liệu
                entityManager.merge(existingAddress);
                transaction.commit();
                return true; // Cập nhật thành công
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback(); // Rollback nếu có lỗi
            }
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close(); // Đảm bảo đóng EntityManager
            }
        }
        return false; // Cập nhật thất bại
    }
}
