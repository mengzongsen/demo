package fruit.demo.rule;

import fruit.demo.entity.ShoppingCartEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 满减规则 - 如满100减10
 */
public class FullReductionRule implements PricingRule {
    private final BigDecimal threshold; // 满减的门槛金额
    private final BigDecimal reduction; // 满减的金额

    public FullReductionRule(BigDecimal threshold, BigDecimal reduction) {
        this.threshold = threshold;
        this.reduction = reduction;
    }

    @Override
    public BigDecimal apply(BigDecimal total, ShoppingCartEntity cart) {
        // 如果总价未达到满减门槛，则不减
        if (threshold.compareTo(BigDecimal.ZERO) <= 0) {
            return total;
        }
        // 计算满减的次数（如满100减10，200减20，以此类推）
        int times = total.divideToIntegralValue(threshold).intValue();
        // 计算最终价格
        return total.subtract(reduction.multiply(BigDecimal.valueOf(times)))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
