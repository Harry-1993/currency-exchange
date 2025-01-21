# Currency Exchange and Discount Application

This is a Spring Boot application designed to calculate the final payable amount for a bill, including currency exchange and discount calculations.

## Features
- Integrates with a currency exchange API to retrieve real-time exchange rates.
- Applies discounts based on user type and bill amount.
- Exposes a REST API endpoint for input and output.

## Requirements
- Java 11+
- Maven/Gradle
- Spring Boot
- Mockito for testing

## Setup
1. Clone the repository.
2. Update the API key in `CalculationService.java`.
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Usage
### Endpoint
`POST /api/calculate`

### Request Body
```json
{
  "userType": "employee",
  "customerTenure": 3,
  "totalAmount": 200,
  "originalCurrency": "USD",
  "targetCurrency": "EUR",
  "items": [
    {
      "name": "Laptop",
      "category": "Electronics",
      "price": 200
    }
  ]
}
```

### Response
```json
110.5
```

## Testing
Run unit tests with:
```bash
mvn test
```