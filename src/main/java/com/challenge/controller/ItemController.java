package com.challenge.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.challenge.exceptions.RecordNotFoundException;
import com.challenge.model.Item;
import com.challenge.service.ItemService;
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

  private final ItemService itemService;

  ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping("/v1/healthcheck")
  ResponseEntity<?> healthcheck() {
    Map<String, String> body = new HashMap<>();
    body.put("message", "OK");
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  @GetMapping("/v1/items")
  ResponseEntity<?> getAllItems(@RequestParam(value = "page", required = false) Optional<Integer> page,
                                @RequestParam(value = "count", required = false) Optional<Integer> count,
                                @RequestParam(value = "category", required = false) Optional<String> itemCategory) {

    Map<String, Object> response = itemService.findPaginatedItems(page, count, itemCategory);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/v1/items/{id}")
  ResponseEntity<Item> getOneItem(@PathVariable Long id) {
    Optional<Item> item = itemService.getOneItem(id);

    if(item.isPresent()) {
      return new ResponseEntity<>(item.get(), HttpStatus.OK);
    }
    throw new RecordNotFoundException("Invalid item id : " + id);
  }

  @PostMapping("/v1/items")
  ResponseEntity<?> newItem(@Valid @RequestBody Item newItem) {
    Long newItemId = itemService.saveItem(newItem);
    Map<String, String> body = new HashMap<>();
    body.put("item_id", newItemId.toString());
    return new ResponseEntity<>(body, HttpStatus.CREATED);
  }

  @PutMapping("/v1/items/{id}")
  ResponseEntity<?> replaceItem(@Valid @RequestBody Item newItem, @PathVariable Long id) {
    Long itemId = itemService.replaceItem(newItem, id);
    Map<String, String> body = new HashMap<>();
    body.put("item_id", itemId.toString());
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  @DeleteMapping("/v1/items/{id}")
  ResponseEntity<?> deleteItem(@PathVariable Long id) {
    Long itemId = itemService.deleteItem(id);
    Map<String, String> body = new HashMap<>();
    body.put("item_id", itemId.toString());
    return new ResponseEntity<>(body, HttpStatus.OK);
  }
}
