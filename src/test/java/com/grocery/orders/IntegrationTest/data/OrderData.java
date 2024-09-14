package com.grocery.orders.IntegrationTest.data;

public class OrderData {

    public static final String CREATE_ORDER_PAYLOAD_0 = """
        {
          "customer_id": "customer_test_id",
          "customer_name": "customer_test_name",
          "products": [
            {
              "product_id": "PWWe3w1SDU",
              "qty": 2
            },
            {
              "product_id": "PWWe3w1SDU",
              "qty": 3
            }
          ]
        }
    """;

    public static final String CREATE_ORDER_PAYLOAD = """
        {
          "customer_id": "customer_test_id",
          "customer_name": "customer_test_name",
          "products": [
            {
              "product_id": "PWWe3w1SDU",
              "qty": 2
            },
            {
              "product_id": "PWWe3w1SDU",
              "qty": 2
            },
            {
              "product_id": "PWWe3w1SDU0000",
              "qty": 3
            }
          ]
        }
    """;


    public static final String CREATE_ORDER_PAYLOAD_INVALID_1 = """
        {
          "customer_name": "customer_test_name",
          "products": [
            {
              "product_id": "PWWe3w1SDU",
              "qty": 2
            },
            {
              "product_id": "PWWe3w1SDU",
              "qty": 2
            },
            {
              "product_id": "PWWe3w1SDU0000",
              "qty": 3
            }
          ]
        }
    """;



    public static final String CREATE_ORDER_PAYLOAD_INVALID_2 = """
        {
          "customer_id": "customer_test_id",
          "products": [
            {
              "product_id": "PWWe3w1SDU",
              "qty": 2
            },
            {
              "product_id": "PWWe3w1SDU",
              "qty": 2
            },
            {
              "product_id": "PWWe3w1SDU0000",
              "qty": 3
            }
          ]
        }
    """;


    public static final String CREATE_ORDER_PAYLOAD_INVALID_3 = """
        {
          "customer_id": "customer_test_id",
          "customer_name": "customer_test_name",
          "products": [
            {
              "qty": 2
            },
            {
              "product_id": "PWWe3w1SDU",
              "qty": 2
            },
            {
              "product_id": "PWWe3w1SDU0000",
              "qty": 3
            }
          ]
        }
    """;



    public static final String MOCK_PRODUCT_RESPONSE = """
            {
              "id": "PWWe3w1SDU",
              "name": "Amazing Burger!",
              "price": 999,
              "promotions": [
                {
                  "id": "ZRAwbsO2qM",
                  "type": "BUY_X_GET_Y_FREE",
                  "required_qty": 2,
                  "free_qty": 1
                }
              ]
            }
        """;

    public static final String ORDER_UPDATE = """
            {
              "products": [
                {
                  "product_id": "PWWe3w1SDU",
                  "qty": 2
                }
              ]
            }
    """;
}
