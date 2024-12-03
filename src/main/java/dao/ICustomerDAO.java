package dao;

import dto.CustomerDTO;
import entity.Customer;

import java.util.List;

public interface ICustomerDAO {
    boolean insertCustomer(Customer customer);
    boolean updateCustomerByID(Customer customer);
    public Customer getCustomerByAccountID(int accountID);
    List<CustomerDTO> GetAllCustomer();
    public Integer getUserIDByAccountId(int accountID);
    int getCustomerLoyaty(int userID);
}
