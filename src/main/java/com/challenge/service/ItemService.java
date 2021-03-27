package com.challenge.service;

import com.challenge.model.Item;
import com.challenge.repository.ItemRepository;
import com.challenge.util.Utilities;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("itemService")
public class ItemService {
  private final ItemRepository repository;

  ItemService(ItemRepository repository) {
    this.repository = repository;
  }

  public Map<String, Object> findPaginatedItems(Optional<Integer> page, Optional<Integer> count,
                                          Optional<String> itemCategory) {
    Integer limit = 10;
    Integer offset = 0;

    if (count.isPresent()) {
      limit = count.get();
    }
    if (page.isPresent()) {
      offset = page.get() * limit;
    }

    List<Item> items;

    if (itemCategory.isPresent()) {
      String category = itemCategory.get().toLowerCase();
      items = repository.findPaginatedItemsWithCategory(limit, offset, category);
    } else {
      items = repository.findPaginatedItems(limit, offset);
    }

    Map<String, List<Item>> groupedItems = new HashMap<>();

    for (Item item: items) {
      String category = item.getCategory();
      if (!groupedItems.containsKey(category)) {
        List<Item> list = new ArrayList<>();
        list.add(item);

        groupedItems.put(category, list);
      } else {
        groupedItems.get(category).add(item);
      }
    }
    Map<String, Object> formattedData = new HashMap<>();
    formattedData.put("total_count", items.size());
    formattedData.put("data", groupedItems);
    return formattedData;
  }

  public Long saveItem(Item item) {
    item.setTs_create(Utilities.getCurrentEpoch());
    item.setCategory(item.getCategory().toLowerCase());
    return repository.save(item).getId();
  }

  public Optional<Item> getOneItem(Long id) {
    return repository.findById(id);
  }

  public Long replaceItem(Item newItem, Long id) {
    return repository.findById(id)
            .map(item -> {
              item.setName(newItem.getName());
              item.setCategory(newItem.getCategory().toLowerCase());
              item.setDescription(newItem.getDescription());
              item.setPicture_url(newItem.getPicture_url());
              item.setPrice(newItem.getPrice());
              item.setTs_update(Utilities.getCurrentEpoch());
              return repository.save(item).getId();
            })
            .orElseGet(() -> {
              newItem.setId(id);
              newItem.setTs_create(Utilities.getCurrentEpoch());
              newItem.setCategory(newItem.getCategory().toLowerCase());
              return repository.save(newItem).getId();
            });
  }

  public Long deleteItem(Long id) {
    repository.deleteById(id);
    return id;
  }
}
