package service.Impl;

import Authentication.ResetPassword;
import controller.login.ForgetPassword;
import dto.OrderDTO;
import service.GmailService;

public class GmailServiceImpl implements GmailService {
    ResetPassword resetPassword = new ResetPassword();
    @Override
    public Boolean sendGmailBill(OrderDTO order, int amount, int discount, int feeShip, String address,String gmail,String orderID) {
        return resetPassword.sendBill(gmail,amount,discount,feeShip,order,orderID,address);
    }
}
