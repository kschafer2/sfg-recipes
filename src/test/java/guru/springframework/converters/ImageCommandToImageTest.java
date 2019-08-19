package guru.springframework.converters;

import guru.springframework.commands.ImageCommand;
import guru.springframework.domain.Image;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImageCommandToImageTest {

    ImageCommandToImage converter;

    @Before
    public void setUp() throws Exception {
        converter = new ImageCommandToImage();
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new ImageCommand()));
    }

    @Test
    public void convert() {
        //given
        ImageCommand imageCommand = new ImageCommand();
        imageCommand.setId(1L);
        imageCommand.setImageBytes(new Byte[]{34, 23, 56});

        //when
        Image image = converter.convert(imageCommand);

        //then
        assertNotNull(image);
        assertEquals(image.getId(), imageCommand.getId());
        assertEquals(image.getImageBytes().length, imageCommand.getImageBytes().length);



    }
}