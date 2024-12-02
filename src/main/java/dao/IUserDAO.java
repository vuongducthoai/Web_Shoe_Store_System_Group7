package dao;

import entity.User;
import jakarta.persistence.EntityManagerFactory;

public interface IUserDAO {
    User getUserByAccountId(int accountID);
}