package service.Impl;

import dao.Impl.NotifyDAO;
import dto.NotifyDTO;
import service.INotifyService;

import java.util.List;

public class NotifyServiceImpl implements INotifyService {
    private NotifyDAO notifyDAO = new NotifyDAO();
    @Override
    public List<NotifyDTO> LoadNotifies(int userId){
        return notifyDAO.LoadNotifies(userId);
    }
}
