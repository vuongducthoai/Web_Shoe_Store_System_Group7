package service.Impl;

import dao.IChatDAO;
import dao.Impl.ChatDAO;
import service.IChatService;

public class ChatService implements IChatService {
    private IChatDAO chatDAO = new ChatDAO();

    @Override
    public int getOrCreateChatId(int userId){
        return chatDAO.getOrCreateChatId(userId);
    }
}
