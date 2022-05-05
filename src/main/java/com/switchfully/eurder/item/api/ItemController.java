package com.switchfully.eurder.item.api;

import com.switchfully.eurder.item.api.dto.CreateItemDto;
import com.switchfully.eurder.item.api.dto.ItemDto;
import com.switchfully.eurder.item.api.dto.UpdateItemDto;
import com.switchfully.eurder.item.service.ItemService;
import com.switchfully.eurder.security.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.switchfully.eurder.security.Feature.ADD_ITEM;

@RestController
@RequestMapping("items")
public class ItemController {
    private final ItemService itemService;
    private final SecurityService securityService;

    public ItemController(ItemService itemService, SecurityService securityService) {
        this.itemService = itemService;
        this.securityService = securityService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createNewItem(@RequestBody CreateItemDto createItemDto, @RequestHeader String authorization){
        securityService.validateAuthorization(authorization, ADD_ITEM);
        return itemService.saveNewItem(createItemDto);
    }
    @PutMapping(path="{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestBody UpdateItemDto updateItemDto, @PathVariable  Long itemId){
        return itemService.updateItem(itemId, updateItemDto);
    }
}
