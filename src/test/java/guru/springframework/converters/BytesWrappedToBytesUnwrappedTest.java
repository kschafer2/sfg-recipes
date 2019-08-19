package guru.springframework.converters;

import guru.springframework.exceptions.NotFoundException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BytesWrappedToBytesUnwrappedTest {

    BytesWrappedToBytesUnwrapped converter;

    @Before
    public void setUp() {
        converter = new BytesWrappedToBytesUnwrapped();
    }

    @Test(expected = NotFoundException.class)
    public void testNullObject() throws Exception {
        converter.convert(null);
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new Byte[20]));
    }

    @Test
    public void convert() throws Exception {
        //given
        Byte[] wrappedBytes = new Byte[]{20, 35, 45};

        //when
        byte[] unwrappedBytes = converter.convert(wrappedBytes);

        //then
        assertNotNull(unwrappedBytes);

        for(int i = 0; i < unwrappedBytes.length; i++) {
            assertEquals(wrappedBytes[i].byteValue(), unwrappedBytes[i]);
        }
    }
}