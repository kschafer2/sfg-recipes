package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureCommandToUnitOfMeasure implements Converter<UnitOfMeasureCommand, UnitOfMeasure> {

    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasure convert(UnitOfMeasureCommand uomCommand) {
        if(uomCommand == null) {
            return null;
        }

        final UnitOfMeasure uom = new UnitOfMeasure();

        uom.setId(uomCommand.getId());
        uom.setDescription(uomCommand.getDescription());

        return uom;
    }
}

