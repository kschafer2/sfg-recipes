package guru.springframework.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude={"recipe"})
@Entity
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne //no cascade; owned by recipe
    private Recipe recipe;

    @Lob //identifies large field; allows more than 255 chars
    private String recipeNotes;
}
