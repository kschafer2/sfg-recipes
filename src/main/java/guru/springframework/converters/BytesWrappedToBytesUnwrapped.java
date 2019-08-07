package guru.springframework.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class BytesWrappedToBytesUnwrapped implements Converter<Byte[], byte[]> {

    @Synchronized
    @Nullable
    @Override
    public byte[] convert(Byte[] wrappedBytes) {
        byte[] unwrappedBytes = new byte[wrappedBytes.length];

        for(int i = 0; i < unwrappedBytes.length; ++i) {
            unwrappedBytes[i] = wrappedBytes[i];
        }
        return unwrappedBytes;
    }
}
