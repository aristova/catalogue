package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@NamedEntityGraph(
        name = "Product.reviews",
        attributeNodes = @NamedAttributeNode("reviews")
)
@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank(message = "{product.title.notBlank}")
    @ToString.Include
    private String title;

    @Size(min = 3, max = 255, message = "{product.details.size}")
    @ToString.Include
    private String details;

    @Size(max = 255, message = "{product.image.size}")
    @ToString.Include
    private String image;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private List<Review> reviews;
}