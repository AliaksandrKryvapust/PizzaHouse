package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.OrderDtoOutput;
import groupId.artifactId.core.dto.output.SelectedItemDtoOutput;
import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    private final SelectedItemMapper selectedItemMapper;

    public OrderMapper(SelectedItemMapper selectedItemMapper) {
        this.selectedItemMapper = selectedItemMapper;
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
