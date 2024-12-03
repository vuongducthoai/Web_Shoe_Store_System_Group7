package service.Impl;

import JpaConfig.JpaConfig;
import dao.ICustomerDAO;
import dao.Impl.CustomerDAOImpl;
import dto.AccountDTO;
import dto.AddressDTO;
import dto.CustomerDTO;
import dto.UserDTO;
import entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.checkerframework.checker.units.qual.C;
import service.IAccountService;
import service.ICustomerService;
import util.PasswordHashingSHA;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class CustomerServiceImpl implements ICustomerService {
    private ICustomerDAO customerDAO = new CustomerDAOImpl();
    private IAccountService accountService = new AccountServiceImpl();
    @Override
    public boolean insertCustomer(CustomerDTO customerDTO) {
        try {
            Account account = new Account();
            account.setEmail(customerDTO.getAccount().getEmail());
            // Check if email already exists
            if (accountService.findAccountByEmail(account.getEmail()) != null) {
                System.out.println("Email already exists!");
                return false;  // Early return if email exists
            }


            // Create new Customer entity from DTO
            Customer customer = new Customer();
            customer.setDateOfBirth(customerDTO.getDateOfBirth());
            customer.setPhone(customerDTO.getPhone());
            customer.setFullName(customerDTO.getFullName());
            customer.setActive(true);

            // Create and set Cart and Chat
            Cart cart = new Cart();
            customer.setCart(cart);
            cart.setCustomer(customer);

            Chat chat = new Chat();
            chat.setCustomer(customer);
            customer.setChat(chat);
            // Set Address
            Address address = new Address();
            address.setHouseNumber(customerDTO.getAddressDTO().getHouseNumber());
            address.setStreetName(customerDTO.getAddressDTO().getStreetName());
            address.setCity(customerDTO.getAddressDTO().getCity());
            address.setProvince(customerDTO.getAddressDTO().getProvince());
            address.setDistrict(customerDTO.getAddressDTO().getDistrict());
            customer.setAddress(address);

            // Create Account with hashed password

            PasswordHashingSHA passwordHashingSHA = new PasswordHashingSHA();



            String password = customerDTO.getAccount().getPassword();
            try {
                String passwordHash = passwordHashingSHA.toHexString(passwordHashingSHA.getSHA(password));
                account.setPassword(passwordHash);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            account.setAuthProvider(customerDTO.getAccount().getAuthProvider());
            account.setRole(customerDTO.getAccount().getRole());
            customer.setAccount(account);

            // Insert the new customer
            return customerDAO.insertCustomer(customer);
        } catch (Exception e) {
            System.out.println("Error in insertCustomer: " + e.getMessage());
        }
        return false;
    }


    public AddressDTO convertToAddressDTO(Address address) {

        if (address == null) {
            System.out.println("loi address null");
            return null; // Trả về null nếu Address rỗng
        }
        System.out.println(address.getAddressID());

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressID(address.getAddressID());
        addressDTO.setHouseNumber(address.getHouseNumber());
        addressDTO.setStreetName(address.getStreetName());
        addressDTO.setDistrict(address.getDistrict());
        addressDTO.setCity(address.getCity());

        return addressDTO;
    }

    @Override
    public CustomerDTO getCustomerByAccountID(int accountID) {
        // Tìm khách hàng theo ID
        Customer customer = customerDAO.getCustomerByAccountID(accountID);

        if (customer != null) {
            // Tạo CustomerDTO để chuyển đổi dữ liệu
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setLoyalty(customer.getLoyalty());
            customerDTO.setFullName(customer.getFullName());
            customerDTO.setDateOfBirth(customer.getDateOfBirth());
            customerDTO.setAddressDTO(convertToAddressDTO(customer.getAddress()));
            customerDTO.setUserID(customer.getUserID());
            customerDTO.setPhone(customer.getPhone());
            customerDTO.setActive(customer.isActive());


            // Nếu khách hàng có tài khoản (Account), sao chép thông tin tài khoản
            if (customer.getAccount() != null) {


                customerDTO.setUserID(customer.getAccount().getAccountID());

            }

            return customerDTO;
        }

        // Nếu không tìm thấy khách hàng, trả về null
        return null;
    }

    @Override
    public boolean updateCustomer(CustomerDTO customerDTO) {

        try {
            Account account = new Account();
            account.setEmail(customerDTO.getAccount().getEmail());
            // Check if email already exists
            if (accountService.findAccountByEmail(account.getEmail()) != null) {
                System.out.println("Email already exists!");
                return false;  // Early return if email exists
            }

            // Create new Customer entity from DTO

            Customer customer = new Customer();
            customer.setDateOfBirth(customerDTO.getDateOfBirth());
            customer.setUserID(customerDTO.getUserID());
            customer.setPhone(customerDTO.getPhone());
            customer.setFullName(customerDTO.getFullName());

            // Set Address
            Address address = new Address();
            address.setHouseNumber(customerDTO.getAddressDTO().getHouseNumber());
            address.setStreetName(customerDTO.getAddressDTO().getStreetName());
            address.setCity(customerDTO.getAddressDTO().getCity());
            address.setProvince(customerDTO.getAddressDTO().getProvince());
            address.setDistrict(customerDTO.getAddressDTO().getDistrict());
            customer.setAddress(address);

            // Create Account with hashed password

            PasswordHashingSHA passwordHashingSHA = new PasswordHashingSHA();

            String password = customerDTO.getAccount().getPassword();
            try {
                String passwordHash = passwordHashingSHA.toHexString(passwordHashingSHA.getSHA(password));
                account.setPassword(passwordHash);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            account.setEmail(customerDTO.getAccount().getEmail());
            customer.setAccount(account);


            // Insert the new customer
            return customerDAO.updateCustomerByID(customer);
        } catch (Exception e) {
            System.out.println("Error in insertCustomer: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<CustomerDTO> GetAllCustomer(){
        return customerDAO.GetAllCustomer();
    }
    public Integer getUserIDByAccountId(int accountID){
        return customerDAO.getUserIDByAccountId(accountID);
    }

    public int getCustomerLoyaty(int userID){
        return customerDAO.getCustomerLoyaty(userID);
    }
}
