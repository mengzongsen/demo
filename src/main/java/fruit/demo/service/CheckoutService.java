package fruit.demo.service;

import fruit.demo.entity.ShoppingCartEntity;
import fruit.demo.rule.PricingRule;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/**
 * 结账服务
 */
@Service
public class CheckoutService {

    /**
     * 计算购物车总价（应用所有规则）
     * @param cart 购物车
     * @param rule 计价规则（可为null表示无规则）
     * @return 最终价格
     */
    public BigDecimal checkout(ShoppingCartEntity cart, PricingRule rule) {
        BigDecimal subtotal = cart.getSubtotal();
        if (rule == null) {
            return subtotal;
        }
        return rule.apply(subtotal, cart);
    }

    /**
     * 计算购物车总价（无额外规则）
     */
    public BigDecimal checkout(ShoppingCartEntity cart) {
        return checkout(cart, null);
    }
}
