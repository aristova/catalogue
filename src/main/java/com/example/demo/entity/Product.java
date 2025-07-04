package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@NamedEntityGraph(name = "Product.reviews",
        attributeNodes = @NamedAttributeNode("reviews")
)
@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "{product.title.notBlank}")
    private String title;

    @Size(min = 3, max = 255, message = "{product.details.size}")
    private String details;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    @JsonIgnore
    private List<Review> reviews;

    @Size(max = 255, message = "{product.image.size}")
    private String image;
}