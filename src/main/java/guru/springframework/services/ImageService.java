package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void saveImageFile(Long recipeId, MultipartFile file);

    byte[] getImageBytesFromRecipeOrDefault(RecipeCommand recipeCommand, String defaultImagePath);
}
