package guru.springframework.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

public class BytesUnwrappedToBytesWrapped implements Converter<byte[], Byte[]> {

    @Synchronized
    @Nullable
    @Override
    public Byte[] convert(byte[] unwrappedBytes) {
        Byte[] wrappedBytes = new Byte[unwrappedBytes.length];

        for(int i = 0; i < wrappedBytes.length; ++i) {
            wrappedBytes[i] = unwrappedBytes[i];
        }
        return wrappedBytes;
    }
}
