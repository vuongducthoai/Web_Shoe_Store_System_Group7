package service;

import dto.ResponseDTO;

public interface IResponseService {
    boolean addResponse(ResponseDTO responseDTO);
    boolean deleteResponse(int responseID);
}
