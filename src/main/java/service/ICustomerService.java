package service;

import dto.CustomerDTO;

import java.util.List;

public interface ICustomerService {
    boolean insertCustomer(CustomerDTO customerDTO);
    boolean updateCustomer(CustomerDTO customerDTO) ;
    CustomerDTO getCustomerByAccountID(int accountID);
    List<CustomerDTO> GetAllCustomer();
    public Integer getUserIDByAccountId(int accountID);
    int getCustomerLoyaty(int userID);
    }
