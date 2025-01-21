package com.example.currency.service;

import com.example.currency.model.BillDetails;
import com.example.currency.model.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Service class for handling business logic related to bill calculations.
 */
@Service
public class CalculationService {

    /**
     * The minimum tenure in years for a customer to qualify for a tenure-based discount.
     */
    @Value("${customer.tenure}")
    private double customerTenure;

    /**
     * RestTemplate instance for making external API calls.
     */
    private final RestTemplate restTemplate;

    /**
     * Constructor for initializing the CalculationService with a RestTemplate.
     *
     * @param restTemplate the RestTemplate for API calls.
     */
    public CalculationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Calculates the payable amount for a given bill after applying applicable discounts and currency conversion.
     *
     * @param billDetails the details of the bill, including items, user type, and currency details.
     * @return the final payable amount after discounts and conversion.
     */
    public double calculatePayableAmount(BillDetails billDetails) {
        List<Item> groceryItems = itemSegregationGrocery(billDetails);
        double actualAmount = itemsSegregation(billDetails);
        double discount = calculateDiscount(billDetails, actualAmount);

        double groceryDiscount = 0.0;
        if (groceryItems.size() > 0) {
            groceryDiscount = discountAppliedAfterPercentageDiscount(billDetails.getTotalAmount() - actualAmount);
            groceryDiscount = billDetails.getTotalAmount() - actualAmount - groceryDiscount;
        }

        double discountedAmount = actualAmount - discount + groceryDiscount;
        double exchangeRate = getExchangeRate(billDetails.getOriginalCurrency(), billDetails.getTargetCurrency());
        return discountedAmount * exchangeRate;
    }

    /**
     * Calculates the discount based on user type and customer tenure.
     *
     * @param billDetails  the details of the bill.
     * @param actualPrice  the total price of non-grocery items.
     * @return the calculated discount amount.
     */
    private double calculateDiscount(BillDetails billDetails, double actualPrice) {
        double discount = 0;

        if (billDetails.getUserType().name().equalsIgnoreCase("employee")) {
            discount = actualPrice * 0.3;
        } else if (billDetails.getUserType().name().equalsIgnoreCase("affiliate")) {
            discount = actualPrice * 0.1;
        } else {
            if (isLongTermEmployee(billDetails.getCustomerTenure(), customerTenure)) {
                discount = actualPrice * 0.05;
            }
        }

        return discount;
    }

    /**
     * Determines if a customer qualifies as a long-term employee.
     *
     * @param absoluteValue1 the customer's tenure or other criteria.
     * @param absoluteValue2 the threshold value to qualify.
     * @return true if the customer qualifies, false otherwise.
     */
    private boolean isLongTermEmployee(double absoluteValue1, double absoluteValue2) {
        return absoluteValue1 >= absoluteValue2;
    }

    /**
     * Applies an additional discount for amounts over a certain threshold.
     *
     * @param totalAmount the total amount to consider for discount.
     * @return the discount amount or 0 if not applicable.
     */
    private double discountAppliedAfterPercentageDiscount(double totalAmount) {
        if (isLongTermEmployee(totalAmount, 100)) {
            return (totalAmount / 100) * 5;
        } else {
            return 0.0;
        }
    }

    /**
     * Fetches the exchange rate for converting between currencies.
     *
     * @param originalCurrency the currency of the original amount.
     * @param targetCurrency   the desired currency for conversion.
     * @return the exchange rate.
     */
    private double getExchangeRate(String originalCurrency, String targetCurrency) {
        String url = "https://open.er-api.com/v6/latest/" + originalCurrency + "?apikey=091e4fe22c2840fbb854ba4112f6f206";
        Map<String, Object> response = restTemplate.getForObject(url, HashMap.class);
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");
        Object price = rates.get(targetCurrency);
        if (price instanceof Integer value) {
            return value.doubleValue();
        }
        return rates.get(targetCurrency);
    }

    /**
     * Calculates the total price of non-grocery items in the bill.
     *
     * @param billDetails the details of the bill.
     * @return the total price of non-grocery items.
     */
    private double itemsSegregation(BillDetails billDetails) {
        return billDetails.getItems().stream()
                .filter(item -> !item.getCategory().name().equalsIgnoreCase("grocery"))
                .map(Item::getPrice)
                .reduce(0.0, Double::sum);
    }

    /**
     * Segregates grocery items from the bill and removes them from the item list.
     *
     * @param billDetails the details of the bill.
     * @return a list of grocery items.
     */
    private List<Item> itemSegregationGrocery(BillDetails billDetails) {
        Iterator<Item> itr = billDetails.getItems().iterator();
        List<Item> groceryItems = new ArrayList<>();

        while (itr.hasNext()) {
            Item item = itr.next();
            if (item.getCategory().name().equalsIgnoreCase("grocery")) {
                groceryItems.add(item);
                itr.remove();
            }
        }

        return groceryItems;
    }
}
