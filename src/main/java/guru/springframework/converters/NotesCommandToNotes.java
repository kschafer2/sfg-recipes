package guru.springframework.converters;

import com.sun.xml.internal.ws.developer.Serialization;
import guru.springframework.commands.NotesCommand;
import guru.springframework.domain.Notes;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Null;

@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {

    @Serialization
    @Null
    @Override
    public Notes convert(NotesCommand notesCommand) {
        if(notesCommand == null) {
            return null;
        }

        final Notes notes = new Notes();

        notes.setId(notesCommand.getId());
        notes.setRecipeNotes(notesCommand.getRecipeNotes());

        return notes;
    }
}
