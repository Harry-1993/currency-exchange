package com.example.currency.model;

import com.example.currency.enums.CategoryType;

public class Item {
    private String name;
    private CategoryType category;
    private double price;

    public Item(){}

    public Item(String name, double price, CategoryType category) {
        this.name = name;
        this.category = category;
        this.price = price;
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

