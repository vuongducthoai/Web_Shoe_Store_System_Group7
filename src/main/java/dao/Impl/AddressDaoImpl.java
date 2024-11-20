package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IAddressDAO;
import dto.AddressDTO;
import entity.Address;
import entity.Customer;
import entity.User;
import jakarta.persistence.EntityManager;

public class AddressDaoImpl implements IAddressDAO {
    public AddressDTO getAddressByID(int idUser){
        try {
            EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
            Customer customer = entityManager.createQuery("select c from Customer c where c.userID = :userID", Customer.class)
                    .setParameter("userID", idUser).getSingleResult();
            if (customer.getAddress() != null) {
                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setId(customer.getAddress().getId());
                addressDTO.setProvince(customer.getAddress().getProvince());
                addressDTO.setCity(customer.getAddress().getCity());
                addressDTO.setDistrict(customer.getAddress().getDistrict());
                addressDTO.setStreetName(customer.getAddress().getStreetName());
                return addressDTO;
            }
            else return null;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
