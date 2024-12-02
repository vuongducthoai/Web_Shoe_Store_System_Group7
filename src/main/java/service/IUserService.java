package service;

import dto.UserDTO;

public interface IUserService {
    UserDTO findUserByAccoutId(int accoutId);
    boolean checkAdmin(int userID);
}
