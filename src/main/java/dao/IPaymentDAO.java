package dao;

import java.util.Map;

public interface IPaymentDAO {
    long totalAmount ();
    long totalMonth();
    Map<Integer, Map<Integer, Long>> totalRevenueForLastFourYears();
}
