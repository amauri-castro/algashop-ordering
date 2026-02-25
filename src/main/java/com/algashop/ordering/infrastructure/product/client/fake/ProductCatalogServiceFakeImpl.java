package com.algashop.ordering.infrastructure.product.client.fake;

import com.algashop.ordering.core.domain.model.product.ProductCatalogService;
import com.algashop.ordering.core.domain.model.commons.Money;
import com.algashop.ordering.core.domain.model.product.Product;
import com.algashop.ordering.core.domain.model.product.ProductName;
import com.algashop.ordering.core.domain.model.product.ProductId;

import java.util.Optional;

//@Component
public class ProductCatalogServiceFakeImpl implements ProductCatalogService {

    @Override
    public Optional<Product> ofId(ProductId productId) {
        Product product = Product.builder().id(productId)
                .inStock(true)
                .name(new ProductName("Notebook Lenovo"))
                .price(new Money("3000"))
                .build();
        return Optional.of(product);
    }
}
