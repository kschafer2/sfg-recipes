package guru.springframework.services;

import guru.springframework.converters.BytesUnwrappedToBytesWrapped;
import guru.springframework.domain.Image;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {
        try{
            Recipe recipe = recipeRepository.findById(recipeId).get();

            Byte[] imageBytes = new BytesUnwrappedToBytesWrapped().convert(file.getBytes());
//            //todo create wrap method
//            Byte[] byteObjects = new Byte[file.getBytes().length];
//
//            int i = 0;
//
//            for(byte b : file.getBytes()) {
//                byteObjects[i++] = b;
//            }
            Image image = new Image();
            image.setImageBytes(imageBytes);

            recipe.setImage(image);

            recipeRepository.save(recipe);
        } catch (IOException e) {
            //todo handle better
            log.error("Error occurred", e);

            e.printStackTrace();
        }
    }
}
