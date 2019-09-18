package org.fasttrackit.onlineshop.services;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourcesNotFoundException;
import org.fasttrackit.onlineshop.persistance.ProductRepository;
import org.fasttrackit.onlineshop.transfer.product.SaveProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServices {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServices.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductServices(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(SaveProductRequest request) {

        LOGGER.info("Creating product1234: {}", request );

        Product product1234 = new Product();
        product1234.setName(request.getName());
        product1234.setPrice(request.getPrice());
        product1234.setDescription(request.getDescription());
        product1234.setImagePath(request.getImagePath());
        product1234.setQuantity(request.getQuantity());
        return productRepository.save(product1234);
    }

    public Product getProduct(long id) {
        LOGGER.info("Retrieving product {}", id);
        // using Optional
        return productRepository.findById(id)
                // lambda expressions
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Product " + id + " not found."));
    }

    public Product updateProduct(long id, SaveProductRequest request) {
        LOGGER.info("Updating product {} : {}", id, request);
        Product product = getProduct(id);
        BeanUtils.copyProperties(request, product);
        return productRepository.save(product);
    }

    public void deleteProduct(long id) {
        LOGGER.info("Deleting product {}", id);
        productRepository.deleteById(id);

    }
}
