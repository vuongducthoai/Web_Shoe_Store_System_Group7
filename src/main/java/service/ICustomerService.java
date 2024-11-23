package service;

import dto.CustomerDTO;

public interface ICustomerService {
    boolean insertCustomer(CustomerDTO customerDTO);
    boolean updateCustomer(CustomerDTO customerDTO) ;
    CustomerDTO getCustomerByID(int userID);
    }
