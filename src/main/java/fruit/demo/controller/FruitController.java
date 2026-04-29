package fruit.demo.controller;

import fruit.demo.entity.FruitEntity;
import fruit.demo.entity.ShoppingCartEntity;
import fruit.demo.rule.FullReductionRule;
import fruit.demo.rule.PricingRule;
import fruit.demo.service.CheckoutService;
import fruit.demo.service.FruitCatalog;
import fruit.demo.entity.DiscountedFruit;
import fruit.demo.strategy.DiscountStrategy;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 水果商店控制器 - Spring MVC REST API
 */
@RestController
@RequestMapping("/api/fruit")
public class FruitController {

    private final FruitCatalog fruitCatalog;
    private final CheckoutService checkoutService;

    public FruitController(FruitCatalog fruitCatalog, CheckoutService checkoutService) {
        this.fruitCatalog = fruitCatalog;
        this.checkoutService = checkoutService;
    }

    /**
     * 获取所有水果列表
     */
    @GetMapping("/list")
    public Map<String, FruitEntity> listFruits() {
        return fruitCatalog.getAllFruitEntityList();
    }

    /**
     * 计算购物车总价
     * @param items 购物车商品（格式：{"苹果": 2, "草莓": 3}）
     * @param discountFruit 打折水果名称（可选）
     * @param discountRate 折扣率（可选，如0.8表示8折）
     * @param fullReductionThreshold 满减阈值（可选）
     * @param fullReductionAmount 满减金额（可选）
     * @return  最终价格
     */
    @PostMapping("/checkout")
    public BigDecimal checkout(
            @RequestBody Map<String, Integer> items,
            @RequestParam(required = false) String discountFruit,
            @RequestParam(required = false) BigDecimal discountRate,
            @RequestParam(required = false) BigDecimal fullReductionThreshold,
            @RequestParam(required = false) BigDecimal fullReductionAmount) {

        ShoppingCartEntity cart = new ShoppingCartEntity();

        // 添加水果到购物车
        items.forEach((fruitName, weight) -> {
            FruitEntity fruit = fruitCatalog.getFruitEntity(fruitName);

            // 如果该水果需要打折
            if (fruitName.equals(discountFruit) && discountRate != null) {
                fruit = new DiscountedFruit(
                        fruit, DiscountStrategy.percentage(discountRate)
                );
            }

            cart.addFruitEntity(fruit, weight);
        });

        // 应用满减规则
        PricingRule rule = null;
        if (fullReductionThreshold != null && fullReductionAmount != null) {
            rule = new FullReductionRule(fullReductionThreshold, fullReductionAmount);
        }

        return checkoutService.checkout(cart, rule);
    }
}
