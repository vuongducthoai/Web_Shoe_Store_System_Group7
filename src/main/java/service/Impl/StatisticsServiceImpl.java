package service.Impl;

import dao.Impl.OrderImpl;
import dao.Impl.OrderItemDAOImpl;
import dao.Impl.PaymentDAOImpl;
import dao.Impl.ProductDAOImpl;
import service.IStatisticsService;

import java.util.Map;

public class StatisticsServiceImpl implements IStatisticsService {
    private final PaymentDAOImpl paymentDAO = new PaymentDAOImpl();
    private final ProductDAOImpl productDAO = new ProductDAOImpl();
    private  final OrderImpl order = new OrderImpl();
    private  final OrderItemDAOImpl orderItemDAO = new OrderItemDAOImpl();
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
    public long totalMoth()
    {
        return paymentDAO.totalMonth();
    }
    public Map<Integer, Map<Integer, Long>> totalRevenueForLastFourYears()
    {
        return  paymentDAO.totalRevenueForLastFourYears();
    }
    public  Map<String,Integer> top10Product()
    {
       return orderItemDAO.findTop10Products();
    }





}
