package service.Impl;

import dao.Impl.ChatDAO;
import dao.Impl.MessageDAO;
import dto.ChatDTO;
import dto.CustomerDTO;
import dto.MessageDTO;
import entity.Chat;
import entity.Customer;
import entity.Message;
import service.IMessageService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService implements IMessageService {
    private final ChatDAO chatDAO = new ChatDAO();
    private final MessageDAO messageDAO = new MessageDAO();


    @Override
    public void saveMessage(MessageDTO messageDTO) throws SQLException {
        // Convert DTO to Entity before saving
        Message message = convertDtoToEntity(messageDTO);
        messageDAO.saveMessage(message);
    }

    @Override
    public List<MessageDTO> getMessages(int chatId, Timestamp lastMessageTimestamp) throws SQLException {
        // Get list of messages from DAO
        List<Message> messages = messageDAO.getMessages(chatId, lastMessageTimestamp);
        // Convert to DTOs before returning
        return messages.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageDTO> getRecentMessages(int chatId) throws SQLException {
        // Get list of recent messages from DAO
        List<Message> messages = messageDAO.getRecentMessages(chatId);
        // Convert to DTOs before returning
        return messages.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    // Convert from MessageDTO to Message entity
    private Message convertDtoToEntity(MessageDTO dto) {
        Message message = new Message();
        message.setChat(new Chat(dto.getChatId(), null, null, null));  // Set the Chat entity (simplified)
        message.setUserID(dto.getUserId());
        message.setContent(dto.getContent());
        message.setDate(dto.getDate());
        message.setRead(dto.isRead());
        return message;
    }

    // Convert from Message entity to MessageDTO
    private MessageDTO convertEntityToDto(Message entity) {
        // Directly use Chat's id, avoid recursive conversion for simplicity
        return new MessageDTO(
                entity.getMessageID(),
                entity.getChat().getChatID(),  // Just set chatId, no need for full ChatDTO conversion
                entity.getUserID(),
                entity.getContent(),
                new Timestamp(entity.getDate().getTime()),
                entity.isRead()
        );
    }

    // Convert from ChatDTO to Chat entity (if needed)
    private Chat convertDtoToEntity(ChatDTO dto) {
        Chat chat = new Chat();
        chat.setChatID(dto.getChatId());
        chat.setCustomer(dto.getCustomer() != null ? convertDtoToEntity(dto.getCustomer()) : null);  // Convert CustomerDTO to Customer entity
        chat.setCreatedDate(dto.getCreatedDate());
        return chat;
    }

    // Convert from CustomerDTO to Customer entity (if needed)
    private Customer convertDtoToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setUserID(dto.getUserID());
        customer.setFullName(dto.getFullName());
        customer.setPhone(dto.getPhone());
        return customer;
    }

    @Override
    public void updateMessageStatus(int chatId, int userID){
        messageDAO.updateMessageStatus(chatId,userID);
    }
}
