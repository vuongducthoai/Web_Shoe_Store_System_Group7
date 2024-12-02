package service;

import dto.CustomerDTO;
import dto.UserDTO;

public interface IUserService {
    UserDTO findUserByAccoutId(int accoutId);
    CustomerDTO getInformationUser(int userID);
    boolean checkAdmin(int userID);
}
