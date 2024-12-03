package dao;

import dto.CustomerDTO;
import entity.User;

public interface IUserDAO {
    User getUserByAccountId(int accountID);

    CustomerDTO getInformationUser(int userID);
    boolean checkAdmin(int userID);
}

