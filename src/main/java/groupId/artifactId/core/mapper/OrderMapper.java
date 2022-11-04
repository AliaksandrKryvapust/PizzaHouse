package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.OrderDtoOutput;
import groupId.artifactId.core.dto.output.SelectedItemDtoOutput;
import groupId.artifactId.dao.entity.Order;
import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    public static IOrder orderInputMapping(OrderDtoInput input) {
        List<ISelectedItem> items = new ArrayList<>();
        List<SelectedItemDtoInput> temp = input.getSelectedItems();
        for (SelectedItemDtoInput selectedItem : temp) {
            ISelectedItem item = SelectedItemMapper.selectedItemInputMapping(selectedItem);
            items.add(item);
        }
        return new Order(items);
    }

    public static OrderDtoOutput orderOutputMapping(IOrder order) {
        List<SelectedItemDtoOutput> items = new ArrayList<>();
        List<ISelectedItem> temp = order.getSelectedItems();
        for (ISelectedItem selectedItem : temp) {
            SelectedItemDtoOutput item = SelectedItemMapper.selectedItemOutputMapping(selectedItem);
            items.add(item);
        }
        return new OrderDtoOutput(items, order.getId(), order.getCreationDate(), order.getVersion());
    }
}
