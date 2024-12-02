package dao;

import dto.CustomerDTO;
import entity.Customer;

import java.util.List;

public interface ICustomerDAO {
    boolean insertCustomer(Customer customer);
    boolean updateCustomerByID(Customer customer);
    Customer getCustomerById(int userID);
    List<CustomerDTO> GetAllCustomer();
}
