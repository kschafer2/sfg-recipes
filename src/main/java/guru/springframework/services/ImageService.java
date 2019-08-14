package guru.springframework.services;

import guru.springframework.commands.ImageCommand;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void saveImageFile(Long recipeId, MultipartFile file);

    Byte[] getImageByteArray(ImageCommand imageCommand);
}
