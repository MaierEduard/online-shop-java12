package org.fasttrackit.onlineshop.services;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourcesNotFoundException;
import org.fasttrackit.onlineshop.persistance.ProductRepository;
import org.fasttrackit.onlineshop.transfer.product.GetProductsRequest;
import org.fasttrackit.onlineshop.transfer.product.SaveProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Product getProductById(long id) {
        LOGGER.info("Retrieving product {}", id);
        // using Optional
        return productRepository.findById(id)
                // lambda expressions
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Product " + id + " not found."));
    }


    public Page<Product> getProducts(GetProductsRequest request, Pageable pageable) {
        LOGGER.info("Retrieving products: {}", request);

        if (request != null && request.getPartialName() != null && request.getMinimumQuantity() != null) {
            return productRepository.findByNameContainingAndQuantityGreaterThanEqual(request.getPartialName(), request.getMinimumQuantity(), pageable);
        } else if (request != null && request.getPartialName() != null) {
            return productRepository.findByNameContaining(request.getPartialName(), pageable);
        } else return productRepository.findAll(pageable);
    }


    public Product updateProduct(long id, SaveProductRequest request) {
        LOGGER.info("Updating product {} : {}", id, request);
        Product product = getProductById(id);
        BeanUtils.copyProperties(request, product);
        return productRepository.save(product);
    }

    public void deleteProduct(long id) {
        LOGGER.info("Deleting product {}", id);
        productRepository.deleteById(id);

    }
}
