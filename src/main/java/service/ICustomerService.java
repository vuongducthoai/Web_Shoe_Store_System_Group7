package service;

import dto.CustomerDTO;

public interface ICustomerService {
    boolean insertCustomer(CustomerDTO customerDTO);
}
