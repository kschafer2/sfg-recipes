package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
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

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeCommandToRecipe recipeCommandToRecipe,
                             RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("Service Activated");

        Set<Recipe> recipeSet = new HashSet<>();

        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(Long l) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(l);

        if(!recipeOptional.isPresent()) {

            //throw custom exception
            throw new NotFoundException("Recipe Not Found For ID Value: " + l.toString());
        }

        return recipeOptional.get();
    }

    @Transactional
    @Override
    public RecipeCommand findCommandById(Long l) {
        return recipeToRecipeCommand.convert(findById(l));
    }

    @Transactional
    @Override
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        if(recipeCommand.getId() != null) {
            Recipe originalRecipe = this.findById(recipeCommand.getId());
            recipeCommand.getNotes().setId(originalRecipe.getNotes().getId());
        }

        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);

        log.debug("Saved RecipeId: " + savedRecipe.getId());

        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    public void deleteById(Long l) {
        recipeRepository.deleteById(l);
    }
}
