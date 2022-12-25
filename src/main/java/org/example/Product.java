package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Product {
    private String sku;
    private String name;
    private double price;

    public Product() {
    }
}
