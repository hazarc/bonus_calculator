# bonus_calculator

## Set up data

**GET** http://localhost:8080/customers/list

#### Response

```json
[
  {
    "id": 1,
    "name": "Customer 1",
    "transactions": [
      {
        "id": 1,
        "amount": 120.0,
        "transactionDate": "2023-07-01"
      },
      {
        "id": 2,
        "amount": 75.0,
        "transactionDate": "2023-08-15"
      },
      {
        "id": 3,
        "amount": 60.0,
        "transactionDate": "2023-09-30"
      }
    ],
    "totalRewardPoints": 0
  }
]
```

### View monthly reward points and total rewards for a valid/existing customer (id = 1)

- **HTTP Request:**
  - **Method:** GET
  - **Endpoint:** http://localhost:8080/customers/1/monthly-reward-points

- **Response:**
  ```json
  {
    "monthlyRewards": {
      "2023-07": 90,
      "2023-08": 25,
      "2023-09": 10
    },
    "totalRewards": 125
  }
```
