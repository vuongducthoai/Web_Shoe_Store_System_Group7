package service;

import dto.OrderDTO;

public interface GmailService {
    Boolean sendGmailBill(OrderDTO order,int amount, int discount, int feeShip,String address,String gmail,String orderID);
}
