package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IUserDAO;
import dto.AccountDTO;
import dto.AddressDTO;
import dto.CustomerDTO;
import entity.Account;
import entity.User;
import enums.RoleType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDAOImpl implements IUserDAO {

    @Override
    public User getUserByAccountId(int accountID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        User user = null;
        try {
            String sql = "SELECT u.userID, a.role,u.active FROM User u " +
                    "INNER JOIN Account a ON u.accountID = a.accountID " +
                    "WHERE u.accountID = ?1";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, accountID);

            Object[] result = (Object[]) query.getSingleResult();

            int userId = (int) result[0];
            String role = (String) result[1];
            Boolean active = (Boolean) result[2];
            RoleType role1 = RoleType.valueOf(role);
            user = new User();
            user.setUserID(userId);
            user.setActive(active);
            Account account = new Account();
            account.setRole(role1);
            user.setAccount(account);
        } catch (NoResultException e) {
            System.out.println("Không tìm thấy User với accountID: " + accountID);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return user;
    }

    @Override
    public CustomerDTO getInformationUser(int userID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
            CustomerDTO customerDTO = null;
        try {
            String sql = "SELECT fullName, phone,dateOfBirth,houseNumber, streetName, email,city, district, province, password " +
                    "FROM User u " +
                    "INNER JOIN Account a on u.accountID = a.accountID " +
                    "INNER JOIN Customer c ON c.userID = u.userID " +
                    "INNER JOIN Address ad ON ad.addressID = c.addressID " +
                    "WHERE u.userID = ?1";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, userID);

            Object[] result = (Object[]) query.getSingleResult();
            String fullName = (String) result[0];
            String phone = (String) result[1];
            java.sql.Date dateOfBirth = (java.sql.Date) result[2];
            int houseNumber = (Integer) result[3];
            String streetName = (String) result[4];
            String email = (String) result[5];
            String city = (String) result[6];
            String district = (String) result[7];
            String province = (String) result[8];
            String password = (String) result[9];

            customerDTO = new CustomerDTO();
            if(dateOfBirth != null) {
                customerDTO.setDateOfBirth(dateOfBirth);
            }

            if(fullName != null) {
                customerDTO.setFullName(fullName);
            }

            if(phone != null) {
                customerDTO.setPhone(phone);
            }


            AddressDTO address = new AddressDTO();
            if(streetName != null) {
                address.setStreetName(streetName);
            }
            if(city != null) {
                address.setCity(city);
            }

            if(district != null) {
                address.setDistrict(district);
            }

            if(province != null) {
                address.setProvince(province);
            }

            address.setHouseNumber(houseNumber);
            customerDTO.setAddressDTO(address);

            AccountDTO account = new AccountDTO();
            if(email != null) {
                account.setEmail(email);
            }
            if(password != null) {
                account.setPassword(password);
            }
            customerDTO.setAccount(account);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return customerDTO;
    }

    public boolean checkAdmin(int userID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String sql = "Select * from Admin inner join User on Admin.userID = User.userID where User.userID = ?";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, userID);
            Object[] result = (Object[]) query.getSingleResult();

            if (result != null){
                System.out.print("co admin");
                return true;
            }else System.out.print("ko admin");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return false;
    }


}
