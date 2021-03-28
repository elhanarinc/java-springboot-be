package com.challenge.unit.repository;

import com.challenge.model.Item;
import com.challenge.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@DataJpaTest
public class ItemRepositoryTest {
  @Autowired
  TestEntityManager entityManager;

  @Autowired
  ItemRepository itemRepository;

  @Test
  public void it_should_save_item() {
    Item item = new Item();
    item.setId(1L);
    item.setName("testname");
    item.setCategory("testcategory");
    item.setPrice(111L);

    Item savedItem = itemRepository.save(item);
    Item returnedItem = entityManager.find(Item.class, 1L);

    assertEquals(savedItem.getId(), returnedItem.getId());
  }

  @Test
  public void it_should_find_item() {
    Item item = new Item();
    item.setId(1L);
    item.setName("testname");
    item.setCategory("testcategory");
    item.setPrice(111L);

    Item savedItem = entityManager.merge(item);
    Optional<Item> returnedItem = itemRepository.findById(item.getId());

    assertEquals(savedItem.getId(), returnedItem.get().getId());
  }

  @Test
  public void it_should_update_item() {
    Item item = new Item();
    item.setId(1L);
    item.setName("testname");
    item.setCategory("testcategory");
    item.setPrice(111L);

    Item savedItem = itemRepository.save(item);

    Item returnedItem = entityManager.find(Item.class, 1L);

    savedItem.setName("updatedname");
    savedItem.setCategory("updatecategory");
    savedItem.setPrice(123L);

    savedItem = itemRepository.save(savedItem);

    assertEquals(savedItem.getId(), returnedItem.getId());
    assertEquals(savedItem.getName(), returnedItem.getName());
    assertEquals(savedItem.getCategory(), returnedItem.getCategory());
    assertEquals(savedItem.getPrice(), returnedItem.getPrice());
  }

  @Test
  public void it_should_delete_item() {
    Item item = new Item();
    item.setId(1L);
    item.setName("testname");
    item.setCategory("testcategory");
    item.setPrice(111L);

    Item savedItem = itemRepository.save(item);
    itemRepository.delete(savedItem);

    Item returnedItem = entityManager.find(Item.class, 1L);
    assertNull(returnedItem);
  }

  @Test
  public void it_should_fetch_all_items() {
    Item item1 = new Item();
    item1.setName("testname1");
    item1.setCategory("testcategory1");
    item1.setPrice(111L);
    item1.setTs_create(System.currentTimeMillis() / 1000);

    Item item2 = new Item();
    item2.setName("testname2");
    item2.setCategory("testcategory2");
    item2.setPrice(111L);
    item2.setTs_create(System.currentTimeMillis() / 1000);

    Item item3 = new Item();
    item3.setName("testname3");
    item3.setCategory("testcategory3");
    item3.setPrice(111L);
    item3.setTs_create(System.currentTimeMillis() / 1000);

    itemRepository.save(item1);
    itemRepository.save(item2);
    itemRepository.save(item3);

    List<Item> items = itemRepository.findPaginatedItems(5, 0);

    System.out.println(items);

    Item returnedItem1 = entityManager.find(Item.class, 1L);
    Item returnedItem2 = entityManager.find(Item.class, 2L);
    Item returnedItem3 = entityManager.find(Item.class, 3L);
    assertEquals(items.get(0).getName(), returnedItem1.getName());
    assertEquals(items.get(1).getName(), returnedItem2.getName());
    assertEquals(items.get(2).getName(), returnedItem3.getName());
  }
}
