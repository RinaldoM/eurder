package com.switchfully.eurder.item.service;

import com.switchfully.eurder.item.api.dto.CreateItemDto;
import com.switchfully.eurder.item.api.dto.ItemDto;
import com.switchfully.eurder.item.domain.Item;
import com.switchfully.eurder.item.domain.ItemRepository;
import com.switchfully.eurder.item.exceptions.EmptyInputException;
import com.switchfully.eurder.item.exceptions.InvalidNumberInputException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;


@Service
public class ItemService {
    private final Logger serviceLogger = LoggerFactory.getLogger(ItemService.class);
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public ItemDto savenewItem(CreateItemDto createItemDto) {
        if(createItemDto.getName().isEmpty()){
            serviceLogger.error("Item name is Empty");
            throw new EmptyInputException("Item name");
        }
        if(createItemDto.getDescription().isEmpty()){
            serviceLogger.error("Item description is Empty");
            throw new EmptyInputException("Item description");
        }
        if(createItemDto.getPrice()<=0){
            serviceLogger.error("Invalid item price!");
            throw new InvalidNumberInputException(" for price!");
        }
        if(createItemDto.getAmount()<=0){
            serviceLogger.error("Invalid amount!");
            throw new InvalidNumberInputException(" for amount!");
        }

        Item item = itemMapper.toItem(createItemDto);
        itemRepository.save(item);
        return itemMapper.toDto(item);
    }
}
