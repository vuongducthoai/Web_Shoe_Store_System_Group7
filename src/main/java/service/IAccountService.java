package service;

import dto.AccountDTO;
import entity.Account;
import enums.AuthProvider;

public interface IAccountService {
    boolean InsertAccount(AccountDTO accountDTO);
    boolean findAccountForLogin(AccountDTO accountDTO);
    AccountDTO findAccountByEmail(String email);
    AccountDTO findAccoutByProvide(String provideID, AuthProvider authProvider);
}
