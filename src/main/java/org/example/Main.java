package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    private static ElasticsearchClient elasticsearchClient = null;

    public static void main(String[] args) throws IOException {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(4);

        scheduledThreadPoolExecutor.scheduleAtFixedRate(new ProductsWriter("bk-", getElasticsearchClient()), 1, 100, TimeUnit.MILLISECONDS);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new ProductsWriter("ck-", getElasticsearchClient()), 1, 200, TimeUnit.MILLISECONDS);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new ProductsSearcher(getElasticsearchClient()), 4, 250, TimeUnit.MILLISECONDS);
//        scheduledThreadPoolExecutor.scheduleAtFixedRate(new ProductsSearcher(getElasticsearchClient()), 4, 2, TimeUnit.SECONDS);
    }

    private static ElasticsearchClient getElasticsearchClient() {
        if (elasticsearchClient == null) {
            RestClient httpClient = RestClient.builder(
                    new HttpHost("localhost", 9200)
            ).build();
            ElasticsearchTransport transport = new RestClientTransport(
                    httpClient,
                    new JacksonJsonpMapper()
            );
            elasticsearchClient = new ElasticsearchClient(transport);
        }

        return elasticsearchClient;
    }
}
