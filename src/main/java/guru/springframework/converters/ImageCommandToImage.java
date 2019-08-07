package guru.springframework.converters;

import guru.springframework.commands.ImageCommand;
import guru.springframework.domain.Image;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class ImageCommandToImage implements Converter<ImageCommand, Image> {

    @Synchronized
    @Nullable
    @Override
    public Image convert(ImageCommand imageCommand) {
        if(imageCommand == null) {
            return null;
        }

        final Image image = new Image();

        image.setId(imageCommand.getId());
        image.setImageBytes(imageCommand.getImageBytes());

        return image;
    }
}
