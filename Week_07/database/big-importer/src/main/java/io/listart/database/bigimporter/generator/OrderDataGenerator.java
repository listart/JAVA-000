package io.listart.database.bigimporter.generator;

import io.listart.database.bigimporter.entity.Order;
import io.listart.database.bigimporter.util.UUID;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Data
public class OrderDataGenerator {
    private Long size;
    private List<Order> data;

    public OrderDataGenerator(Long size) {
        this.size = size;
    }

    public List<Order> gen() {
        data = new LinkedList<>();

        for (int i = 0; i < size; i++) {
            Order order = new Order();
            order.setSn(UUID.newUUID());
            order.setConsignee(String.format("User-%d", i));
            order.setAddress(String.format("User-%d-address", i));
            order.setMobile(String.format("1%02d%04d%04d", new Random().nextInt(99), new Random().nextInt(9999), new Random().nextInt(9999)));
            data.add(order);
        }

        return data;
    }
}
