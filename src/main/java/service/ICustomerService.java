package service;

import dto.CustomerDTO;

import java.util.List;

public interface ICustomerService {
    boolean insertCustomer(CustomerDTO customerDTO);
    boolean updateCustomer(CustomerDTO customerDTO) ;
    CustomerDTO getCustomerByID(int userID);
    List<CustomerDTO> GetAllCustomer();
    }
