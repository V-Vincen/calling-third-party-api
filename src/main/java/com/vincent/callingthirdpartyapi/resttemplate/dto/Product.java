package com.vincent.callingthirdpartyapi.resttemplate.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author vincent
 */
@Data
public class Product {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Date date;

    public Product() {
    }

    public Product(Integer id, String name, BigDecimal price, Date date) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
    }
}
