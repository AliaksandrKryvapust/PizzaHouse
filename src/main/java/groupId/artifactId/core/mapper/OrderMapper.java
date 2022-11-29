package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.OrderDtoOutput;
import groupId.artifactId.core.dto.output.SelectedItemDtoOutput;
import groupId.artifactId.dao.entity.Order;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    private final SelectedItemMapper selectedItemMapper;

    public OrderMapper(SelectedItemMapper selectedItemMapper) {
        this.selectedItemMapper = selectedItemMapper;
    }

    public IOrder inputMapping(OrderDtoInput input, List<IMenuItem> menuItems) {
        List<ISelectedItem> items = new ArrayList<>();
        for (SelectedItemDtoInput selectedItem : input.getSelectedItems()) {
            ISelectedItem item = selectedItemMapper.inputMapping(selectedItem, menuItems);
            items.add(item);
        }
        return Order.builder().selectedItems(items).build();
    }

    public OrderDtoOutput outputMapping(IOrder order) {
        List<SelectedItemDtoOutput> items = new ArrayList<>();
        for (ISelectedItem selectedItem : order.getSelectedItems()) {
            SelectedItemDtoOutput item = selectedItemMapper.outputMapping(selectedItem);
            items.add(item);
        }
        return new OrderDtoOutput(items, order.getId());
    }
}
