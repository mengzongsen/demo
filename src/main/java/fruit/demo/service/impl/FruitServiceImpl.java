package fruit.demo.service.impl;

import fruit.demo.entity.FruitEntity;
import fruit.demo.service.IFruitService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 水果服务实现类
 */
@Service
public class FruitServiceImpl implements IFruitService {

    // 水果名称常量
    public static final String APPLE = "苹果";
    public static final String STRAWBERRY = "草莓";
    public static final String MANGO = "芒果";

    // 价格常量
    private static final BigDecimal APPLE_PRICE = new BigDecimal("8"); // 苹果
    private static final BigDecimal STRAWBERRY_PRICE = new BigDecimal("13"); // 草莓
    private static final BigDecimal MANGO_PRICE = new BigDecimal("20"); // 芒果

    // 折扣常量
    private static final BigDecimal NO_DISCOUNT = BigDecimal.ONE; // 原价
    private static final BigDecimal STRAWBERRY_DISCOUNT = new BigDecimal("0.8"); // 八折

    // 满减常量
    private static final BigDecimal FULL_REDUCTION_THRESHOLD = new BigDecimal("100"); // 一百元
    private static final BigDecimal FULL_REDUCTION_AMOUNT = new BigDecimal("10"); // 10元

    /**
     * 通用计算方法：支持任意水果、任意折扣、满减规则
     *
     * @param fruits                 水果实体列表（包含名称、单价、折扣）
     * @param weightMap              水果名称 -> 斤数（>=0的整数）
     * @param fullReductionThreshold 满减阈值（null或0表示无满减）
     * @param fullReductionAmount    满减金额（null或0表示无满减）
     * @return 总价
     */
    @Override
    public BigDecimal calculate(List<FruitEntity> fruits, Map<String, Integer> weightMap, BigDecimal fullReductionThreshold, BigDecimal fullReductionAmount) {
        BigDecimal total = calculateTotal(fruits, weightMap);
        if (fullReductionThreshold != null && fullReductionAmount != null && fullReductionThreshold.compareTo(BigDecimal.ZERO) > 0) {
            total = applyFullReduction(total, fullReductionThreshold, fullReductionAmount);
        }
        return total;
    }

    /**
     * 顾客A：购买苹果和草莓，计算总价
     * 苹果 8 元/斤，草莓 13 元/斤
     *
     * @param appleWeight      苹果斤数（>=0的整数）
     * @param strawberryWeight 草莓斤数（>=0的整数）
     * @return 总价
     */
    @Override
    public BigDecimal buyAppleAndStrawberry(int appleWeight, int strawberryWeight) {
        // 构建水果实体列表（包含名称、单价、折扣）
        List<FruitEntity> fruits = List.of(
                new FruitEntity(APPLE, APPLE_PRICE, NO_DISCOUNT),
                new FruitEntity(STRAWBERRY, STRAWBERRY_PRICE, NO_DISCOUNT)
        );
        // 构建水果重量映射（水果名称 -> 斤数）
        HashMap<String, Integer> weightMap = new HashMap<>();
        weightMap.put(APPLE, appleWeight);
        weightMap.put(STRAWBERRY, strawberryWeight);
        return calculate(fruits, weightMap, null, null);
    }

    /**
     * 顾客B：购买苹果、草莓和芒果，计算总价
     * 苹果 8 元/斤，草莓 13 元/斤，芒果 20 元/斤
     *
     * @param appleWeight      苹果斤数（>=0的整数）
     * @param strawberryWeight 草莓斤数（>=0的整数）
     * @param mangoWeight      芒果斤数（>=0的整数）
     * @return 总价
     */
    @Override
    public BigDecimal buyAppleStrawberryAndMango(int appleWeight, int strawberryWeight, int mangoWeight) {
        // 构建水果实体列表（包含名称、单价、折扣）
        List<FruitEntity> fruits = List.of(
                new FruitEntity(APPLE, APPLE_PRICE, NO_DISCOUNT),
                new FruitEntity(STRAWBERRY, STRAWBERRY_PRICE, NO_DISCOUNT),
                new FruitEntity(MANGO, MANGO_PRICE, NO_DISCOUNT)
        );
        // 构建水果重量映射（水果名称 -> 斤数）
        HashMap<String, Integer> weightMap = new HashMap<>();
        weightMap.put(APPLE, appleWeight);
        weightMap.put(STRAWBERRY, strawberryWeight);
        weightMap.put(MANGO, mangoWeight);
        return calculate(fruits, weightMap, null, null);
    }

