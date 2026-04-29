package fruit.demo;

import fruit.demo.entity.DiscountedFruit;
import fruit.demo.entity.FruitEntity;
import fruit.demo.entity.ShoppingCartEntity;
import fruit.demo.rule.FullReductionRule;
import fruit.demo.rule.PricingRule;
import fruit.demo.service.CheckoutService;
import fruit.demo.service.FruitCatalog;
import fruit.demo.strategy.DiscountStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 面向对象设计的购物车测试
 * 展示扩展性：换水果打折、多水果打折、新增水果种类
 */
class OOCartTest {

    private ShoppingCartEntity cart;
    private CheckoutService checkoutService;
    private FruitEntity apple;
    private FruitEntity strawberry;
    private FruitEntity mango;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCartEntity();
        checkoutService = new CheckoutService();
        FruitCatalog catalog = FruitCatalog.getInstance();
        apple = catalog.getFruitEntity("苹果");
        strawberry = catalog.getFruitEntity("草莓");
        mango = catalog.getFruitEntity("芒果");
    }

    // ==================== 顾客A：苹果8元 + 草莓13元 ====================
    @Nested
    @DisplayName("顾客A - 苹果8元/斤 + 草莓13元/斤")
    class CustomerATest {

        @Test
        @DisplayName("正例：正常购买 2斤苹果 + 3斤草莓 = 55元")
        void positiveBasic() {
            cart.addFruitEntity(apple, 2);
            cart.addFruitEntity(strawberry, 3);
            assertEquals(new BigDecimal("55.00"), checkoutService.checkout(cart));
        }

        @Test
        @DisplayName("正例：只买苹果 5斤 = 40元")
        void positiveOnlyApple() {
            cart.addFruitEntity(apple, 5);
            assertEquals(new BigDecimal("40.00"), checkoutService.checkout(cart));
        }

        @Test
        @DisplayName("正例：只买草莓 4斤 = 52元")
        void positiveOnlyStrawberry() {
            cart.addFruitEntity(strawberry, 4);
            assertEquals(new BigDecimal("52.00"), checkoutService.checkout(cart));
        }

        @Test
        @DisplayName("边界值：斤数全为0，总价=0")
        void boundaryZeroWeight() {
            cart.addFruitEntity(apple, 0);
            cart.addFruitEntity(strawberry, 0);
            assertEquals(new BigDecimal("0.00"), checkoutService.checkout(cart));
        }

        @Test
        @DisplayName("边界值：各买1斤（最小正整数）= 21元")
        void boundaryMinPositive() {
            cart.addFruitEntity(apple, 1);
            cart.addFruitEntity(strawberry, 1);
            assertEquals(new BigDecimal("21.00"), checkoutService.checkout(cart));
        }

        @Test
        @DisplayName("反例：重量为负数，抛出异常")
        void negativeWeightThrowsException() {
            assertThrows(IllegalArgumentException.class, () -> {
                cart.addFruitEntity(apple, -1);
            });
        }
    }

    // ==================== 顾客B：苹果8元 + 草莓13元 + 芒果20元 ====================
    @Nested
    @DisplayName("顾客B - 苹果8元/斤 + 草莓13元/斤 + 芒果20元/斤")
    class CustomerBTest {

        @Test
        @DisplayName("正例：正常购买三种水果 = 75元")
        void positiveBasic() {
            cart.addFruitEntity(apple, 2);
            cart.addFruitEntity(strawberry, 3);
            cart.addFruitEntity(mango, 1);
            assertEquals(new BigDecimal("75.00"), checkoutService.checkout(cart));
        }

        @Test
        @DisplayName("边界值：斤数全为0，总价=0")
        void boundaryZeroWeight() {
            cart.addFruitEntity(apple, 0);
            cart.addFruitEntity(strawberry, 0);
            cart.addFruitEntity(mango, 0);
            assertEquals(new BigDecimal("0.00"), checkoutService.checkout(cart));
        }

        @Test
        @DisplayName("边界值：各买1斤 = 41元")
        void boundaryMinPositive() {
            cart.addFruitEntity(apple, 1);
            cart.addFruitEntity(strawberry, 1);
            cart.addFruitEntity(mango, 1);
            assertEquals(new BigDecimal("41.00"), checkoutService.checkout(cart));
        }

        @Test
        @DisplayName("反例：不买芒果，等价于顾客A")
        void negativeNoMango() {
            cart.addFruitEntity(apple, 2);
            cart.addFruitEntity(strawberry, 3);
            // 应与顾客A结果一致：55元
            ShoppingCartEntity cartA = new ShoppingCartEntity();
            cartA.addFruitEntity(apple, 2);
            cartA.addFruitEntity(strawberry, 3);
            assertEquals(checkoutService.checkout(cartA), checkoutService.checkout(cart));
        }
    }

    // ==================== 顾客C：草莓打8折 ====================
    @Nested
    @DisplayName("顾客C - 草莓打8折")
    class CustomerCTest {

        @Test
        @DisplayName("正例：草莓打8折 2苹果 + 3草莓(8折) + 1芒果 = 67.20")
        void positiveBasic() {
            cart.addFruitEntity(apple, 2);
            DiscountedFruit discountedStrawberry = new DiscountedFruit(
                    strawberry, DiscountStrategy.percentage(new BigDecimal("0.8"))
            );
            cart.addFruitEntity(discountedStrawberry, 3);
            cart.addFruitEntity(mango, 1);
            assertEquals(new BigDecimal("67.20"), checkoutService.checkout(cart));
        }

        @Test
        @DisplayName("正例：只买草莓(8折)，验证折扣生效 10斤 = 104元")
        void positiveOnlyDiscountedStrawberry() {
            DiscountedFruit discountedStrawberry = new DiscountedFruit(
                    strawberry, DiscountStrategy.percentage(new BigDecimal("0.8"))
            );
            cart.addFruitEntity(discountedStrawberry, 10);
            // 10 * 13 * 0.8 = 104
            assertEquals(new BigDecimal("104.00"), checkoutService.checkout(cart));
        }

        @Test
        @DisplayName("边界值：只买1斤草莓(8折) = 10.40")
        void boundaryMinDiscountAmount() {
            DiscountedFruit discountedStrawberry = new DiscountedFruit(
                    strawberry, DiscountStrategy.percentage(new BigDecimal("0.8"))
            );
            cart.addFruitEntity(discountedStrawberry, 1);
            assertEquals(new BigDecimal("10.40"), checkoutService.checkout(cart));
        }

        @Test
        @DisplayName("反例：不买草莓，折扣不影响结果")
        void negativeNoStrawberry() {
            cart.addFruitEntity(apple, 2);
            cart.addFruitEntity(mango, 1);
            // 无草莓：16 + 20 = 36
            assertEquals(new BigDecimal("36.00"), checkoutService.checkout(cart));
        }

        @Test
        @DisplayName("对比：C比B便宜，验证折扣确实生效")
        void compareDiscountDifference() {
            // B: 2苹果+3草莓+1芒果 = 75
            ShoppingCartEntity cartB = new ShoppingCartEntity();
            cartB.addFruitEntity(apple, 2);
            cartB.addFruitEntity(strawberry, 3);
            cartB.addFruitEntity(mango, 1);
            BigDecimal priceB = checkoutService.checkout(cartB);

            // C: 草莓打8折 = 67.20
            cart.addFruitEntity(apple, 2);
            DiscountedFruit discountedStrawberry = new DiscountedFruit(
                    strawberry, DiscountStrategy.percentage(new BigDecimal("0.8"))
            );
            cart.addFruitEntity(discountedStrawberry, 3);
            cart.addFruitEntity(mango, 1);
            BigDecimal priceC = checkoutService.checkout(cart);

            // 差额 = 3 * 13 * 0.2 = 7.8
            assertEquals(new BigDecimal("7.80"), priceB.subtract(priceC));
        }
    }

    // ==================== 顾客D：满100减10 ====================
    @Nested
    @DisplayName("顾客D - 满100减10")
    class CustomerDTest {

        @Test
        @DisplayName("正例：触发满减 总价145，减10 = 135")
        void positiveBasicWithReduction() {
            cart.addFruitEntity(apple, 5);
            cart.addFruitEntity(strawberry, 5);
            cart.addFruitEntity(mango, 2);
            PricingRule rule = new FullReductionRule(
                    new BigDecimal("100"), new BigDecimal("10")
            );
            assertEquals(new BigDecimal("135.00"), checkoutService.checkout(cart, rule));
        }

        @Test
        @DisplayName("正例：多次满减 总价310，减30 = 280")
        void positiveMultipleReduction() {
            cart.addFruitEntity(apple, 10);
            cart.addFruitEntity(strawberry, 10);
            cart.addFruitEntity(mango, 5);
            PricingRule rule = new FullReductionRule(
                    new BigDecimal("100"), new BigDecimal("10")
            );
            assertEquals(new BigDecimal("280.00"), checkoutService.checkout(cart, rule));
        }

        @Test
        @DisplayName("边界值：总价恰好=100，应减10 → 90")
        void boundaryExactly100() {
            // 用芒果凑：5斤 * 20元 = 100元
            cart.addFruitEntity(mango, 5);
            PricingRule rule = new FullReductionRule(
                    new BigDecimal("100"), new BigDecimal("10")
            );
            assertEquals(new BigDecimal("90.00"), checkoutService.checkout(cart, rule));
        }

        @Test
        @DisplayName("边界值：总价=100.01，超过阈值，应减10 → 90.01")
        void boundaryJustOver100() {
            // 添加0.01元/斤的水果，买10001斤 = 100.01元
            FruitCatalog.getInstance().addFruitEntity("边界测试果", new BigDecimal("0.01"));
            FruitEntity testFruit = FruitCatalog.getInstance().getFruitEntity("边界测试果");
            cart.addFruitEntity(testFruit, 10001);
            PricingRule rule = new FullReductionRule(
                    new BigDecimal("100"), new BigDecimal("10")
            );
            assertEquals(new BigDecimal("90.01"), checkoutService.checkout(cart, rule));
        }

        @Test
        @DisplayName("反例：总价=99.99，不满100，不减 → 99.99")
        void negativeJustUnder100() {
            // 添加0.01元/斤的水果，买9999斤 = 99.99元
            FruitCatalog.getInstance().addFruitEntity("边界测试果", new BigDecimal("0.01"));
            FruitEntity testFruit = FruitCatalog.getInstance().getFruitEntity("边界测试果");
            cart.addFruitEntity(testFruit, 9999);
            PricingRule rule = new FullReductionRule(
                    new BigDecimal("100"), new BigDecimal("10")
            );
            assertEquals(new BigDecimal("99.99"), checkoutService.checkout(cart, rule));
        }

        @Test
        @DisplayName("边界值：总价=200，减2次 → 180")
        void boundaryExactly200() {
            // 10斤芒果 = 200元
            cart.addFruitEntity(mango, 10);
            PricingRule rule = new FullReductionRule(
                    new BigDecimal("100"), new BigDecimal("10")
            );
            assertEquals(new BigDecimal("180.00"), checkoutService.checkout(cart, rule));
        }

        @Test
        @DisplayName("边界值：总价=199.99，只减1次 → 189.99")
        void boundaryJustUnder200() {
            FruitCatalog.getInstance().addFruitEntity("边界测试果", new BigDecimal("0.01"));
            FruitEntity testFruit = FruitCatalog.getInstance().getFruitEntity("边界测试果");
            cart.addFruitEntity(testFruit, 19999);
            PricingRule rule = new FullReductionRule(
                    new BigDecimal("100"), new BigDecimal("10")
            );
            assertEquals(new BigDecimal("189.99"), checkoutService.checkout(cart, rule));
        }

        @Test
        @DisplayName("反例：总价<100，不触发满减 → 75元")
        void negativeUnderThreshold() {
            cart.addFruitEntity(apple, 2);
            cart.addFruitEntity(strawberry, 3);
            cart.addFruitEntity(mango, 1);
            // 16+39+20=75 < 100
            PricingRule rule = new FullReductionRule(
                    new BigDecimal("100"), new BigDecimal("10")
            );
            assertEquals(new BigDecimal("75.00"), checkoutService.checkout(cart, rule));
        }
    }

    // ==================== 扩展性测试 ====================
    @Nested
    @DisplayName("扩展性测试")
    class ExtensibilityTest {

        @Test
        @DisplayName("新增水果种类：西瓜15元/斤")
        void newFruitType() {
            FruitCatalog.getInstance().addFruitEntity("西瓜", new BigDecimal("15"));
            FruitEntity watermelon = FruitCatalog.getInstance().getFruitEntity("西瓜");
            cart.addFruitEntity(watermelon, 3);
            assertEquals(new BigDecimal("45.00"), checkoutService.checkout(cart));
        }

        @Test
        @DisplayName("自定义满减规则：满200减30")
        void customFullReduction() {
            cart.addFruitEntity(apple, 10);
            cart.addFruitEntity(mango, 10);
            PricingRule rule = new FullReductionRule(
                    new BigDecimal("200"), new BigDecimal("30")
            );
            assertEquals(new BigDecimal("250.00"), checkoutService.checkout(cart, rule));
        }

        @Test
        @DisplayName("组合规则：折扣 + 满减")
        void combinedRules() {
            DiscountedFruit discountedStrawberry = new DiscountedFruit(
                    strawberry, DiscountStrategy.percentage(new BigDecimal("0.8"))
            );
            cart.addFruitEntity(apple, 5);
            cart.addFruitEntity(discountedStrawberry, 5);
            cart.addFruitEntity(mango, 2);
            PricingRule rule = new FullReductionRule(
                    new BigDecimal("100"), new BigDecimal("10")
            );
            assertEquals(new BigDecimal("122.00"), checkoutService.checkout(cart, rule));
        }
    }
}
