package fruit.demo.rule;

import fruit.demo.entity.ShoppingCartEntity;

import java.math.BigDecimal;

/**
 * 计价规则接口
 * 定义各种计价规则（满减、折扣等）
 */
public interface PricingRule {
    
    /**
     * 应用计价规则
     * @param total 当前总价
     * @param cart 购物车
     * @return 应用规则后的总价
     */
    BigDecimal apply(BigDecimal total, ShoppingCartEntity cart);
    
    /**
     * 无规则（原价）
     */
    PricingRule NO_RULE = (total, cart) -> total;
}
