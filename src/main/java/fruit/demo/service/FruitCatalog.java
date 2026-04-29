package fruit.demo.service;

import fruit.demo.entity.FruitEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 水果目录
 * 管理所有可用水果的种类和价格
 */
@Component
public class FruitCatalog {
    private static final FruitCatalog INSTANCE = new FruitCatalog();
    private final Map<String, FruitEntity> catalog = new HashMap<>();

    private FruitCatalog() {
        // 初始化默认水果
        catalog.put("苹果", new FruitEntity("苹果", new BigDecimal("8")));
        catalog.put("草莓", new FruitEntity("草莓", new BigDecimal("13")));
        catalog.put("芒果", new FruitEntity("芒果", new BigDecimal("20")));
    }

    public static FruitCatalog getInstance() {
        return INSTANCE;
    }

    /**
     * 根据名称获取水果
     */
    public FruitEntity getFruitEntity(String name) {
        FruitEntity FruitEntity = catalog.get(name);
        if (FruitEntity == null) {
            throw new IllegalArgumentException("未知水果: " + name);
        }
        return FruitEntity;
    }

    /**
     * 添加新水果（扩展用）
     */
    public void addFruitEntity(String name, BigDecimal price) {
        catalog.put(name, new FruitEntity(name, price));
    }

    /**
     * 获取所有水果
     */
    public Map<String, FruitEntity> getAllFruitEntityList() {
        return new HashMap<>(catalog);
    }
}
