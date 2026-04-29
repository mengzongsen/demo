package fruit.demo.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 水果实体类
 */
@Data
public class FruitEntity {
    private final String name;
    private final BigDecimal unitPrice; // 每斤价格

    public FruitEntity(String name, BigDecimal unitPrice) {
        this.name = name;
        this.unitPrice = unitPrice;
    }

    /**
     * 计算指定重量水果的价格
     * @param weight 重量（斤）
     * @return 价格
     */
    public BigDecimal calculatePrice(int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("重量不能为负数");
        }
        return unitPrice.multiply(BigDecimal.valueOf(weight));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FruitEntity FruitEntity = (FruitEntity) o;
        return name.equals(FruitEntity.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name + "(¥" + unitPrice + "/斤)";
    }
}
