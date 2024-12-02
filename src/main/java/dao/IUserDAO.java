package dao;

import entity.User;

public interface IUserDAO {
    User getUserByAccountId(int accountID);
}
