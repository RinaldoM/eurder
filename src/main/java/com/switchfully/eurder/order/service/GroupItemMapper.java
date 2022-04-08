package com.switchfully.eurder.order.service;

import com.switchfully.eurder.item.domain.Item;
import com.switchfully.eurder.item.service.ItemService;
import com.switchfully.eurder.order.api.dto.CreateGroupItemDto;
import com.switchfully.eurder.order.api.dto.GroupItemDto;
import com.switchfully.eurder.order.domain.GroupItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GroupItemMapper {
    private final ItemService itemService;

    public GroupItemMapper(ItemService itemService) {
        this.itemService = itemService;
    }


    public GroupItem toGroupitem(CreateGroupItemDto createGroupItemDto) {
        return new GroupItem(createGroupItemDto.getItemId(), createGroupItemDto.getAmount());
    }

    public List<GroupItem> toGroupitem(List<CreateGroupItemDto> createGroupItemDtoList) {
        return createGroupItemDtoList
                .stream()
                .map(this::toGroupitem)
                .collect(Collectors.toList());
    }

    public GroupItemDto toDto(GroupItem groupItem){
        Item item = itemService.findItemById(groupItem.getItemId());
        return new GroupItemDto(item.getName(), groupItem.getAmount(), (groupItem.getAmount())*item.getPrice());
    }

    public List<GroupItemDto> toDto(List<GroupItem> groupItemList){
        return groupItemList
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


}
