package com.example.currency.service;

import com.example.currency.model.BillDetails;
import com.example.currency.model.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CalculationService {

    @Value("${customer.tenure}")
    private double customerTenure;

    private final RestTemplate restTemplate;

    public CalculationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double calculatePayableAmount(BillDetails billDetails) {
        List<Item> groceryItems = itemSegregationGrocery(billDetails);
        double actualAmount =  itemsSegregation(billDetails);
        double discount = calculateDiscount(billDetails, actualAmount);


        double groceryDiscount = 0.0;
        if(groceryItems.size() > 0){
            groceryDiscount = discountAppliedAfterPercentageDiscount(billDetails.getTotalAmount() - actualAmount);
            groceryDiscount = billDetails.getTotalAmount() - actualAmount - groceryDiscount;
        }

        double discountedAmount = actualAmount - discount + groceryDiscount;

        double exchangeRate = getExchangeRate(billDetails.getOriginalCurrency(), billDetails.getTargetCurrency());
        return discountedAmount * exchangeRate;
    }

    private double calculateDiscount(BillDetails billDetails, double actualPrice) {
        double discount = 0;

        if (billDetails.getUserType().name().equalsIgnoreCase("employee")) {
            discount = actualPrice * 0.3;
        } else if (billDetails.getUserType().name().equalsIgnoreCase("affiliate")) {
            discount = actualPrice * 0.1;
        } else {
            if (isLongTermEmployee(billDetails.getCustomerTenure(), customerTenure)) {
                discount = actualPrice * 0.05;
            } /*else if (isLongTermEmployee(billDetails.getTotalAmount(), 100)) {
                //discount = actualPrice-5;
                discount = actualPrice * 0.05;
            }*/
        }

        return discount;
    }

    private boolean isLongTermEmployee(double absoluteValue1, double absoluteValue2){
        if(absoluteValue1 >= absoluteValue2){
            return true;
        }else{
            return false;
        }
    }//end isLongTermEmployee

    private double discountAppliedAfterPercentageDiscount(double totalAmount){
        if(isLongTermEmployee(totalAmount, 100)){
            return (totalAmount/100) * 5;
        }else{
            return totalAmount;
        }
    }

    private double getExchangeRate(String originalCurrency, String targetCurrency) {
        String url = "https://open.er-api.com/v6/latest/" + originalCurrency + "?apikey=091e4fe22c2840fbb854ba4112f6f206";
        Map<String, Object> response = restTemplate.getForObject(url, HashMap.class);
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");
        return rates.get(targetCurrency);
    }

    private double itemsSegregation(BillDetails billDetails){
       return  billDetails.getItems().stream().
                filter(item -> !item.getCategory().name()
                        .equalsIgnoreCase("grocery"))
                .map( item -> item.getPrice())
                .reduce(0.0, (price1, price2) -> price1+price2 ).doubleValue();
    }

    private List<Item> itemSegregationGrocery(BillDetails billDetails){
        Iterator<Item> itr = billDetails.getItems().iterator();
        List<Item> groceryItems = new ArrayList<>();

        while(itr.hasNext()){
            Item item = itr.next();
            if(item.getCategory().name().equalsIgnoreCase("grocery")){
                groceryItems.add(item);
                itr.remove();
            }

        }

        return groceryItems;
    }
}

