package groupId.artifactId.core.mapper;

import groupId.artifactId.storage.entity.CompletedOrder;
import groupId.artifactId.storage.entity.Pizza;
import groupId.artifactId.storage.entity.api.ICompletedOrder;
import groupId.artifactId.storage.entity.api.IOrderData;
import groupId.artifactId.storage.entity.api.IPizza;
import groupId.artifactId.storage.entity.api.ISelectedItem;

import java.util.ArrayList;
import java.util.List;

public class CompletedOrderMapper {
    public static ICompletedOrder orderDataMapping(IOrderData orderData) {
        CompletedOrder order = new CompletedOrder();
        order.setToken(orderData.getToken());
        List<ISelectedItem> temp = orderData.getToken().getOrder().getSelectedItems();
        List<IPizza> pizzas = new ArrayList<>();
        for (ISelectedItem selectedItem : temp) {
            for (int j = 0; j < selectedItem.getCount(); j++) {
                pizzas.add(new Pizza(selectedItem.getItem().getInfo().getName(),
                        Math.toIntExact(selectedItem.getItem().getInfo().getSize())));
            }
        }
        order.setItems(pizzas);
        return order;
    }
}
