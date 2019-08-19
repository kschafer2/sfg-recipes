package guru.springframework.converters;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BytesUnwrappedToBytesWrappedTest {

    BytesUnwrappedToBytesWrapped converter;

    @Before
    public void setUp() {
        converter = new BytesUnwrappedToBytesWrapped();
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new byte[20]));
    }

    @Test
    public void convert() {
        //given
        byte[] unwrappedBytes = {23, 34, 45};

        //when
        Byte[] wrappedBytes = converter.convert(unwrappedBytes);

        //then
        assertNotNull(wrappedBytes);

        for(int i = 0; i < wrappedBytes.length; ++i) {
            assertEquals(wrappedBytes[i].byteValue(), unwrappedBytes[i]);
        }
    }
}