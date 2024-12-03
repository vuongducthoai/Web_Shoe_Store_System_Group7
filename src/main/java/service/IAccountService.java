package service;

import dto.AccountDTO;
import entity.Account;
import enums.AuthProvider;

import java.util.List;

public interface IAccountService {
    boolean InsertAccount(AccountDTO accountDTO);
    AccountDTO findAccountForLogin(AccountDTO accountDTO);
    AccountDTO findAccountByEmail(String email);
    AccountDTO findAccoutByProvide(String provideID, AuthProvider authProvider);
    AccountDTO getAccountByID(int id);
    //    boolean updateAccountByAccountID(int accountID, AccountDTO accountDTO);
    List<AccountDTO> getListAccounts();
    boolean updateAccount(AccountDTO account);
    boolean updateAccountStatus(int accountID, int status);
}
