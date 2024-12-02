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
    public int getOrCreateChatId(int userId) {
        return chatDAO.getOrCreateChat(userId);
    }
}
