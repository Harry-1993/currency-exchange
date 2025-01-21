package service;

import model.BillDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CalculationService {

    private final RestTemplate restTemplate;

    public CalculationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double calculatePayableAmount(BillDetails billDetails) {
        double discount = calculateDiscount(billDetails);
        double discountedAmount = billDetails.getTotalAmount() - discount;
        double exchangeRate = getExchangeRate(billDetails.getOriginalCurrency(), billDetails.getTargetCurrency());
        return discountedAmount * exchangeRate;
    }

    private double calculateDiscount(BillDetails billDetails) {
        double discount = 0;

        if (billDetails.getUserType().equalsIgnoreCase("employee")) {
            discount = billDetails.getTotalAmount() * 0.3;
        } else if (billDetails.getUserType().equalsIgnoreCase("affiliate")) {
            discount = billDetails.getTotalAmount() * 0.1;
        } else if (billDetails.getCustomerTenure() > 2) {
            discount = billDetails.getTotalAmount() * 0.05;
        }

        discount += (int) (billDetails.getTotalAmount() / 100) * 5;

        return discount;
    }

    private double getExchangeRate(String originalCurrency, String targetCurrency) {
        String url = "https://open.er-api.com/v6/latest/" + originalCurrency + "?apikey=your-api-key";
        Map<String, Object> response = restTemplate.getForObject(url, HashMap.class);
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");
        return rates.get(targetCurrency);
    }
}

