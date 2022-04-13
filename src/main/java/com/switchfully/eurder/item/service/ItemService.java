package com.switchfully.eurder.item.service;

import com.switchfully.eurder.item.api.dto.CreateItemDto;
import com.switchfully.eurder.item.api.dto.ItemDto;
import com.switchfully.eurder.item.api.dto.UpdateItemDto;
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

    public ItemDto saveNewItem(CreateItemDto createItemDto) {
        loggingErrorEmptyInput(createItemDto.getName(), "name");
        loggingErrorEmptyInput(createItemDto.getDescription(), "description");
        loggingErrorInvalidInput(createItemDto.getPrice(),"price");
        loggingErrorInvalidInput(createItemDto.getAmount(),"amount");

        Item item = itemMapper.toItem(createItemDto);
        itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    private void loggingErrorEmptyInput(String input, String fieldName) {
        if (input.isEmpty()) {
            serviceLogger.error("Item " + fieldName + " is empty!");
            throw new EmptyInputException(fieldName);
        }
    }

    private void loggingErrorInvalidInput(double input, String fieldName){
        if (input <= 0) {
            serviceLogger.error("Invalid item "+fieldName+"!");
            throw new InvalidNumberInputException(" for price!");
        }
    }

    public Item findItemById(String itemId){
        return itemRepository.findById(itemId);
    }

    public void removeFromStock(Item itemToUpdate, int amount){
        Item item = itemRepository.findById(itemToUpdate.getItemId());
        item.removeFromStock(amount);
    }

    public ItemDto updateItem(String itemId, UpdateItemDto updateItemDto) {
        Item item = itemRepository.findById(itemId);
        loggingErrorEmptyInput(updateItemDto.getName(), "name");
        loggingErrorEmptyInput(updateItemDto.getDescription(), "description");
        loggingErrorInvalidInput(updateItemDto.getAmount(), "amount");
        loggingErrorInvalidInput(updateItemDto.getPrice(), "price");
        item.setName(updateItemDto.getName());
        item.setDescription(updateItemDto.getDescription());
        item.setAmount(updateItemDto.getAmount());
        item.setPrice(updateItemDto.getPrice());
        return itemMapper.toDto(item);
    }
}