    /**
     * 顾客C：购买苹果、草莓和芒果，草莓打8折，计算总价
     * 苹果 8 元/斤，草莓 13 元/斤（8折），芒果 20 元/斤
     *
     * @param appleWeight      苹果斤数（>=0的整数）
     * @param strawberryWeight 草莓斤数（>=0的整数）
     * @param mangoWeight      芒果斤数（>=0的整数）
     * @return 总价
     */
    @Override
    public BigDecimal buyAppleStrawberryAndMangoWithDiscount(int appleWeight, int strawberryWeight, int mangoWeight) {
        // 构建水果实体列表（包含名称、单价、折扣）
        List<FruitEntity> fruits = List.of(
                new FruitEntity(APPLE, APPLE_PRICE, NO_DISCOUNT),
                new FruitEntity(STRAWBERRY, STRAWBERRY_PRICE, STRAWBERRY_DISCOUNT),
                new FruitEntity(MANGO, MANGO_PRICE, NO_DISCOUNT)
        );
        // 构建水果重量映射（水果名称 -> 斤数）
        HashMap<String, Integer> weightMap = new HashMap<>();
        weightMap.put(APPLE, appleWeight);
        weightMap.put(STRAWBERRY, strawberryWeight);
        weightMap.put(MANGO, mangoWeight);
        return calculate(fruits, weightMap, null, null);
    }

    /**
     * 顾客D：购买苹果、草莓和芒果，满100减10，计算总价
     * 苹果 8 元/斤，草莓 13 元/斤，芒果 20 元/斤，满100减10
     *
     * @param appleWeight      苹果斤数（>=0的整数）
     * @param strawberryWeight 草莓斤数（>=0的整数）
     * @param mangoWeight      芒果斤数（>=0的整数）
     * @return 总价
     */
    @Override
    public BigDecimal buyAppleStrawberryAndMangoWithFullReduction(int appleWeight, int strawberryWeight, int mangoWeight) {
        // 构建水果实体列表（包含名称、单价、折扣）
        List<FruitEntity> fruits = List.of(
                new FruitEntity(APPLE, APPLE_PRICE, NO_DISCOUNT),
                new FruitEntity(STRAWBERRY, STRAWBERRY_PRICE, NO_DISCOUNT),
                new FruitEntity(MANGO, MANGO_PRICE, NO_DISCOUNT)
        );
        // 构建水果重量映射（水果名称 -> 斤数）
        HashMap<String, Integer> weightMap = new HashMap<>();
        weightMap.put(APPLE, appleWeight);
        weightMap.put(STRAWBERRY, strawberryWeight);
        weightMap.put(MANGO, mangoWeight);
        return calculate(fruits, weightMap, FULL_REDUCTION_THRESHOLD, FULL_REDUCTION_AMOUNT);
    }

    /**
     * 计算水果总价（含折扣）
     *
     * @param fruits    水果实体列表（包含名称、单价、折扣）
     * @param weightMap 水果名称 -> 斤数
     * @return 总价
     */
    private BigDecimal calculateTotal(List<FruitEntity> fruits, Map<String, Integer> weightMap) {
        BigDecimal total = BigDecimal.ZERO;
        for (FruitEntity fruit : fruits) {
            // 获取水果名称对应的斤数，默认为0
            int weight = weightMap.getOrDefault(fruit.getName(), 0);
            // 总价 += 单价 * 斤数 * 折扣
            total = total.add(fruit.getPrice().multiply(new BigDecimal(weight)).multiply(fruit.getDiscount()));
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 满减计算
     *
     * @param total                  总价
     * @param fullReductionThreshold 满减阈值
     * @param fullReductionAmount    每次满减金额
     * @return 满减后价格
     */
    private BigDecimal applyFullReduction(BigDecimal total, BigDecimal fullReductionThreshold, BigDecimal fullReductionAmount) {
        // 1. 计算满减次数：总价 ÷ 阈值 取整数部分
        int reductionCount = total.divideToIntegralValue(fullReductionThreshold).intValue();
        // 2. 计算实际减免金额：每次满减金额 × 满减次数
        BigDecimal finalTotal = total.subtract(fullReductionAmount.multiply(new BigDecimal(reductionCount)));
        // 3. 四舍五入保留两位小数返回
        return finalTotal.setScale(2, RoundingMode.HALF_UP);
    }
}
