package com.example.currency.controller;

import com.example.currency.model.BillDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.currency.service.CalculationService;

/**
 * REST controller for handling calculation-related requests.
 */
@RestController
@RequestMapping("/api")
public class CalculationController {

    @Autowired
    private final CalculationService calculationService;

    /**
     * Constructor for initializing the CalculationController with a CalculationService instance.
     *
     * @param calculationService the service used to perform calculations.
     */
    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    /**
     * Endpoint to calculate the payable amount for a given bill.
     *
     * @param billDetails the details of the bill, including user type, items, and currencies.
     * @return ResponseEntity containing the calculated payable amount.
     */
    @PostMapping("/calculate")
    public ResponseEntity<Double> calculate(@RequestBody BillDetails billDetails) {
        double payableAmount = calculationService.calculatePayableAmount(billDetails);
        return ResponseEntity.ok(payableAmount);
    }
}
