package com.switchfully.eurder.item.domain;

import com.switchfully.eurder.customer.domain.Customer;
import com.switchfully.eurder.customer.domain.CustomerRepository;
import com.switchfully.eurder.customer.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepository {
    private final Logger repositoryLogger = LoggerFactory.getLogger(ItemRepository.class);

    private final Map<String, Item> itemsById;

    public ItemRepository() {
        this.itemsById = new HashMap<>();
    }

    public void save(Item item) {
        itemsById.put(item.getItemId(), item);
        repositoryLogger.info("New Item has been added to stock");
    }

    public Item findById(String itemId) {
        Item foundItem = itemsById.get(itemId);
        if (foundItem == null) {
            repositoryLogger.error("Customer with customer ID " + itemId + " not found");
            throw new NotFoundException(itemId);
        }
        return foundItem;
    }


}
