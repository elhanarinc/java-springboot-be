package com.challenge.unit.service;

import com.challenge.model.Item;
import com.challenge.repository.ItemRepository;
import com.challenge.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ItemServiceTest {
  @Mock
  ItemRepository itemRepository;

  @InjectMocks
  ItemService itemService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
//    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void when_save_item_return_item() {
    Item newItem = new Item();

    newItem.setName("testitem");
    newItem.setCategory("testcategory");
    newItem.setPrice(1L);

    when(itemRepository.save(newItem)).thenReturn(newItem);

    Item created = itemService.saveItem(newItem);

    assertEquals(created.getName(), newItem.getName());
    assertEquals(created.getCategory(), newItem.getCategory());
    assertEquals(created.getPrice(), newItem.getPrice());
    assertEquals(created.getId(), newItem.getId());
  }

  @Test
  public void when_get_item_return_item() {
    Item newItem = new Item();

    newItem.setName("testitem");
    newItem.setCategory("testcategory");
    newItem.setPrice(1L);

    when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(newItem));

    Optional<Item> created = itemService.getOneItem(1L);

    assertEquals(created.get().getName(), newItem.getName());
    assertEquals(created.get().getCategory(), newItem.getCategory());
    assertEquals(created.get().getPrice(), newItem.getPrice());
    assertEquals(created.get().getId(), newItem.getId());
  }

  @Test
  public void when_replace_item_return_item() {
    Item newItem = new Item();

    newItem.setName("testitem");
    newItem.setCategory("testcategory");
    newItem.setPrice(1L);

    when(itemRepository.save(newItem)).thenReturn(newItem);

    Item updated = itemService.replaceItem(newItem, 1L);

    assertEquals(updated.getName(), newItem.getName());
    assertEquals(updated.getCategory(), newItem.getCategory());
    assertEquals(updated.getPrice(), newItem.getPrice());
    assertEquals(updated.getId(), newItem.getId());
  }

  @Test
  public void when_replace_item_return_item_second_version() {
    Item currentItem = new Item();

    currentItem.setName("testitem");
    currentItem.setCategory("testcategory");
    currentItem.setPrice(1L);

    Item newItem = new Item();

    newItem.setName("updateditem");
    newItem.setCategory("updatedCategory");
    newItem.setPrice(2L);

    when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(currentItem));
    when(itemRepository.save(any(Item.class))).thenReturn(newItem);

    Item updated = itemService.replaceItem(currentItem, 1L);

    assertEquals(updated.getName(), newItem.getName());
    assertEquals(updated.getCategory(), newItem.getCategory());
    assertEquals(updated.getPrice(), newItem.getPrice());
    assertEquals(updated.getId(), newItem.getId());
  }

  @Test
  public void when_delete_item_return_item() {
    Item newItem = new Item();

    newItem.setName("testitem");
    newItem.setCategory("testcategory");
    newItem.setPrice(1L);

    when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(newItem));

    Optional<Item> created = itemService.deleteItem(1L);

    assertEquals(created.get().getName(), newItem.getName());
    assertEquals(created.get().getCategory(), newItem.getCategory());
    assertEquals(created.get().getPrice(), newItem.getPrice());
    assertEquals(created.get().getId(), newItem.getId());
  }

  @Test
  public void when_get_all_items() {
    Item newItem1 = new Item();
    Item newItem2 = new Item();
    Item newItem3 = new Item();

    newItem1.setName("item1");
    newItem2.setName("item2");
    newItem3.setName("item3");

    newItem1.setCategory("category1");
    newItem2.setCategory("category2");
    newItem3.setCategory("category3");

    newItem1.setPrice(1L);
    newItem2.setPrice(2L);
    newItem3.setPrice(3L);

    List<Item> itemList = new ArrayList<>();
    itemList.add(newItem1);
    itemList.add(newItem2);
    itemList.add(newItem3);

    Map<String, List<Item>> groupedItems = new HashMap<>();

    for (Item item: itemList) {
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
    formattedData.put("total_count", itemList.size());
    formattedData.put("data", groupedItems);

    when(itemRepository.findPaginatedItems(any(Integer.class), any(Integer.class))).thenReturn(itemList);

    Map<String, Object> itemServiceData = itemService.findPaginatedItems(Optional.empty(),
            Optional.empty(),
            Optional.empty());

    assertEquals(formattedData, itemServiceData);
  }
}
