package service;

import dto.AccountDTO;

public interface IAccountService {
    boolean InsertAccount(AccountDTO accountDTO);
    boolean findAccountForLogin(AccountDTO accountDTO);
    AccountDTO findAccountByEmail(String email);
    AccountDTO getAccoutByEmail(String email);
}
