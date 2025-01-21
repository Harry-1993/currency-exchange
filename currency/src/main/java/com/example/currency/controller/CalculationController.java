package com.example.currency.controller;

import com.example.currency.model.BillDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.currency.service.CalculationService;

@RestController
@RequestMapping("/api")
public class CalculationController {

    @Autowired
    private final CalculationService calculationService;

    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<Double> calculate(@RequestBody BillDetails billDetails) {
        double payableAmount = calculationService.calculatePayableAmount(billDetails);
        return ResponseEntity.ok(payableAmount);
    }
}
