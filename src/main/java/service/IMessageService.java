package service;

import dto.MessageDTO;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface IMessageService {
    int getOrCreateChatId(int userId) throws SQLException;
    public void saveMessage(MessageDTO message) throws SQLException;
    List<MessageDTO> getMessages(int chatId, Timestamp lastMessageTimestamp) throws SQLException;
    public List<MessageDTO> getRecentMessages(int chatId) throws SQLException;
}
