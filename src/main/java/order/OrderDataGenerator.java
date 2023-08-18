package order;

import java.util.List;

public class OrderDataGenerator {
    public static Order getOrderData(){
        return new Order(List.of("61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa74"));
    }
    public static Order getInvalidOrderData(){
        return new Order(List.of("vkusno", "i", "tochka"));
    }
}
