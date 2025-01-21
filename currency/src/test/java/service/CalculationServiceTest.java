package service;

import com.example.currency.enums.CategoryType;
import com.example.currency.enums.UserType;
import com.example.currency.model.BillDetails;
import com.example.currency.model.Item;
import com.example.currency.service.CalculationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link CalculationService}.
 * This class contains test cases for verifying the calculation logic of the service.
 */
class CalculationServiceTest {

    /** The service being tested. */
    @InjectMocks
    private CalculationService calculationService;

    /** Mocked {@link RestTemplate} for mocking API calls. */
    @Mock
    private RestTemplate restTemplate;

    /**
     * Initializes the mocks for this test class.
     */
    public CalculationServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for verifying the payable amount calculation for an employee.
     * It tests the discount and exchange rate application for a user of type `EMPLOYEE`.
     */
    @Test
    void testCalculatePayableAmountForEmployee() {
        // Arrange
        BillDetails billDetails = new BillDetails();
        billDetails.setUserType(UserType.EMPLOYEE);
        billDetails.setTotalAmount(500);
        billDetails.setOriginalCurrency("USD");
        billDetails.setTargetCurrency("EUR");
        billDetails.setCustomerTenure(5);

        // Create list of items with grocery and non-grocery categories
        List<Item> items = new ArrayList<>();
        items.add(new Item("Apple", 50, CategoryType.GROCERY));
        items.add(new Item("Laptop", 450, CategoryType.ELECTRONICS));
        billDetails.setItems(items);

        // Mock the exchange rate API response
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 0.965172);
        mockResponse.put("rates", rates);

        when(restTemplate.getForObject(
                "https://open.er-api.com/v6/latest/USD?apikey=091e4fe22c2840fbb854ba4112f6f206",
                HashMap.class
        )).thenReturn((HashMap) mockResponse);

        // Act
        double payableAmount = calculationService.calculatePayableAmount(billDetails);

        // Assert
        // Discount for employee: 450 * 0.3 = 135
        // Total non-grocery amount after discount: 450 - 135 = 315
        // Total grocery amount remains unchanged
        // Total discounted amount = 315 + 50 = 365
        // Exchange rate applied: 365 * 0.965172 = 352.28778
        assertEquals(352.28778, payableAmount, 0.01);
    }

    /**
     * Test case for verifying the payable amount calculation for an affiliate.
     * It tests the discount and exchange rate application for a user of type `AFFILIATE`.
     */
    @Test
    void testCalculatePayableAmountForAfflitiate() {
        // Arrange
        BillDetails billDetails = new BillDetails();
        billDetails.setUserType(UserType.AFFILIATE);
        billDetails.setTotalAmount(500);
        billDetails.setOriginalCurrency("USD");
        billDetails.setTargetCurrency("EUR");
        billDetails.setCustomerTenure(5);

        // Create list of items with grocery and non-grocery categories
        List<Item> items = new ArrayList<>();
        items.add(new Item("Apple", 50, CategoryType.GROCERY));
        items.add(new Item("Laptop", 450, CategoryType.ELECTRONICS));
        billDetails.setItems(items);

        // Mock the exchange rate API response
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 0.965172);
        mockResponse.put("rates", rates);

        when(restTemplate.getForObject(
                "https://open.er-api.com/v6/latest/USD?apikey=091e4fe22c2840fbb854ba4112f6f206",
                HashMap.class
        )).thenReturn((HashMap) mockResponse);

        // Act
        double payableAmount = calculationService.calculatePayableAmount(billDetails);

        // Assert
        // Discount for affiliate: 450 * 0.1 = 45
        // Total non-grocery amount after discount: 450 - 45 = 405
        // Total grocery amount remains unchanged
        // Total discounted amount = 405 + 50 = 455
        // Exchange rate applied: 455 * 0.965172 = 439.15326
        assertEquals(439.15326, payableAmount, 0.01);
    }
}
