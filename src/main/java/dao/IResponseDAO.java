package dao;

import entity.Response;

public interface IResponseDAO {
    boolean addResponse(Response response);
    boolean deleteResponse(int responseID);
}
