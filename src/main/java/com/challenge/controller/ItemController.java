package com.challenge.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.challenge.exceptions.RecordNotFoundException;
import com.challenge.model.Item;
import com.challenge.repository.ItemRepository;
import com.challenge.util.Utilities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ItemController {
  private final ItemRepository repository;

  ItemController(ItemRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/items")
  ResponseEntity<?> getAllItems(@RequestParam(value = "page", required = false) Optional<Integer> page,
                                @RequestParam(value = "count", required = false) Optional<Integer> count) {
    Integer limit = 10;
    Integer offset = 0;

    if (count.isPresent()) {
      limit = count.get();
    }
    if (page.isPresent()) {
      offset = page.get() * limit;
    }

    List<Item> items = repository.findPaginatedItems(limit, offset);
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
    Map<String, Object> response = new HashMap<>();
    response.put("total_count", items.size());
    response.put("data", groupedItems);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/items")
  ResponseEntity<Item> newItem(@Valid @RequestBody Item newItem) {
    newItem.setTs_create(Utilities.getCurrentEpoch());
    repository.save(newItem);
    return new ResponseEntity<>(newItem, HttpStatus.OK);
  }

  @GetMapping("/items/{id}")
  ResponseEntity<Item> getOneItem(@PathVariable Long id) {
    Optional<Item> item = repository.findById(id);

    if(item.isPresent()) {
      return new ResponseEntity<>(item.get(), HttpStatus.OK);
    }
    throw new RecordNotFoundException("Invalid item id : " + id);
  }

  @PutMapping("/items/{id}")
  ResponseEntity<Item> replaceItem(@Valid @RequestBody Item newItem, @PathVariable Long id) {
    return repository.findById(id)
            .map(item -> {
              item.setName(newItem.getName());
              item.setCategory(newItem.getCategory());
              item.setDescription(newItem.getDescription());
              item.setPicture_url(newItem.getPicture_url());
              item.setPrice(newItem.getPrice());
              item.setTs_update(Utilities.getCurrentEpoch());
              repository.save(item);
              return new ResponseEntity<>(item, HttpStatus.OK);
            })
            .orElseGet(() -> {
              newItem.setId(id);
              newItem.setTs_create(Utilities.getCurrentEpoch());
              repository.save(newItem);
              return new ResponseEntity<>(newItem, HttpStatus.OK);
            });
  }

  @DeleteMapping("/items/{id}")
  ResponseEntity<Long> deleteItem(@PathVariable Long id) {
    repository.deleteById(id);
    return new ResponseEntity<>(id, HttpStatus.OK);
  }
}
