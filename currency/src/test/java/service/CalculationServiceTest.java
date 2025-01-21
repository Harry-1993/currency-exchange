package service;

import com.example.currency.enums.UserType;
import com.example.currency.model.BillDetails;
import com.example.currency.service.CalculationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CalculationServiceTest {

    @InjectMocks
    private CalculationService calculationService;

    @Mock
    private RestTemplate restTemplate;

    public CalculationServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    //@Test
    void testCalculatePayableAmount() {
        BillDetails billDetails = new BillDetails();
        billDetails.setUserType(UserType.EMPLOYEE);
        billDetails.setTotalAmount(200);
        billDetails.setOriginalCurrency("USD");
        billDetails.setTargetCurrency("EUR");

        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 0.85);
        mockResponse.put("rates", rates);

        when(restTemplate.getForObject("https://open.er-api.com/v6/latest/USD?apikey=091e4fe22c2840fbb854ba4112f6f206", HashMap.class))
                .thenReturn((HashMap) mockResponse);

        double payableAmount = calculationService.calculatePayableAmount(billDetails);
        assertEquals(119.0, payableAmount, 0.01);
    }
}
