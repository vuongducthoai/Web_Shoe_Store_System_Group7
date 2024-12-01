package dao;

import dto.NotifyDTO;

import java.util.List;

public interface INotifyDAO {
    List<NotifyDTO> LoadNotifies(int userId);
}
