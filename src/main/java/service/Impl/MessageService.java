package service.Impl;

import dao.Impl.ChatDAO;
import dao.Impl.MessageDAO;
import dto.MessageDTO;
import entity.Message;
import service.IMessageService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService implements IMessageService {
    private final ChatDAO chatDAO = new ChatDAO();
    private final MessageDAO messageDAO = new MessageDAO();

    @Override
    public int getOrCreateChatId(int userId) throws SQLException {
        return chatDAO.getOrCreateChatId(userId);
    }

    @Override
    public void saveMessage(MessageDTO messageDTO) throws SQLException {
        // Chuyển đổi từ DTO sang Entity trước khi lưu
        Message message = convertDtoToEntity(messageDTO);
        messageDAO.saveMessage(message);
    }

    @Override
    public List<MessageDTO> getMessages(int chatId, Timestamp lastMessageTimestamp) throws SQLException {
        // Lấy danh sách Entity từ DAO
        List<Message> messages = messageDAO.getMessages(chatId, lastMessageTimestamp);
        // Chuyển đổi sang DTO trước khi trả về
        return messages.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageDTO> getRecentMessages(int chatId) throws SQLException {
        // Lấy danh sách Entity từ DAO
        List<Message> messages = messageDAO.getRecentMessages(chatId);
        // Chuyển đổi sang DTO trước khi trả về
        return messages.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    // Phương thức chuyển đổi từ DTO sang Entity
    private Message convertDtoToEntity(MessageDTO dto) {
        Message message = new Message();
        message.setChat(dto.getChat());
        message.setUserID(dto.getUserId());
        message.setContent(dto.getContent());
        message.setDate(dto.getDate());
        message.setRead(dto.isRead());
        return message;
    }


    // Phương thức chuyển đổi từ Entity sang DTO
    private MessageDTO convertEntityToDto(Message entity) {
        return new MessageDTO(
                entity.getMessageID(),
                entity.getChat(),
                entity.getUserID(),
                entity.getContent(),
                (Timestamp) entity.getDate(),
                entity.isRead()// Thêm giá trị isRead
        );
    }
}
