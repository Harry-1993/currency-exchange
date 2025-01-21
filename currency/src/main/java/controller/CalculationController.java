package controller;

import model.BillDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CalculationService;

@RestController
@RequestMapping("/api")
public class CalculationController {

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
