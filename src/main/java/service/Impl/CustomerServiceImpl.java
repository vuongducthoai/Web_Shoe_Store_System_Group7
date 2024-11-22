package service.Impl;

import dao.ICustomerDAO;
import dao.Impl.CustomerDAOImpl;
import dto.CustomerDTO;
import entity.*;
import service.ICustomerService;
import util.PasswordHashingSHA;

import java.security.NoSuchAlgorithmException;

public class CustomerServiceImpl implements ICustomerService {
    private ICustomerDAO customerDAO = new CustomerDAOImpl();
    @Override
    public boolean insertCustomer(CustomerDTO customerDTO) {
    try {
        Customer customer = new Customer();
        customer.setDateOfBirth(customerDTO.getDateOfBirth());
        customer.setUserID(customerDTO.getUserID());
        customer.setPhone(customerDTO.getPhone());
        customer.setFullName(customerDTO.getFullName());
        customer.setActive(true);

        Cart cart = new Cart();
        customer.setCart(cart);
        cart.setCustomer(customer);

        Chat chat = new Chat();
        chat.setUser(customer);
        customer.setChat(chat);


        Address address = new Address();
        address.setHouseNumber(customerDTO.getAddressDTO().getHouseNumber());
        address.setStreetName(customerDTO.getAddressDTO().getStreetName());
        address.setCity(customerDTO.getAddressDTO().getCity());
        address.setProvince(customerDTO.getAddressDTO().getProvince());
        address.setDistrict(customerDTO.getAddressDTO().getDistrict());
        customer.setAddress(address);

        Account account = new Account();
        PasswordHashingSHA passwordHashingSHA = new PasswordHashingSHA();
        account.setEmail(customerDTO.getAccount().getEmail());
        account.setPassword(customerDTO.getAccount().getPassword());
        String password = customerDTO.getAccount().getPassword();
        String passwordHash = null;
        try {
            passwordHash = passwordHashingSHA.toHexString(passwordHashingSHA.getSHA(password));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        account.setPassword(passwordHash);
        account.setAuthProvider(customerDTO.getAccount().getAuthProvider());
        account.setRole(customerDTO.getAccount().getRole());
        customer.setAccount(account);


        if(customerDAO.findAccountByEmail(account.getEmail())){
            System.out.println("12333423432432432432");
            return false;
        }
        System.out.println("ưadeqweqweqweqw");
        return customerDAO.insertCustomer(customer);
    } catch (Exception e){
        System.out.println(e.getMessage());
    }
        return false;
    }
}
