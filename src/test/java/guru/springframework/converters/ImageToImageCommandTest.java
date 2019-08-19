package guru.springframework.converters;

import guru.springframework.commands.ImageCommand;
import guru.springframework.domain.Image;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImageToImageCommandTest {

    ImageToImageCommand converter;

    @Before
    public void setUp() throws Exception {
        converter = new ImageToImageCommand();
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new Image()));
    }

    @Test
    public void convert() {
        Image image = new Image();
        image.setId(1L);
        image.setImageBytes(new Byte[]{12, 23, 34});

        //when
        ImageCommand imageCommand = converter.convert(image);

        //then
        assertNotNull(imageCommand);
        assertEquals(imageCommand.getId(), image.getId());
        assertEquals(imageCommand.getImageBytes().length, image.getImageBytes().length);
    }
}