package service.Impl;

import dao.IAccountDAO;
import dao.Impl.AccountDaoImpl;
import dto.AccountDTO;
import dto.UserDTO;
import entity.Account;
import entity.User;
import enums.AuthProvider;
import service.IAccountService;
import util.PasswordHashingSHA;

import java.security.NoSuchAlgorithmException;

public class AccountServiceImpl implements IAccountService {
   private IAccountDAO iAccountDAO = new AccountDaoImpl();

    @Override
    public boolean InsertAccount(AccountDTO accountDTO) {
        /*
            Account
         */
        Account account = new Account();
        account.setEmail(accountDTO.getEmail());
        account.setProviderID(accountDTO.getProviderID());
        account.setAuthProvider(accountDTO.getAuthProvider());
        account.setRole(accountDTO.getRole());

        /*
        User
         */
        User user = new User();
        user.setFullName(accountDTO.getUser().getFullName());
        user.setPhone(accountDTO.getUser().getPhone());
        user.setAccount(account);

        account.setUser(user);
        return iAccountDAO.InsertAccount(account);
    }

    @Override
    public boolean findAccountForLogin(AccountDTO accountDTO) {
        String password = accountDTO.getPassword();
        PasswordHashingSHA passwordHashingSHA = new PasswordHashingSHA();
        String passwordHash = null;
        try {
            passwordHash = passwordHashingSHA.toHexString(passwordHashingSHA.getSHA(password));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Account account = new Account();
        account.setEmail(accountDTO.getEmail());
        account.setPassword(passwordHash);
        if(iAccountDAO.findAccountForLogin(account)){
            return true;
        }
        return false;
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
    public AccountDTO findAccoutByProvide(String provideID, AuthProvider authProvider){
        Account account = iAccountDAO.findAccoutByProvide(provideID,authProvider);
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
}