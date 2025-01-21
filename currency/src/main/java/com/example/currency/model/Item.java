package com.example.currency.model;

import com.example.currency.enums.CategoryType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class Item {
    private String name;
    private CategoryType category;
    private double price;

    public Item(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryType getCategory() {
        return category;
    }

    public void setCategory(CategoryType category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

