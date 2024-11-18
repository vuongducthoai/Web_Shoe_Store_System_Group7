package dao;

import entity.Account;

public interface IAccountDAO {
    boolean InsertAccount(Account account);
    boolean findAccountForLogin(Account account);
}
