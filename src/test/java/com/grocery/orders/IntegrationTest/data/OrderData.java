package com.grocery.orders.IntegrationTest.data;

public class OrderData {

    public final static String CREATE_ORDER_PAYLOAD = """
            {
              "customerId": "customer_test_id",
              "customerName": "Customer Test",
              "products": [
                {
                  "productId": "product_test_id"
                }
              ]
            }
    """;
}
