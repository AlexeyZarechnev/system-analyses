package io.github.alexeyzarechnev.autoservice.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Part representation
 */

@Schema(name = "PartDTO", description = "Part representation")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-24T19:51:35.954605+03:00[Europe/Moscow]", comments = "Generator version: 7.8.0")
public class PartDTO {

  private Long id;

  private String name;

  private Long articleNumber;

  private Integer remains;

  private Integer price;

  public PartDTO() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PartDTO(String name, Long articleNumber, Integer remains, Integer price) {
    this.name = name;
    this.articleNumber = articleNumber;
    this.remains = remains;
    this.price = price;
  }

  public PartDTO id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Unique part identifier
   * @return id
   */
  
  @Schema(name = "id", description = "Unique part identifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PartDTO name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Part name
   * @return name
   */
  @NotNull @Size(max = 255) 
  @Schema(name = "name", description = "Part name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PartDTO articleNumber(Long articleNumber) {
    this.articleNumber = articleNumber;
    return this;
  }

  /**
   * Article number
   * @return articleNumber
   */
  @NotNull 
  @Schema(name = "articleNumber", description = "Article number", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("articleNumber")
  public Long getArticleNumber() {
    return articleNumber;
  }

  public void setArticleNumber(Long articleNumber) {
    this.articleNumber = articleNumber;
  }

  public PartDTO remains(Integer remains) {
    this.remains = remains;
    return this;
  }

  /**
   * Available stock quantity
   * @return remains
   */
  @NotNull 
  @Schema(name = "remains", description = "Available stock quantity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("remains")
  public Integer getRemains() {
    return remains;
  }

  public void setRemains(Integer remains) {
    this.remains = remains;
  }

  public PartDTO price(Integer price) {
    this.price = price;
    return this;
  }

  /**
   * Part price
   * @return price
   */
  @NotNull 
  @Schema(name = "price", description = "Part price", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("price")
  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartDTO partDTO = (PartDTO) o;
    return Objects.equals(this.id, partDTO.id) &&
        Objects.equals(this.name, partDTO.name) &&
        Objects.equals(this.articleNumber, partDTO.articleNumber) &&
        Objects.equals(this.remains, partDTO.remains) &&
        Objects.equals(this.price, partDTO.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, articleNumber, remains, price);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartDTO {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    articleNumber: ").append(toIndentedString(articleNumber)).append("\n");
    sb.append("    remains: ").append(toIndentedString(remains)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

