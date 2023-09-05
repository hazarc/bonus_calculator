# bonus_calculator
###
### Request to add a customer
POST http://localhost:8080/customers/add
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com"
}


### Request to Add a Transaction
POST http://localhost:8080/customers/1/transactions
Content-Type: application/json

{
  "amount": 120.0
}

### Request to view reward points - returns a customer instance
GET http://localhost:8080/customers/1/reward-points
