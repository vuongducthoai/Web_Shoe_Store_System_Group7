package service.Impl;

import dao.IAccountDAO;
import dao.Impl.AccountDaoImpl;
import dto.AccountDTO;
import entity.Account;
import entity.User;
import service.IAccountService;

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
}
