package nl.tudelft.sem.template.order.unit;

import commons.Coupon;
import nl.tudelft.sem.template.order.models.RemoveCouponRequestModel;
import nl.tudelft.sem.template.order.services.RestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RestServiceTest {
    private RestService restService;

    @Autowired
    public RestServiceTest(RestTemplateBuilder restTemplateBuilder) {
        this.restService = new RestService(restTemplateBuilder);
    }

    @BeforeEach
    public void setup() {
    }

    @Test
    public void unlimitedCouponTest() {
        Coupon coupon = new Coupon();
        String token = "Token";

        ResponseEntity<String> res = restService.removeCoupon(new RemoveCouponRequestModel(coupon), token);
        HttpStatus h = res.getStatusCode();
        assertThat(h).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void limitedCouponTest() {
        Coupon coupon = new Coupon("CODE", true);
        String token = "Token";

        ResponseEntity<String> res = restService.removeCoupon(new RemoveCouponRequestModel(coupon), token);
        HttpStatus h = res.getStatusCode();
        assertThat(h).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void limitedCouponTestWithMock() {
        RestTemplateBuilder restTemplateBuilder = mock(RestTemplateBuilder.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        RestService rest = new RestService(restTemplateBuilder);
        Coupon coupon = new Coupon("CODE", true);
        String token = "Token";

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                any(Class.class))).thenReturn(ResponseEntity.ok(""));
        ResponseEntity<String> res = rest.removeCoupon(new RemoveCouponRequestModel(coupon), token);
        String h = res.getBody();
        assertThat(h).isEqualTo("");
    }

    @Test
    public void getWrongBasketTest() {
        assertThat(restService.getBasket("Token").getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getMockBasketTest() {
        RestTemplateBuilder restTemplateBuilder = mock(RestTemplateBuilder.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        RestService rest = new RestService(restTemplateBuilder);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class),
                any(Class.class))).thenReturn(ResponseEntity.ok(""));

        assertThat(rest.getBasket("Token").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
