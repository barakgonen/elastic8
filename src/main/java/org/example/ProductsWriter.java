package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.Arrays;

@AllArgsConstructor
public class ProductsWriter implements Runnable {
    private String productName;
    private ElasticsearchClient elasticsearchClient;
    private static int counter = 0;


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @Override
    public void run() {
        String productId = productName + counter;
        String productName = "City bike: " + getRandomNumber(0, counter);
        Product product = new Product(productId, productName, 123.0 * getRandomNumber(0, counter));

        IndexResponse response = null;
        try {
            response = elasticsearchClient.index(i -> i
                    .index("products")
                    .id(product.getSku())
                    .document(product)
            );
            counter++;
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        System.out.println("Indexed with version " + response.version());

    }
}
