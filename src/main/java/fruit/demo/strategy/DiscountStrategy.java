package fruit.demo.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 折扣策略接口
 * 定义折扣的计算规则，支持不同的折扣方式
 */
public interface DiscountStrategy {
    
    /**
     * 应用折扣
     * @param originalPrice 原价
     * @return 折扣后的价格
     */
    BigDecimal apply(BigDecimal originalPrice);
    
    /**
     * 无折扣策略
     */
    DiscountStrategy NO_DISCOUNT = originalPrice -> originalPrice;
    
    /**
     * 百分比折扣策略（如8折）
     */
    static DiscountStrategy percentage(BigDecimal rate) {
        return originalPrice -> originalPrice.multiply(rate)
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 固定金额减免策略
     */
    static DiscountStrategy fixedReduction(BigDecimal reductionAmount) {
        return originalPrice -> originalPrice.subtract(reductionAmount)
                .max(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
