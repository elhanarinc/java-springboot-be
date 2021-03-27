package com.challenge.model;

import com.challenge.util.Utilities;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class Item {
  private @Id @GeneratedValue Long id;

  @NotBlank(message = "Name is mandatory.")
  @Size(min = 1, max = 180, message = "Name length should be between 1 and 180 characters.")
  private String name;

  @NotBlank(message = "Category is mandatory.")
  @Size(min = 1, max = 180, message = "Category length should be between 1 and 180 characters.")
  private String category;

  @Size(min = 1, max = 255, message = "Description length should be between 1 and 255 characters.")
  private String description;

  @URL(message = "Picture Url should be a valid URL.")
  private String picture_url;

  @NotNull(message = "Price is mandatory.")
  @Min(value = 0, message = "Price should be at least 0.")
  private Long price;

  private Long ts_create;

  private Long ts_update;

  public Item() {};

  public Item(String name, String category, String description, String picture_url, Long price) {
    this.name = name;
    this.category = category.toLowerCase();
    this.description = description;
    this.picture_url = picture_url;
    this.price = price;
    this.ts_create = Utilities.getCurrentEpoch();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPicture_url() {
    return picture_url;
  }

  public void setPicture_url(String picture_url) {
    this.picture_url = picture_url;
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public Long getTs_create() {
    return ts_create;
  }

  public void setTs_create(Long ts_create) {
    this.ts_create = ts_create;
  }

  public Long getTs_update() {
    return ts_update;
  }

  public void setTs_update(Long ts_update) {
    this.ts_update = ts_update;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name, this.category, this.description, this.picture_url,
            this.price, this.ts_create, this.ts_update);
  }
}
