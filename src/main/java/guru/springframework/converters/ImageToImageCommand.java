package guru.springframework.converters;

import guru.springframework.commands.ImageCommand;
import guru.springframework.domain.Image;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class ImageToImageCommand implements Converter<Image, ImageCommand> {

    @Synchronized
    @Nullable
    @Override
    public ImageCommand convert(Image image) {
        if(image == null) {
            return null;
        }

        final ImageCommand imageCommand = new ImageCommand();

        imageCommand.setId(image.getId());
        imageCommand.setImageBytes(image.getImageBytes());

        return imageCommand;
    }
}
