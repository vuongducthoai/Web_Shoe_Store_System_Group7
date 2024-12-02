package service;

import dto.ChatDTO;
import dto.MessageDTO;
import entity.Chat;
import jakarta.websocket.Session;

import java.util.List;

public interface IChatService {
    int getOrCreateChatId(int userId);
}
