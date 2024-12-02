package service.Impl;

import dao.IAccountDAO;
import dao.ICustomerDAO;
import dao.Impl.AccountDaoImpl;
import dao.Impl.CustomerDAOImpl;
import dto.AccountDTO;
import dto.UserDTO;
import entity.*;
import enums.AuthProvider;
import service.IAccountService;
import util.PasswordHashingSHA;

import java.security.NoSuchAlgorithmException;

public class AccountServiceImpl implements IAccountService {
    private IAccountDAO iAccountDAO = new AccountDaoImpl();
    private ICustomerDAO iCustomerDAO = new CustomerDAOImpl();
    @Override
    public boolean InsertAccount(AccountDTO accountDTO) {
        try {
            /*
                Customer
             */
            Customer customer = new Customer();
            customer.setFullName(accountDTO.getUser().getFullName());
            customer.setActive(true);

            /*
               Cart
             */
            Cart cart = new Cart();
            customer.setCart(cart);
            cart.setCustomer(customer);
            /*
                Chat
             */
            Chat chat = new Chat();
            customer.setChat(chat);
            chat.setCustomer(customer);


           /*
                Address
             */
            Address address = new Address();
            customer.setAddress(address);


            Account account = new Account();
            account.setProviderID(accountDTO.getProviderID());
            account.setAuthProvider(accountDTO.getAuthProvider());
            account.setRole(accountDTO.getRole());
            account.setEmail(accountDTO.getEmail());

            customer.setAccount(account);

            return iCustomerDAO.insertCustomer(customer);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public AccountDTO findAccountForLogin(AccountDTO accountDTO) {
        String password = accountDTO.getPassword();
        PasswordHashingSHA passwordHashingSHA = new PasswordHashingSHA();
        String passwordHash = null;
        try {
            passwordHash = passwordHashingSHA.toHexString(passwordHashingSHA.getSHA(password));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        accountDTO.setPassword(passwordHash);
        return iAccountDAO.findAccountForLogin(accountDTO);
    }

    @Override
    public AccountDTO findAccountByEmail(String email) {
        Account account = iAccountDAO.findAccountByEmail(email);
        if (account != null) {
            AccountDTO accountDTO = new AccountDTO();
            if (account.getUser() != null) {
                accountDTO.setUser(new UserDTO());
                accountDTO.getUser().setUserID(account.getUser().getUserID());
                accountDTO.getUser().setActive(account.getUser().isActive());
            }
            accountDTO.setEmail(account.getEmail());
            accountDTO.setAccountID(account.getAccountID());
            accountDTO.setRole(account.getRole());
            return accountDTO;
        }
        return null;
    }

    @Override
    public AccountDTO findAccoutByProvide(String provideID, AuthProvider authProvider) {
        Account account = iAccountDAO.findAccoutByProvide(provideID, authProvider);
        if (account != null) {
            AccountDTO accountDTO = new AccountDTO();
            if (account.getUser() != null) {
                accountDTO.setUser(new UserDTO());
                accountDTO.getUser().setUserID(account.getUser().getUserID());
                accountDTO.getUser().setActive(account.getUser().isActive());
            }
            accountDTO.setAccountID(account.getAccountID());
            accountDTO.setRole(account.getRole());
            return accountDTO;
        }
        return null;
    }


    @Override
    public AccountDTO getAccountByID(int id) {
        Account account = iAccountDAO.getAccountByID(id);
        if (account != null) {
            AccountDTO accountDTO = new AccountDTO();
            if (account.getUser() != null) {
                accountDTO.setUser(new UserDTO());
                accountDTO.setEmail(account.getEmail());

                accountDTO.getUser().setUserID(account.getUser().getUserID());
                accountDTO.getUser().setActive(account.getUser().isActive());
            }
            accountDTO.setAccountID(account.getAccountID());
            accountDTO.setRole(account.getRole());
            return accountDTO;
        }
        return null;
    }
}
