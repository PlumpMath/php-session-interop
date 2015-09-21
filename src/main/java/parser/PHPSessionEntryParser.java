package parser;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import exceptions.InvalidSessionEntryException;
import exceptions.ParseException;
import exceptions.UnknownPHPObjectType;
import parser.objects.PHPObject;

import java.io.ByteArrayInputStream;

public class PHPSessionEntryParser {

    private ByteArrayInputStream inputStream;

    public PHPSessionEntryParser(ByteArrayInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public PHPSessionEntry nextEntry() throws InvalidSessionEntryException, UnknownPHPObjectType {
        PHPSessionEntry entry = new PHPSessionEntry();

        try {
            String fieldName = BaseParser.nextWord(inputStream, '|');
            if(fieldName == null) {
                return null;
            }

            entry.setFieldName(fieldName);
            entry.setValue(PHPObjectParser.nextObject(inputStream));
        } catch (ParseException e) {
            throw new InvalidSessionEntryException("Parsing failed: " + e.getMessage());
        }

        return entry;
    }

}
