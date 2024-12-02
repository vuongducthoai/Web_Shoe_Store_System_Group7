package service.Impl;

import dao.IUserDAO;
import dao.Impl.UserDAOImpl;
import dto.AccountDTO;
import dto.UserDTO;
import entity.User;
import service.IUserService;

public class UserServiceImpl implements IUserService {

    IUserDAO userDAO = new UserDAOImpl();
    @Override
    public UserDTO findUserByAccoutId(int accoutId) {

        User user = userDAO.getUserByAccountId(accoutId);
        if(user != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserID(user.getUserID());
            userDTO.setActive(user.isActive());
            AccountDTO account = new AccountDTO();
            account.setRole(user.getAccount().getRole());
            userDTO.setAccount(account);
            return userDTO;
        }
        return null;
    }

    public boolean checkAdmin(int userID){
       return userDAO.checkAdmin(userID);
    }
}
