package service.Impl;

import dao.Impl.PaymentDAOImpl;
import dao.Impl.ProductDAOImpl;
import service.IStatisticsService;

public class StatisticsServiceImpl implements IStatisticsService {
    private final PaymentDAOImpl paymentDAO = new PaymentDAOImpl();
    private final ProductDAOImpl productDAO = new ProductDAOImpl();
    @Override

    //tong doanh thu
    public  long totalAmount(){
       return paymentDAO.totalAmount();
    }
    //San pham ton kho
    public long InventoryQuantity(){

        return productDAO.getInventoryQuantity();
    }





}
