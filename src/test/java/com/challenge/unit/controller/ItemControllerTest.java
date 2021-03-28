package com.challenge.unit.controller;

import com.challenge.controller.ItemController;
import com.challenge.model.Item;
import com.challenge.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {
  @MockBean
  ItemService itemService;

  ObjectMapper mapper = new ObjectMapper();

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void it_should_return_created_item() throws Exception {
    Item newItem = new Item();

    newItem.setName("testitem");
    newItem.setCategory("testcategory");
    newItem.setDescription("testdescription");
    newItem.setPrice(123L);

    when(itemService.saveItem(any(Item.class))).thenReturn(newItem);

    mockMvc.perform(post("/v1/items")
            .content(mapper.writeValueAsString(newItem))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(newItem.getName()));
  }

  @Test
  public void it_should_return_fail_for_created_item() throws Exception {
    Item newItem = new Item();

    newItem.setName("testitem");
    newItem.setCategory("testcategory");
    newItem.setDescription("testdescription");

    when(itemService.saveItem(any(Item.class))).thenReturn(newItem);

    mockMvc.perform(post("/v1/items")
            .content(mapper.writeValueAsString(newItem))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Validation Failed"))
            .andExpect(jsonPath("$.details[0]").value("Price is mandatory."));
  }

  @Test
  public void it_should_return_all_items() throws Exception {
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

    when(itemService.findPaginatedItems(Optional.empty(), Optional.empty(), Optional.empty())).thenReturn(formattedData);

    mockMvc.perform(get("/v1/items"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_count").value(3))
            .andExpect(jsonPath("$.data").exists());
  }

  @Test
  public void it_should_return_one_item() throws Exception {
    Item newItem = new Item();

    newItem.setId(1L);
    newItem.setName("testitem");
    newItem.setCategory("testcategory");
    newItem.setDescription("testdescription");

    when(itemService.getOneItem(1L)).thenReturn(Optional.of(newItem));

    mockMvc.perform(get("/v1/items/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(newItem.getName()));
  }

  @Test
  public void it_should_return_empty() throws Exception {
    Item newItem = new Item();

    newItem.setId(1L);
    newItem.setName("testitem");
    newItem.setCategory("testcategory");
    newItem.setDescription("testdescription");

    when(itemService.getOneItem(2L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/v1/items/2"))
            .andExpect(status().isNotFound());
  }

  @Test
  public void it_should_change_one_item() throws Exception {
    Item newItem = new Item();

    newItem.setId(1L);
    newItem.setName("testitem2");
    newItem.setCategory("testcategory2");
    newItem.setPrice(100L);
    newItem.setDescription("testdescription2");

    when(itemService.replaceItem(any(Item.class), eq(1L))).thenReturn(newItem);

    mockMvc.perform(put("/v1/items/1")
            .content(mapper.writeValueAsString(newItem))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(newItem.getName()))
            .andExpect(jsonPath("$.category").value(newItem.getCategory()));
  }

  @Test
  public void it_should_delete_one_item() throws Exception {
    Item newItem = new Item();

    newItem.setId(1L);
    newItem.setName("testitem");
    newItem.setCategory("testcategory");
    newItem.setDescription("testdescription");

    when(itemService.deleteItem(eq(1L))).thenReturn(Optional.of(newItem));

    mockMvc.perform(delete("/v1/items/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(newItem.getName()))
            .andExpect(jsonPath("$.category").value(newItem.getCategory()));
  }

  @Test
  public void it_should_return_fail_for_delete_one_item() throws Exception {
    Item newItem = new Item();

    newItem.setId(1L);
    newItem.setName("testitem");
    newItem.setCategory("testcategory");
    newItem.setDescription("testdescription");

    when(itemService.deleteItem(eq(2L))).thenReturn(Optional.empty());

    mockMvc.perform(delete("/v1/items/2"))
            .andExpect(status().isNotFound());
  }
}
