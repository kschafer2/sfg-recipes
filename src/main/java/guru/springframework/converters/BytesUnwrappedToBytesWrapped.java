package guru.springframework.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class BytesUnwrappedToBytesWrapped implements Converter<byte[], Byte[]> {

    @Nullable
    @Override
    public Byte[] convert(byte[] unwrappedBytes) {
        if(unwrappedBytes == null) {
            return null;
        }
        Byte[] wrappedBytes = new Byte[unwrappedBytes.length];

        for(int i = 0; i < wrappedBytes.length; ++i) {
            wrappedBytes[i] = unwrappedBytes[i];
        }
        return wrappedBytes;
    }
}
