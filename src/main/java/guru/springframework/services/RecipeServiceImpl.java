package guru.springframework.services;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.CategoryToCategoryCommand;
import guru.springframework.converters.ImageToImageCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j //used for logging
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private final ImageToImageCommand imageToImageCommand;
    private final CategoryToCategoryCommand categoryToCategoryCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeCommandToRecipe recipeCommandToRecipe,
                             RecipeToRecipeCommand recipeToRecipeCommand,
                             ImageToImageCommand imageToImageCommand,
                             CategoryToCategoryCommand categoryToCategoryCommand) {

        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
        this.imageToImageCommand = imageToImageCommand;
        this.categoryToCategoryCommand = categoryToCategoryCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("Service Activated");

        Set<Recipe> recipeSet = new HashSet<>();

        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);

        if(!recipeOptional.isPresent()) {

            //throw custom exception
            throw new NotFoundException("Recipe Not Found For ID Value: " + id.toString());
        }

        return recipeOptional.get();
    }

    @Transactional
    @Override
    public RecipeCommand getCommandById(Long id) {
        return recipeToRecipeCommand.convert(findById(id));
    }

    @Transactional
    @Override
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        if(recipeCommand.getCategories().size() == 0 && recipeCommand.getCategoryIds().size() > 0) {

            for(String id : recipeCommand.getCategoryIds()) {
                CategoryCommand categoryCommand = new CategoryCommand();
                categoryCommand.setId(Long.valueOf(id));
                recipeCommand.getCategories().add(categoryCommand);
            }
        }

        if(recipeCommand.getId() != null) {
            Recipe originalRecipe = this.findById(recipeCommand.getId());

            if(recipeCommand.getImage() != null) {
                recipeCommand.getImage().setId(originalRecipe.getImage().getId());
            } else {
                if(originalRecipe.getImage() != null) {
                    recipeCommand.setImage(imageToImageCommand.convert(originalRecipe.getImage()));
                }
            }
            if(recipeCommand.getNotes() != null) {
                recipeCommand.getNotes().setId(originalRecipe.getNotes().getId());
            }

            if(recipeCommand.getCategories().size() == 0 &&
                    originalRecipe.getCategories().size() > 0) {

                originalRecipe.getCategories()
                        .forEach(category -> recipeCommand.getCategories()
                                .add(categoryToCategoryCommand.convert(category)));

            }
        }

        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);

        log.debug("Saved RecipeId: " + savedRecipe.getId());

        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }
}
