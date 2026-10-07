package edu.wctc.stockpurchase.service;

import edu.wctc.stockpurchase.entity.StockPurchase;
import edu.wctc.stockpurchase.repo.StockPurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockPurchaseService {

    private final StockPurchaseRepository stockPurchaseRepository;

    @Autowired
    public StockPurchaseService(StockPurchaseRepository stockPurchaseRepository) {
        this.stockPurchaseRepository = stockPurchaseRepository;
    }

    public Iterable<StockPurchase> getAllStockPurchases() {
        return stockPurchaseRepository.findAll();
    }

    public Optional<StockPurchase> getStockPurchaseById(int id) {
        return stockPurchaseRepository.findById(id);
    }

    public StockPurchase createStockPurchase(StockPurchase stockPurchase) {
        stockPurchase.setId(0);
        return stockPurchaseRepository.save(stockPurchase);
    }

    public StockPurchase updateStockPurchase(StockPurchase stockPurchase) {
        return stockPurchaseRepository.save(stockPurchase);
    }

    public void deleteStockPurchase(int id) {
        if (stockPurchaseRepository.existsById(id)) {
            stockPurchaseRepository.deleteById(id);
        }
    }
}
