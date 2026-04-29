package fruit.demo.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * 购物车类 - 封装购物逻辑
 */
public class ShoppingCartEntity {
    private final Map<FruitEntity, Integer> items = new HashMap<>();

    /**
     * 添加水果到购物车
     */
    public void addFruitEntity(FruitEntity fruitEntity, int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("重量不能为负数");
        }
        if (weight > 0) {
            items.put(fruitEntity, items.getOrDefault(fruitEntity, 0) + weight);
        }
    }

    /**
     * 计算小计（不考虑满减规则）
     */
    public BigDecimal getSubtotal() {
        return items.entrySet().stream()
                .map(entry -> entry.getKey().calculatePrice(entry.getValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
