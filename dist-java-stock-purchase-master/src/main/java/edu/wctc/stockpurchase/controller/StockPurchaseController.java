package edu.wctc.stockpurchase.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import edu.wctc.stockpurchase.entity.StockPurchase;
import edu.wctc.stockpurchase.service.StockPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/stockpurchases")
public class StockPurchaseController {

    private final StockPurchaseService stockPurchaseService;
    private final ObjectMapper objectMapper;

    @Autowired
    public StockPurchaseController(StockPurchaseService stockPurchaseService,
                                   ObjectMapper objectMapper) {
        this.stockPurchaseService = stockPurchaseService;
        this.objectMapper = objectMapper;
    }

    // GET ALL
    @GetMapping
    public Iterable<StockPurchase> getAllStockPurchases() {
        return stockPurchaseService.getAllStockPurchases();
    }

    // GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<StockPurchase> getStockPurchase(@PathVariable int id) {

        Optional<StockPurchase> stockPurchase =
                stockPurchaseService.getStockPurchaseById(id);

        return stockPurchase
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping
    public StockPurchase createStockPurchase(
            @RequestBody StockPurchase stockPurchase) {

        return stockPurchaseService.createStockPurchase(stockPurchase);
    }

    // PUT
    @PutMapping
    public ResponseEntity<StockPurchase> updateStockPurchase(
            @RequestBody StockPurchase stockPurchase) {

        if (stockPurchaseService
                .getStockPurchaseById(stockPurchase.getId())
                .isEmpty()) {

            return ResponseEntity.notFound().build();
        }

        StockPurchase updated =
                stockPurchaseService.updateStockPurchase(stockPurchase);

        return ResponseEntity.ok(updated);
    }

    // PATCH
    @PatchMapping("/{id}")
    public ResponseEntity<StockPurchase> patchStockPurchase(
            @PathVariable int id,
            @RequestBody JsonPatch patch) {

        Optional<StockPurchase> optionalStockPurchase =
                stockPurchaseService.getStockPurchaseById(id);

        if (optionalStockPurchase.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            StockPurchase stockPurchase = optionalStockPurchase.get();

            JsonNode patched =
                    patch.apply(objectMapper.convertValue(
                            stockPurchase,
                            JsonNode.class));

            StockPurchase patchedStockPurchase =
                    objectMapper.treeToValue(
                            patched,
                            StockPurchase.class);

            patchedStockPurchase.setId(id);

            StockPurchase saved =
                    stockPurchaseService.updateStockPurchase(
                            patchedStockPurchase);

            return ResponseEntity.ok(saved);

        } catch (JsonPatchException | com.fasterxml.jackson.core.JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockPurchase(
            @PathVariable int id) {

        stockPurchaseService.deleteStockPurchase(id);

        return ResponseEntity.noContent().build();
    }
}