package guru.springframework.converters;

import guru.springframework.exceptions.NotFoundException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class BytesWrappedToBytesUnwrapped implements Converter<Byte[], byte[]> {

    @Nullable
    @Override
    public byte[] convert(Byte[] wrappedBytes) {
        if(wrappedBytes == null) {
            throw new NotFoundException("No Object to convert!");
        }
        byte[] unwrappedBytes = new byte[wrappedBytes.length];

        for(int i = 0; i < unwrappedBytes.length; ++i) {
            if(wrappedBytes[i] != null) {
                unwrappedBytes[i] = wrappedBytes[i];
            }
        }
        return unwrappedBytes;
    }
}
