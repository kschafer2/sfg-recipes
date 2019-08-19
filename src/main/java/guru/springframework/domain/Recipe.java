package guru.springframework.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;

    @Lob
    private String directions;

    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe") //owns ingredient
    private Set<Ingredient> ingredients = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL) //owns image
    private Image image;

    @OneToOne(cascade = CascadeType.ALL) //owns notes
    private Notes notes;

    @ManyToMany
    @JoinTable(name = "recipe_category",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    //logic for bidirectional relationship with notes
    public void setNotes(Notes notes) {
        if(notes != null) {
            this.notes = notes;
            notes.setRecipe(this);
        }
    }
    //logic for bidirectional relationship with image
    public void setImage(Image image) {
        if(image != null) {
            this.image = image;
            image.setRecipe(this);
        }
    }

    //logic for bidirectional relationship with ingredient
    public Recipe addIngredient(Ingredient ingredient) {

        ingredient.setRecipe(this);
        this.ingredients.add(ingredient);
        return this;
    }
}

