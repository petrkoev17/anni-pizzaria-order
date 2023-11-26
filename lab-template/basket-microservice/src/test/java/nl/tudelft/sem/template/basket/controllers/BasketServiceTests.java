package nl.tudelft.sem.template.basket.controllers;

import commons.*;
import nl.tudelft.sem.template.basket.services.BasketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BasketServiceTests {

    private BasketService basketService;
    private Pizza pizza1;
    private Pizza pizza2;

    @BeforeEach
    public void setup() {
        Ingredient ingredient1 = new Ingredient("ingredient1", 5.00);
        Ingredient ingredient2 = new Ingredient("ingredient1", 10.00);
        Ingredient ingredient3 = new Ingredient("ingredient1", 2.50);
        List<Ingredient> ingredientsPizza1 = new ArrayList<>(List.of(ingredient1, ingredient2));
        pizza1 = new Pizza("testPizza1", ingredientsPizza1);
        List<Ingredient> ingredientsPizza2 = new ArrayList<>(List.of(ingredient1, ingredient3));
        pizza2 = new Pizza("testPizza2", ingredientsPizza2);

        basketService = new BasketService();
    }

    @Test
    public void removeBasketTest() {
        // arrange
        basketService.createBasket("testCustomer");

        // act
        basketService.removeBasket("testCustomer");

        // assert
        assertThat(basketService.getBasket("testCustomer")).isNull();
    }

    @Test
    public void addPizzaToBasketTest() {
        // arrange
        basketService.createBasket("testCustomer");

        // act
        basketService.addPizzaToBasket("testCustomer", pizza1);

        // assert pizza was added
        List<Pizza> pizzasInBasket = basketService.getBasket("testCustomer").getBasketInfo().getPizzas();
        assertThat(pizzasInBasket).containsExactly(pizza1);

        // assert price was updated
        double priceOfBasket = basketService.getBasket("testCustomer").getBasketInfo().getPrice();
        assertThat(priceOfBasket).isEqualTo(21.00);
    }

    @Test
    public void removePizzaFromBasketTest() {
        // arrange
        basketService.createBasket("testCustomer");
        basketService.addPizzaToBasket("testCustomer", pizza1);

        // act
        basketService.removePizzaFromBasket("testCustomer", "testPizza1");

        // assert pizza has been removed
        List<Pizza> pizzasInBasket = basketService.getBasket("testCustomer").getBasketInfo().getPizzas();
        assertThat(pizzasInBasket).hasSize(0);

        // assert price was updated
        double priceOfBasket = basketService.getBasket("testCustomer").getBasketInfo().getPrice();
        assertThat(priceOfBasket).isEqualTo(3.00);
    }

    @Test
    public void applyCouponNoPriorCouponTest() {
        // arrange
        basketService.createBasket("testCustomer");
        basketService.addPizzaToBasket("testCustomer", pizza1);
        basketService.addPizzaToBasket("testCustomer", pizza2);

        Coupon coupon = new Coupon("coup10", 'D', 50.0, false);

        // act
        basketService.applyCouponToBasket("testCustomer", coupon);

        // assert coupon is applied
        Coupon couponInBasket = basketService.getBasket("testCustomer").getBasketInfo().getCoupon();
        assertThat(couponInBasket).isEqualTo(coupon);

        // assert price is updated
        double priceOfBasket = basketService.getBasket("testCustomer").getBasketInfo().getPrice();
        assertThat(priceOfBasket).isEqualTo(17.25);
    }

    @Test
    public void applyCheaperCouponTest() {
        // arrange
        basketService.createBasket("testCustomer");
        basketService.addPizzaToBasket("testCustomer", pizza1);
        basketService.addPizzaToBasket("testCustomer", pizza2);

        Coupon expensiveCoupon = new Coupon("coup11", 'D', 25.0, false);
        Coupon cheaperCoupon = new Coupon("coup10", 'D', 50.0, false);
        basketService.applyCouponToBasket("testCustomer", expensiveCoupon);

        // act
        basketService.applyCouponToBasket("testCustomer", cheaperCoupon);

        // assert correct coupon is set
        Coupon currentCouponApplied = basketService.getBasket("testCustomer").getBasketInfo().getCoupon();
        assertThat(currentCouponApplied).isEqualTo(cheaperCoupon);

        // assert price is correct
        double priceOfBasket = basketService.getBasket("testCustomer").getBasketInfo().getPrice();
        assertThat(priceOfBasket).isEqualTo(17.25);
    }

    @Test
    public void applyMoreExpensiveCouponTest() {
        // arrange
        basketService.createBasket("testCustomer");
        basketService.addPizzaToBasket("testCustomer", pizza1);
        basketService.addPizzaToBasket("testCustomer", pizza2);

        Coupon expensiveCoupon = new Coupon("coup11", 'D', 25.0, false);
        Coupon cheaperCoupon = new Coupon("coup10", 'D', 50.0, false);
        basketService.applyCouponToBasket("testCustomer", cheaperCoupon);

        // act
        basketService.applyCouponToBasket("testCustomer", expensiveCoupon);

        // assert correct coupon is set
        Coupon currentCouponApplied = basketService.getBasket("testCustomer").getBasketInfo().getCoupon();
        assertThat(currentCouponApplied).isEqualTo(cheaperCoupon);

        // assert price is correct
        double priceOfBasket = basketService.getBasket("testCustomer").getBasketInfo().getPrice();
        assertThat(priceOfBasket).isEqualTo(17.25);
    }

    @Test
    public void removeCouponTest() {
        // arrange
        basketService.createBasket("testCustomer");
        basketService.addPizzaToBasket("testCustomer", pizza1);
        basketService.addPizzaToBasket("testCustomer", pizza2);

        Coupon coupon = new Coupon("coup11", 'D', 25.0, false);
        basketService.applyCouponToBasket("testCustomer", coupon);

        // act
        basketService.removeCouponFromBasket("testCustomer");

        // assert correct coupon is set
        Coupon currentCouponApplied = basketService.getBasket("testCustomer").getBasketInfo().getCoupon();
        assertThat(currentCouponApplied).isNull();

        // assert price is correct
        double priceOfBasket = basketService.getBasket("testCustomer").getBasketInfo().getPrice();
        assertThat(priceOfBasket).isEqualTo(31.50);
    }

    @Test
    public void setStorePreferenceTest() {
        // arrange
        basketService.createBasket("testCustomer");

        // act
        basketService.setStorePreference("testCustomer", 2);

        // assert
        int storePreferenceInBasket = basketService.getBasket("testCustomer").getBasketInfo().getStoreId();
        assertThat(storePreferenceInBasket).isEqualTo(2);
    }
}
