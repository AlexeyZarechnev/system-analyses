package io.github.alexeyzarechnev.autoservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "parts", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})
})
public class Part {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    private long articleNumber;

    private short remains;

    private int price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(long articleNumber) {
        this.articleNumber = articleNumber;
    }

    public short getRemains() {
        return remains;
    }

    public void setRemains(short remains) {
        this.remains = remains;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}