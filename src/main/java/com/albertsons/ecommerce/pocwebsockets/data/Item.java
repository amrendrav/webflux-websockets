package com.albertsons.ecommerce.pocwebsockets.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    private String id;

    private String name;
    private BigDecimal price;
}
