package service.Impl;

import dao.IChatDAO;
import dao.Impl.ChatDAO;
import dto.ChatDTO;
import dto.CustomerDTO;
import dto.MessageDTO;
import entity.Chat;
import entity.Customer;
import jakarta.websocket.Session;
import service.IChatService;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class ChatService implements IChatService {
    private IChatDAO chatDAO = new ChatDAO();

    @Override
    public ChatDTO getOrCreateChatId(int userId) {
        Chat chat = chatDAO.getOrCreateChat(userId); // Lấy hoặc tạo Chat từ DAO
        Customer customer = chat.getCustomer(); // Lấy Customer từ Chat

        // Tạo và trả về ChatDTO
        return new ChatDTO(
                chat.getChatID(),
                new CustomerDTO(customer.getUserID(), customer.getFullName(), customer.getPhone()), // CustomerDTO
                chat.getCreatedDate(),
                chat.getMessages().stream().map(message -> new MessageDTO(
                        message.getMessageID(),
                        chat.getChatID(),
                        message.getUserID(),
                        message.getContent(),
                        new Timestamp(message.getDate().getTime()),
                        message.isRead()
                )).collect(Collectors.toList()) // Convert messages to MessageDTO
        );
    }
}
