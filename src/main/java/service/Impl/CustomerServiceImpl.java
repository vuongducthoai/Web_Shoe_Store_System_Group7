package service.Impl;

import dao.ICustomerDAO;
import dao.Impl.CustomerDAOImpl;
import dto.CustomerDTO;
import entity.Account;
import entity.Address;
import entity.Customer;
import service.ICustomerService;

public class CustomerServiceImpl implements ICustomerService {
    private ICustomerDAO customerDAO = new CustomerDAOImpl();
    @Override
    public boolean insertCustomer(CustomerDTO customerDTO) {

        Customer customer = new Customer();
        customer.setDateOfBirth(customerDTO.getDateOfBirth());
        customer.setUserID(customerDTO.getUserID());
        customer.setPhone(customerDTO.getPhone());
        customer.setFullName(customerDTO.getFullName());

        Address address = new Address();
        address.setCity(customerDTO.getAddressDTO().getCity());
        address.setDistrict(customerDTO.getAddressDTO().getDistrict());
        address.setProvince(customerDTO.getAddressDTO().getProvince());
        address.setHouseNumber(customerDTO.getAddressDTO().getHouseNumber());
        address.setDistrict(customerDTO.getAddressDTO().getDistrict());
        customer.setAddress(address);

        Account account = new Account();
        account.setEmail(customerDTO.getAccount().getEmail());
        account.setPassword(customerDTO.getAccount().getPassword());
        account.setAuthProvider(customerDTO.getAccount().getAuthProvider());
        account.setRole(customerDTO.getAccount().getRole());
        customer.setAccount(account);

        return customerDAO.insertCustomer(customer);

    }
}
