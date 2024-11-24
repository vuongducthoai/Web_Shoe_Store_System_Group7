package dao;

import dto.AccountDTO;
import dto.ProductDTO;
import entity.Account;
import enums.AuthProvider;

import java.util.List;

public interface IAccountDAO {
    boolean InsertAccount(Account account);
    boolean findAccountForLogin(Account account);
    Account findAccountByEmail(String email);
    public Account getAccountByID(int accountID);
    public Account findAccoutByProvide(String provideID, AuthProvider authProvider);
    List<AccountDTO> getListAccountDTO();
    Account findAccountByProvide(String provideID, AuthProvider authProvider);
    boolean updateAccountByAccountID(int accountID, AccountDTO accountDTO);
    boolean updateAccount(AccountDTO accountDTO);
    boolean updateAccountActive(int accountID, int status);
}