package guru.springframework.services;

import guru.springframework.commands.ImageCommand;
import guru.springframework.converters.BytesUnwrappedToBytesWrapped;
import guru.springframework.domain.Image;
import guru.springframework.domain.Recipe;
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

    public ImageServiceImpl(RecipeRepository recipeRepository,
                            BytesUnwrappedToBytesWrapped bytesUnwrappedToBytesWrapped) {
        this.recipeRepository = recipeRepository;
        this.bytesUnwrappedToBytesWrapped = bytesUnwrappedToBytesWrapped;
    }

    @Transactional
    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {
        try {
            Recipe recipe = recipeRepository.findById(recipeId).get();
            Byte[] imageBytes = bytesUnwrappedToBytesWrapped.convert(file.getBytes());
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

    public Byte[] getImageByteArray(ImageCommand imageCommand) {
        byte[] byteArray;

        if (imageCommand != null) {
            return imageCommand.getImageBytes();
        } else {
            File imageFile = new File("src/main/resources/static/images/default.jpg");
            try {
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                ImageIO.write(bufferedImage, "jpg", outputStream);

                return bytesUnwrappedToBytesWrapped.convert(outputStream.toByteArray());
            } catch (IOException e) {
                log.error("Unable to add image from filepath: " + imageFile.getAbsolutePath());
            }
        }
        return null;
    }
}
