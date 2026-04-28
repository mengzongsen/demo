package fruit.demo;

import fruit.demo.entity.FruitEntity;
import fruit.demo.service.IFruitService;
import fruit.demo.service.impl.FruitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fruit.demo.service.impl.FruitServiceImpl.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FruitServiceTest {

    private IFruitService fruitService;

    @BeforeEach
    void setUp() {
        fruitService = new FruitServiceImpl();
    }

    // ==================== 顾客A：苹果8元 + 草莓13元 ====================
    @Nested
    @DisplayName("顾客A - 苹果8元/斤 + 草莓13元/斤")
    class CustomerATest {

        @Test
        @DisplayName("正例：正常购买")
        void positiveBasic() {
            // 2斤苹果 + 3斤草莓 = 16 + 39 = 55
            assertEquals(new BigDecimal("55.00"), fruitService.buyAppleAndStrawberry(2, 3));
        }

        @Test
        @DisplayName("正例：只买苹果")
        void positiveOnlyApple() {
            // 5斤苹果 = 5 * 8 = 40
            assertEquals(new BigDecimal("40.00"), fruitService.buyAppleAndStrawberry(5, 0));
        }

        @Test
        @DisplayName("正例：只买草莓")
        void positiveOnlyStrawberry() {
            // 4斤草莓 = 4 * 13 = 52
            assertEquals(new BigDecimal("52.00"), fruitService.buyAppleAndStrawberry(0, 4));
        }

        @Test
        @DisplayName("边界值：斤数全为0")
        void boundaryZeroWeight() {
            assertEquals(new BigDecimal("0.00"), fruitService.buyAppleAndStrawberry(0, 0));
        }

        @Test
        @DisplayName("边界值：各买1斤（最小正整数）")
        void boundaryMinPositive() {
            // 8 + 13 = 21
            assertEquals(new BigDecimal("21.00"), fruitService.buyAppleAndStrawberry(1, 1));
        }

        @Test
        @DisplayName("边界值：大数量购买")
        void boundaryLargeWeight() {
            // 1000 * 8 + 1000 * 13 = 21000
            assertEquals(new BigDecimal("21000.00"), fruitService.buyAppleAndStrawberry(1000, 1000));
        }
    }

    // ==================== 顾客B：苹果8元 + 草莓13元 + 芒果20元 ====================
    @Nested
    @DisplayName("顾客B - 苹果8元/斤 + 草莓13元/斤 + 芒果20元/斤")
    class CustomerBTest {

        @Test
        @DisplayName("正例：正常购买三种水果")
        void positiveBasic() {
            // 2 * 8 + 3 * 13 + 20 = 16 + 39 + 20 = 75
            assertEquals(new BigDecimal("75.00"), fruitService.buyAppleStrawberryAndMango(2, 3, 1));
        }

        @Test
        @DisplayName("正例：只买芒果")
        void positiveOnlyMango() {
            // 20 * 3 = 60
            assertEquals(new BigDecimal("60.00"), fruitService.buyAppleStrawberryAndMango(0, 0, 3));
        }

        @Test
        @DisplayName("边界值：斤数全为0")
        void boundaryZeroWeight() {
            assertEquals(new BigDecimal("0.00"), fruitService.buyAppleStrawberryAndMango(0, 0, 0));
        }

        @Test
        @DisplayName("边界值：各买1斤（最小正整数）")
        void boundaryMinPositive() {
            // 8 + 13 + 20 = 41
            assertEquals(new BigDecimal("41.00"), fruitService.buyAppleStrawberryAndMango(1, 1, 1));
        }

        @Test
        @DisplayName("反例：芒果斤数为0，等同于只有苹果草莓")
        void negativeMangoZero() {
            // 与 buyAppleAndStrawberry(2,3) 结果一致
            assertEquals(fruitService.buyAppleAndStrawberry(2, 3), fruitService.buyAppleStrawberryAndMango(2, 3, 0));
        }
    }

    // ==================== 顾客C：苹果8元 + 草莓13元 + 芒果20元，草莓打8折 ====================
    @Nested
    @DisplayName("顾客C - 苹果8元 + 草莓13元 + 芒果20元，草莓打8折")
    class CustomerCTest {

        @Test
        @DisplayName("正例：正常购买，草莓8折")
        void positiveBasic() {
            // 2 * 8 + 3 * 13 * 0.8 + 20 = 16 + 31.2 + 20 = 67.2
            assertEquals(new BigDecimal("67.20"), fruitService.buyAppleStrawberryAndMangoWithDiscount(2, 3, 1));
        }

        @Test
        @DisplayName("正例：只买草莓，验证折扣生效")
        void positiveStrawberryDiscount() {
            // 10 * 13 * 0.8 = 104
            assertEquals(new BigDecimal("104.00"), fruitService.buyAppleStrawberryAndMangoWithDiscount(0, 10, 0));
        }

        @Test
        @DisplayName("反例：不买草莓时折扣不影响结果")
        void negativeNoStrawberry() {
            // 只买苹果和芒果，折扣无影响：2 * 8 + 20 = 36
            assertEquals(new BigDecimal("36.00"), fruitService.buyAppleStrawberryAndMangoWithDiscount(2, 0, 1));
        }

        @Test
        @DisplayName("边界值：斤数全为0")
        void boundaryZeroWeight() {
            assertEquals(new BigDecimal("0.00"), fruitService.buyAppleStrawberryAndMangoWithDiscount(0, 0, 0));
        }

        @Test
        @DisplayName("边界值：只买1斤草莓（最小折扣金额）")
        void boundaryMinDiscountAmount() {
            // 13 * 0.8 = 10.4
            assertEquals(new BigDecimal("10.40"), fruitService.buyAppleStrawberryAndMangoWithDiscount(0, 1, 0));
        }

        @Test
        @DisplayName("对比：C比B便宜，验证折扣确实生效")
        void compareDiscountDifference() {
            BigDecimal priceB = fruitService.buyAppleStrawberryAndMango(2, 3, 1);
            BigDecimal priceC = fruitService.buyAppleStrawberryAndMangoWithDiscount(2, 3, 1);
            // 折扣差额 = 3 * 13 * 0.2 = 7.8
            assertEquals(new BigDecimal("7.80"), priceB.subtract(priceC));
        }
    }

    // ==================== 顾客D：苹果8元 + 草莓13元 + 芒果20元，满100减10 ====================
    @Nested
    @DisplayName("顾客D - 苹果8元 + 草莓13元 + 芒果20元，满100减10")
    class CustomerDTest {

        @Test
        @DisplayName("正例：正常购买，触发满减")
        void positiveBasicWithReduction() {
            // 5 * 8 + 5 * 13 + 2 * 20 = 40 + 65 + 40 = 145，满100减10 = 135
            assertEquals(new BigDecimal("135.00"), fruitService.buyAppleStrawberryAndMangoWithFullReduction(5, 5, 2));
        }

        @Test
        @DisplayName("正例：多次满减")
        void positiveMultipleReduction() {
            // 10 * 8 + 10 * 13 + 5 * 20 = 80 + 130 + 100 = 310，满300减30 = 280
            assertEquals(new BigDecimal("280.00"), fruitService.buyAppleStrawberryAndMangoWithFullReduction(10, 10, 5));
        }

        @Test
        @DisplayName("边界值：总价恰好在阈值上 = 100.00，应减10")
        void boundaryExactly100() {
            // 用通用方法精确凑出100元：5斤芒果=100
            List<FruitEntity> fruits = List.of(
                    new FruitEntity(MANGO, new BigDecimal("20"), BigDecimal.ONE)
            );
            Map<String, Integer> weightMap = Map.of(MANGO, 5);
            BigDecimal result = fruitService.calculate(fruits, weightMap, new BigDecimal("100"), new BigDecimal("10"));
            assertEquals(new BigDecimal("90.00"), result);
        }

        @Test
        @DisplayName("边界值：总价 = 100.01，应减10（超过阈值）")
        void boundaryJustOver100() {
            // 凑出100.01：需通过折扣产生小数
            // 自定义水果单价 0.01元，买10001斤 = 100.01
            List<FruitEntity> fruits = List.of(
                    new FruitEntity("测试水果", new BigDecimal("0.01"), BigDecimal.ONE)
            );
            Map<String, Integer> weightMap = Map.of("测试水果", 10001);
            BigDecimal result = fruitService.calculate(fruits, weightMap, new BigDecimal("100"), new BigDecimal("10"));
            // 100.01 / 100 = 1次满减，100.01 - 10 = 90.01
            assertEquals(new BigDecimal("90.01"), result);
        }

        @Test
        @DisplayName("边界值：总价 = 99.99，不满100，不减")
        void boundaryJustUnder100() {
            // 凑出99.99：自定义水果0.01元，买9999斤 = 99.99
            List<FruitEntity> fruits = List.of(
                    new FruitEntity("测试水果", new BigDecimal("0.01"), BigDecimal.ONE)
            );
            Map<String, Integer> weightMap = Map.of("测试水果", 9999);
            BigDecimal result = fruitService.calculate(fruits, weightMap, new BigDecimal("100"), new BigDecimal("10"));
            // 99.99 < 100，不触发满减
            assertEquals(new BigDecimal("99.99"), result);
        }

        @Test
        @DisplayName("反例：总价 < 100，不触发满减")
        void negativeUnderThreshold() {
            // 2 * 8 + 3 * 13 + 20 = 16 + 39 + 20 = 75 < 100
            assertEquals(new BigDecimal("75.00"), fruitService.buyAppleStrawberryAndMangoWithFullReduction(2, 3, 1));
        }

        @Test
        @DisplayName("边界值：斤数全为0，总价为0")
        void boundaryZeroWeight() {
            assertEquals(new BigDecimal("0.00"), fruitService.buyAppleStrawberryAndMangoWithFullReduction(0, 0, 0));
        }

        @Test
        @DisplayName("边界值：总价 = 200.00，减2次 = 180")
        void boundaryExactly200() {
            // 10斤芒果 = 200
            List<FruitEntity> fruits = List.of(
                    new FruitEntity(MANGO, new BigDecimal("20"), BigDecimal.ONE)
            );
            Map<String, Integer> weightMap = Map.of(MANGO, 10);
            BigDecimal result = fruitService.calculate(fruits, weightMap, new BigDecimal("100"), new BigDecimal("10"));
            assertEquals(new BigDecimal("180.00"), result);
        }

        @Test
        @DisplayName("边界值：总价 = 199.99，只减1次 = 189.99")
        void boundaryJustUnder200() {
            List<FruitEntity> fruits = List.of(
                    new FruitEntity("测试水果", new BigDecimal("0.01"), BigDecimal.ONE)
            );
            Map<String, Integer> weightMap = Map.of("测试水果", 19999);
            BigDecimal result = fruitService.calculate(fruits, weightMap, new BigDecimal("100"), new BigDecimal("10"));
            assertEquals(new BigDecimal("189.99"), result);
        }
    }

    // ==================== 通用方法拓展性测试 ====================
    @Nested
    @DisplayName("通用方法 calculate 拓展性")
    class CalculateTest {

        @Test
        @DisplayName("拓展性：新增水果种类")
        void newFruitType() {
            List<FruitEntity> fruits = List.of(
                    new FruitEntity("西瓜", new BigDecimal("15"), BigDecimal.ONE)
            );
            Map<String, Integer> weightMap = Map.of("西瓜", 3);
            assertEquals(new BigDecimal("45.00"), fruitService.calculate(fruits, weightMap, null, null));
        }

        @Test
        @DisplayName("拓展性：多个水果同时打折")
        void multipleDiscounts() {
            List<FruitEntity> fruits = List.of(
                    new FruitEntity(APPLE, new BigDecimal("8"), BigDecimal.ONE),
                    new FruitEntity(STRAWBERRY, new BigDecimal("13"), new BigDecimal("0.8")),
                    new FruitEntity(MANGO, new BigDecimal("20"), new BigDecimal("0.9"))
            );
            Map<String, Integer> weightMap = new HashMap<>();
            weightMap.put(APPLE, 2);
            weightMap.put(STRAWBERRY, 3);
            weightMap.put(MANGO, 1);
            // 2*8 + 3*13*0.8 + 1*20*0.9 = 16 + 31.2 + 18 = 65.2
            assertEquals(new BigDecimal("65.20"), fruitService.calculate(fruits, weightMap, null, null));
        }

        @Test
        @DisplayName("拓展性：自定义满减规则（满200减30）")
        void customFullReduction() {
            List<FruitEntity> fruits = List.of(
                    new FruitEntity(APPLE, new BigDecimal("8"), BigDecimal.ONE),
                    new FruitEntity(MANGO, new BigDecimal("20"), BigDecimal.ONE)
            );
            Map<String, Integer> weightMap = Map.of(APPLE, 10, MANGO, 10);
            // 10 * 8 + 10 * 20 = 280，满200减30 = 250
            assertEquals(new BigDecimal("250.00"),
                    fruitService.calculate(fruits, weightMap, new BigDecimal("200"), new BigDecimal("30")));
        }

        @Test
        @DisplayName("拓展性：满减参数为null，不触发满减")
        void nullFullReduction() {
            List<FruitEntity> fruits = List.of(
                    new FruitEntity(APPLE, new BigDecimal("8"), BigDecimal.ONE)
            );
            Map<String, Integer> weightMap = Map.of(APPLE, 5);
            assertEquals(new BigDecimal("40.00"), fruitService.calculate(fruits, weightMap, null, null));
        }

        @Test
        @DisplayName("拓展性：满减阈值为0，不触发满减")
        void zeroThresholdNoReduction() {
            List<FruitEntity> fruits = List.of(
                    new FruitEntity(APPLE, new BigDecimal("8"), BigDecimal.ONE)
            );
            Map<String, Integer> weightMap = Map.of(APPLE, 20);
            // 20 * 8 = 160，但阈值=0，不触发满减
            assertEquals(new BigDecimal("160.00"),
                    fruitService.calculate(fruits, weightMap, BigDecimal.ZERO, new BigDecimal("10")));
        }

        @Test
        @DisplayName("拓展性：weightMap中包含fruits里没有的水果，按0斤处理")
        void extraWeightIgnored() {
            List<FruitEntity> fruits = List.of(
                    new FruitEntity(APPLE, new BigDecimal("8"), BigDecimal.ONE)
            );
            Map<String, Integer> weightMap = new HashMap<>();
            weightMap.put(APPLE, 2);
            weightMap.put("不存在的水果", 10); // 列表中没有，不影响计算
            // 只算苹果：2 * 8 = 16
            assertEquals(new BigDecimal("16.00"), fruitService.calculate(fruits, weightMap, null, null));
        }

        @Test
        @DisplayName("拓展性：折扣为1（原价），等价于不打折")
        void discountOneEqualsNoDiscount() {
            List<FruitEntity> fruits = List.of(
                    new FruitEntity(STRAWBERRY, new BigDecimal("13"), BigDecimal.ONE)
            );
            Map<String, Integer> weightMap = Map.of(STRAWBERRY, 5);
            // 5 * 13 = 65
            assertEquals(new BigDecimal("65.00"), fruitService.calculate(fruits, weightMap, null, null));
        }

        @Test
        @DisplayName("拓展性：折扣为0.5（半价）")
        void halfDiscount() {
            List<FruitEntity> fruits = List.of(
                    new FruitEntity(APPLE, new BigDecimal("8"), new BigDecimal("0.5"))
            );
            Map<String, Integer> weightMap = Map.of(APPLE, 4);
            // 4 * 8 * 0.5 = 16
            assertEquals(new BigDecimal("16.00"), fruitService.calculate(fruits, weightMap, null, null));
        }
    }
}
