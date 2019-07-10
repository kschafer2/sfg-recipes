package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureToUnitOfMeasureCommand implements Converter<UnitOfMeasure, UnitOfMeasureCommand> {

    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasureCommand convert(UnitOfMeasure uom) {
        if(uom == null) {
            return null;
        }

        final UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();

        uomCommand.setId(uom.getId());
        uomCommand.setDescription(uom.getDescription());

        return uomCommand;
    }
}
