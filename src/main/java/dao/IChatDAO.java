package dao;

import entity.Chat;

public interface IChatDAO {
    Chat getOrCreateChat(int userId);

}
