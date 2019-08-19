package guru.springframework.commands;

import guru.springframework.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class RecipeCommand {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 255) //max is hibernate default
    private String description;

    @Min(1)
    @Max(999)
    @NotNull
    private Integer prepTime;

    @Max(999)
    @NotNull
    private Integer cookTime;

    @Min(1)
    @Max(100)
    @NotNull
    private Integer servings;
    private String source;

    @URL
    @NotBlank
    private String url;

    @NotBlank
    private String directions;
    private Set<IngredientCommand> ingredients = new HashSet<>();
    private ImageCommand image;
    private Difficulty difficulty;
    private NotesCommand notes;
    private Set<CategoryCommand> categories = new HashSet<>();
    private List<String> categoryIds = new ArrayList<>();
}
