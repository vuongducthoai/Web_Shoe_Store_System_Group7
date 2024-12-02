package dao;

import dto.CustomerDTO;
import entity.Customer;
import entity.User;
import java.util.*;

public interface IUserDAO {
    User getUserByAccountId(int accountID);

    CustomerDTO getInformationUser(int userID);
}
