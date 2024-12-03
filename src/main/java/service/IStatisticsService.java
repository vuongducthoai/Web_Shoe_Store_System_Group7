package service;

import java.util.Map;

public interface IStatisticsService {


    long totalAmount();
    long InventoryQuantity();
    long getQuantityCompleted();
    long totalMoth();
    Map<Integer, Map<Integer, Long>> totalRevenueForLastFourYears();
    Map<String,Integer> top10Product();
}
