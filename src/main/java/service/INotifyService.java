package service;

import dto.NotifyDTO;

import java.util.List;

public interface INotifyService {
    public List<NotifyDTO> LoadNotifies(int userId);
}
