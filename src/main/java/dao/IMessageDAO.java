package dao;

import entity.Message;

import java.sql.Timestamp;
import java.util.List;

public interface IMessageDAO {
    void saveMessage(Message message);
    List<Message> getMessages(int chatId, Timestamp lastMessageTimestamp);
    List<Message> getRecentMessages(int chatId);
    void updateMessageStatus(int chatId, int userID);
}
