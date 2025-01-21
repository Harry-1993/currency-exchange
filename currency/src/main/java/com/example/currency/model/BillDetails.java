package com.example.currency.model;

import com.example.currency.enums.UserType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public class BillDetails {
    private List<Item> items;
    private UserType userType;
    private double customerTenure;
    private double totalAmount;
    private String originalCurrency;
    private String targetCurrency;

    public BillDetails(){

    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public double getCustomerTenure() {
        return customerTenure;
    }

    public void setCustomerTenure(double customerTenure) {
        this.customerTenure = customerTenure;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(String originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }
}
