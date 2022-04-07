package com.switchfully.eurder.item.service;

import com.switchfully.eurder.item.api.dto.CreateItemDto;
import com.switchfully.eurder.item.api.dto.ItemDto;
import com.switchfully.eurder.item.domain.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
    public Item toItem(CreateItemDto newItem) {
        return new Item(newItem.getName(), newItem.getDescription(),newItem.getPrice(),newItem.getAmount());
    }

    public ItemDto toDto(Item item) {
        return new ItemDto(item.getItemId(), item.getName(), item.getDescription(), item.getPrice(), item.getAmount());
    }
}
