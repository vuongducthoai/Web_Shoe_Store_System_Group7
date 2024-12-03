package dao;

import java.util.Map;

public interface IOrderItemDAO {
    Map<String, Integer> findTop10Products();

}
