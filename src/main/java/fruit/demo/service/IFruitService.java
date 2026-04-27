package fruit.demo.service;

import fruit.demo.entity.FruitEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 水果服务接口
 */
public interface IFruitService {

    /**
     * 顾客A：购买苹果和草莓，计算总价
     * 苹果 8 元/斤，草莓 13 元/斤
     *
     * @param appleWeight      苹果斤数（>=0的整数）
     * @param strawberryWeight 草莓斤数（>=0的整数）
     * @return 总价
     */
    BigDecimal buyAppleAndStrawberry(int appleWeight, int strawberryWeight);

    /**
     * 顾客B：购买苹果、草莓和芒果，计算总价
     * 苹果 8 元/斤，草莓 13 元/斤，芒果 20 元/斤
     *
     * @param appleWeight      苹果斤数（>=0的整数）
     * @param strawberryWeight 草莓斤数（>=0的整数）
     * @param mangoWeight      芒果斤数（>=0的整数）
     * @return 总价
     */
    BigDecimal buyAppleStrawberryAndMango(int appleWeight, int strawberryWeight, int mangoWeight);

    /**
     * 顾客C：购买苹果、草莓和芒果，草莓打8折，计算总价
     * 苹果 8 元/斤，草莓 13 元/斤（8折），芒果 20 元/斤
     *
     * @param appleWeight      苹果斤数（>=0的整数）
     * @param strawberryWeight 草莓斤数（>=0的整数）
     * @param mangoWeight      芒果斤数（>=0的整数）
     * @return 总价
     */
    BigDecimal buyAppleStrawberryAndMangoWithDiscount(int appleWeight, int strawberryWeight, int mangoWeight);

    /**
     * 顾客D：购买苹果、草莓和芒果，满100减10，计算总价
     * 苹果 8 元/斤，草莓 13 元/斤，芒果 20 元/斤，满100减10
     *
     * @param appleWeight      苹果斤数（>=0的整数）
     * @param strawberryWeight 草莓斤数（>=0的整数）
     * @param mangoWeight      芒果斤数（>=0的整数）
     * @return 总价
     */
    BigDecimal buyAppleStrawberryAndMangoWithFullReduction(int appleWeight, int strawberryWeight, int mangoWeight);

    /**
     * 通用计算方法：支持任意水果、任意折扣、满减规则
     *
     * @param fruits                 水果实体列表（包含名称、单价、折扣）
     * @param weightMap              水果名称 -> 斤数（>=0的整数）
     * @param fullReductionThreshold 满减阈值（null或0表示无满减）
     * @param fullReductionAmount    满减金额（null或0表示无满减）
     * @return 总价
     */
    BigDecimal calculate(List<FruitEntity> fruits, Map<String, Integer> weightMap, BigDecimal fullReductionThreshold, BigDecimal fullReductionAmount);
}
