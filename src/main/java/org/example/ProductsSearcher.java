package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;


@AllArgsConstructor
public class ProductsSearcher implements Runnable {
    private ElasticsearchClient elasticsearchClient;

    @Override
    public void run() {
        String searchText = "bike";

        SearchResponse<Product> searchResponse = null;
        try {
            searchResponse = elasticsearchClient.search(s -> s
                            .index("products")
                            .query(q -> q
                                    .match(t -> t
                                            .field("name")
                                            .query(searchText)
                                    )
                            ),
                    Product.class
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        TotalHits total = searchResponse.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;
        if (isExactResult) {
            System.out.println("There are " + total.value() + " results");
        } else {
            System.out.println("There are more than " + total.value() + " results");
        }

        List<Hit<Product>> hits = searchResponse.hits().hits();

        System.out.println("Searcher found: " + hits.size() + " results");
    }
}
