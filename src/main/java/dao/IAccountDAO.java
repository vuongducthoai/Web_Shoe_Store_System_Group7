package dao;

import dto.AccountDTO;
import entity.Account;
import enums.AuthProvider;

public interface IAccountDAO {
    boolean InsertAccount(Account account);
    boolean findAccountForLogin(Account account);
    Account findAccountByEmail(String email);
    Account findAccountById(int id);
    Account findAccoutByProvide(String provideID, AuthProvider authProvider);
}
