package dao;

import dto.OrderDTO;

public interface IOrderDao {
    boolean CreateOrder(OrderDTO order);
}
