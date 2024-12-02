package service;

import dto.AccountDTO;
import entity.Account;
import enums.AuthProvider;

public interface IAccountService {
    boolean InsertAccount(AccountDTO accountDTO);
    AccountDTO findAccountForLogin(AccountDTO accountDTO);
    AccountDTO findAccountByEmail(String email);
    AccountDTO findAccoutByProvide(String provideID, AuthProvider authProvider);
    AccountDTO getAccountByID(int id);

//    boolean updateAccountByAccountID(int accountID, AccountDTO accountDTO);
}
