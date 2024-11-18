package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonProperty("cookTime")
    private String cookTime;

    private String rating;

    private String calories;

    private String keywords;

    @JsonProperty("recipeCategory")
    private String recipeCategory;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("imageUrl")
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("cookingInstructions")
    private String cookingInstructions;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("ingredientsParts")
    private String ingredientsParts;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("ingredientsQuantities")
    private String ingredientsQuantities;

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cookTime='" + cookTime + '\'' +
                ", rating=" + rating +
                ", calories=" + calories +
                ", keywords='" + keywords + '\'' +
                ", recipeCategory='" + recipeCategory + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", cookingInstructions='" + cookingInstructions + '\'' +
                ", ingredientsParts='" + ingredientsParts + '\'' +
                ", ingredientsQuantities='" + ingredientsQuantities + '\'' +
                '}';
    }
}
