package dao;

import dto.AccountDTO;
import entity.Account;
import enums.AuthProvider;

public interface IAccountDAO {
    boolean InsertAccount(Account account);
    boolean findAccountForLogin(Account account);
    Account findAccountByEmail(String email);
    public Account getAccountByID(int accountID);
    public Account findAccoutByProvide(String provideID, AuthProvider authProvider);

    Account findAccountByProvide(String provideID, AuthProvider authProvider);
    boolean updateAccountByAccountID(int accountID, AccountDTO accountDTO);
}