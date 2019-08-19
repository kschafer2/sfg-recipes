package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.BytesUnwrappedToBytesWrapped;
import guru.springframework.converters.BytesWrappedToBytesUnwrapped;
import guru.springframework.domain.Image;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;
    private final BytesUnwrappedToBytesWrapped bytesUnwrappedToBytesWrapped;
    private final BytesWrappedToBytesUnwrapped bytesWrappedToBytesUnwrapped;

    public ImageServiceImpl(RecipeRepository recipeRepository,
                            BytesUnwrappedToBytesWrapped bytesUnwrappedToBytesWrapped,
                            BytesWrappedToBytesUnwrapped bytesWrappedToBytesUnwrapped) {
        this.recipeRepository = recipeRepository;
        this.bytesUnwrappedToBytesWrapped = bytesUnwrappedToBytesWrapped;
        this.bytesWrappedToBytesUnwrapped = bytesWrappedToBytesUnwrapped;
    }

    @Transactional
    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {
        try {
            Recipe recipe = recipeRepository.findById(recipeId).get();
            Byte[] imageBytes = bytesUnwrappedToBytesWrapped.convert(file.getBytes());
            Image image = new Image();

            image.setImageBytes(imageBytes);

            if(recipe.getImage() != null && recipe.getImage().getId() != null) {
                image.setId(recipe.getImage().getId());
            }
            recipe.setImage(image);
            recipeRepository.save(recipe);
        }
        catch (IOException e) {
            //todo handle better
            log.error("Error occurred", e);

            e.printStackTrace();
        }
    }

    public byte[] getImageBytesFromRecipeOrDefault(RecipeCommand recipeCommand, String defaultImagePath) {

        if (recipeCommand.getImage() != null && recipeCommand.getImage().getImageBytes() != null) {
            return bytesWrappedToBytesUnwrapped.convert(recipeCommand.getImage().getImageBytes());
        }
        else {
            File imageFile = new File(defaultImagePath);

            try {
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                ImageIO.write(bufferedImage, "jpg", outputStream);

                return outputStream.toByteArray();
            } catch (IOException e) {
                log.error("Unable to add image from filepath: " + imageFile.getAbsolutePath());
            }
        }
        throw new NotFoundException("Image bytes could not be located");
    }
}
