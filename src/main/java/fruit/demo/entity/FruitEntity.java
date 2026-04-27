package fruit.demo.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 水果实体类
 */
@Data
public class FruitEntity {
    // 水果名称
    private String name;
    // 水果价格单价-多少元一斤
    private BigDecimal price;
    // 折扣
    private BigDecimal discount;

    /**
     * 创建水果实体对象
     */
    public FruitEntity(String name, BigDecimal price, BigDecimal discount) {
        this.name = name;
        this.price = price;
        this.discount = discount;
    }

    public FruitEntity() {
    }
}
