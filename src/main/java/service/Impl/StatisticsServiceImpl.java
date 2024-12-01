package service.Impl;

import dao.Impl.OrderImpl;
import dao.Impl.PaymentDAOImpl;
import dao.Impl.ProductDAOImpl;
import service.IStatisticsService;

public class StatisticsServiceImpl implements IStatisticsService {
    private final PaymentDAOImpl paymentDAO = new PaymentDAOImpl();
    private final ProductDAOImpl productDAO = new ProductDAOImpl();
    private  final OrderImpl order = new OrderImpl();
    @Override

    //tong doanh thu
    public  long totalAmount(){
       return paymentDAO.totalAmount();
    }
    //San pham ton kho
    public long InventoryQuantity(){

        return productDAO.getInventoryQuantity();
    }
    public long getQuantityCompleted(){
        return order.quantityOrderCompleted();
    }





}
