package parser;

import exceptions.ParseException;
import exceptions.UnknownPHPObjectType;
import parser.objects.PHPObject;

import java.io.ByteArrayInputStream;

public class PHPObjectParser {

    public static PHPObject nextObject(ByteArrayInputStream inputStream) throws ParseException, UnknownPHPObjectType {

        String typeStr = BaseParser.nextWord(inputStream, ':');
        Integer length;

        if(typeStr == null) {
            return null;
        }

        switch (typeStr) {
            case "b":
                return new PHPObject<>(BaseParser.nextBoolean(inputStream, ';'), typeStr, null);
            case "s":
                length = BaseParser.nextInteger(inputStream, ':');
                return new PHPObject<>(BaseParser.nextString(inputStream, ';'), typeStr, length);
            case "i":
                return new PHPObject<>(BaseParser.nextInteger(inputStream, ';'), typeStr, null);
            case "d":
                return new PHPObject<>(BaseParser.nextDouble(inputStream, ';'), typeStr, null);
            case "a":
                length = BaseParser.nextInteger(inputStream, ':');
                return new PHPObject<>(BaseParser.nextArray(inputStream), typeStr, length);
            default:
                throw new UnknownPHPObjectType("Don't know how to decode type: " + typeStr);
        }
    }
}
