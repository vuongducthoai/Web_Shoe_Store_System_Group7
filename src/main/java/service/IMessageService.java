package service;

import dto.MessageDTO;
import entity.Chat;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface IMessageService {
    Chat getOrCreateChatId(int userId) throws SQLException;
    void saveMessage(MessageDTO message) throws SQLException;
    List<MessageDTO> getMessages(int chatId, Timestamp lastMessageTimestamp) throws SQLException;
    List<MessageDTO> getRecentMessages(int chatId) throws SQLException;
}
