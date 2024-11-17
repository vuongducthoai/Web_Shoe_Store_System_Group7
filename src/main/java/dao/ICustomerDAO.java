package dao;

import entity.Customer;

public interface ICustomerDAO {
    boolean insertCustomer(Customer customer);
    boolean findAccountByEmail(String email);
}
