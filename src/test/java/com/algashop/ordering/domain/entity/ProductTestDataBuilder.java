package com.algashop.ordering.domain.entity;

import com.algashop.ordering.domain.valueobject.Money;
import com.algashop.ordering.domain.valueobject.Product;
import com.algashop.ordering.domain.valueobject.ProductName;
import com.algashop.ordering.domain.valueobject.id.ProductId;

public class ProductTestDataBuilder {

    public static final ProductId DEFAULT_PRODUCT_ID = new ProductId();

    public ProductTestDataBuilder() {
    }

    public static Product.ProductBuilder aProductMacbook() {
        return Product.builder()
                .id(DEFAULT_PRODUCT_ID)
                .inStock(true)
                .name(new ProductName("Macbook Pro M4"))
                .price(new Money("1299"));
    }

    public static Product.ProductBuilder aProductUnavailable() {
        return Product.builder()
                .id(DEFAULT_PRODUCT_ID)
                .name(new ProductName("Ryzen 7 9800X3D"))
                .price(new Money("899"))
                .inStock(false);
    }

    public static Product.ProductBuilder aProductAltIphone() {
        return Product.builder()
                .id(DEFAULT_PRODUCT_ID)
                .name(new ProductName("Iphone 17"))
                .price(new Money("799"))
                .inStock(true);
    }
}
