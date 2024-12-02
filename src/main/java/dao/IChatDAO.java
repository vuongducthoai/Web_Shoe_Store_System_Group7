package dao;

import entity.Chat;

public interface IChatDAO {
    int getOrCreateChat(int userId);

}
