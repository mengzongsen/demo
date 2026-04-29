package fruit.demo.entity;

import fruit.demo.strategy.DiscountStrategy;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 打折水果
 * 在不改变原水果类的情况下，为其添加折扣功能
 */
@Getter
public class DiscountedFruit extends FruitEntity {
    private final FruitEntity fruitEntity;
    private final DiscountStrategy discountStrategy;

    public DiscountedFruit(FruitEntity fruitEntity, DiscountStrategy discountStrategy) {
        super(fruitEntity.getName(), fruitEntity.getUnitPrice());
        this.fruitEntity = fruitEntity;
        this.discountStrategy = discountStrategy;
    }

    /**
     * 计算打折后的价格
     */
    @Override
    public BigDecimal calculatePrice(int weight) {
        BigDecimal originalPrice = fruitEntity.calculatePrice(weight);
        return discountStrategy.apply(originalPrice);
    }

    @Override
    public String toString() {
        return fruitEntity.toString() + "[打折]";
    }
}
