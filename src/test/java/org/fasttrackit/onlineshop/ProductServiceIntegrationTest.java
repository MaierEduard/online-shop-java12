package org.fasttrackit.onlineshop;


import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourcesNotFoundException;
import org.fasttrackit.onlineshop.services.ProductServices;
import org.fasttrackit.onlineshop.transfer.product.SaveProductRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringRunner.class)
@SpringBootTest

public class ProductServiceIntegrationTest {

    @Autowired
    private ProductServices productServices;

    @Test
    public void testCreatedProduct_whenValidRequest_thenReturnCreatedProduct() {
        createProduct();
    }

    @Test(expected = TransactionSystemException.class)
    public void testCreateProduct_whenInvalidRequest_thanThrowException() {
        SaveProductRequest saveProductRequest = new SaveProductRequest();

        productServices.createProduct(saveProductRequest);
    }

    @Test
    public void testGetProductById_whenExistingEntity_thenReturnProduct() {

        Product creatProduct = createProduct();

        Product retrievedProduct = productServices.getProductById(creatProduct.getId());
        assertThat(retrievedProduct, notNullValue());
        assertThat(retrievedProduct.getId(), is(creatProduct.getId()));
        assertThat(retrievedProduct.getName(), is(creatProduct.getName()));
    }


    @Test(expected = ResourcesNotFoundException.class)
    public void testGetProduct_whenNonExistingEntity_thenThrowNotFoundException() {
        productServices.getProductById(999999);
    }

    @Test
    public void testGetUpdateById_whenValidRequest_thenReturnUpdatedProduct() {
        Product createdProduct = createProduct();

        SaveProductRequest request = new SaveProductRequest();
        request.setName(createdProduct.getName() + "Updated");
        request.setPrice(createdProduct.getPrice() + 10);
        request.setQuantity(createdProduct.getQuantity() + 10);

        Product productUpdated = productServices.updateProduct(createdProduct.getId(), request);

        assertThat(productUpdated.getName(), is(request.getName()));
        assertThat(productUpdated.getPrice(), is(request.getPrice()));
        assertThat(productUpdated.getQuantity(), is(request.getQuantity()));
        assertThat(productUpdated.getDescription(), is(request.getDescription()));

    }


    private Product createProduct() {
        SaveProductRequest request = new SaveProductRequest();
        request.setName("Computer");
        request.setDescription("Some Description");
        request.setPrice(2000);
        request.setQuantity(100);

        Product product123 = productServices.createProduct(request);

        assertThat(product123, notNullValue());
        assertThat(product123.getDescription(), is(request.getDescription()));
        assertThat(product123.getName(), is(request.getName()));
        assertThat(product123.getPrice(), is(request.getPrice()));
        assertThat(product123.getQuantity(), is(request.getQuantity()));
        assertThat(product123.getId(), notNullValue());
        assertThat(product123.getId(), greaterThan(0L));

        return product123;
    }

}
