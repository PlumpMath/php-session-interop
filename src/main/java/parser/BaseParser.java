package parser;


import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import exceptions.InvalidSessionEntryException;
import exceptions.ParseException;
import exceptions.UnknownPHPObjectType;
import parser.objects.PHPObject;
import parser.util.ClosureState;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class BaseParser {

    public static String nextWord(ByteArrayInputStream inputStream, Character terminator) throws ParseException {
        StringBuilder word = new StringBuilder(256);

        int c;
        boolean foundTerminator = false;

        while((c = inputStream.read()) != -1) {
            if(c == terminator) {
                foundTerminator = true;
                break;
            } else {
                word.append((char)c);
            }
        }

        if(c == -1 && word.length() == 0) {
            return null;
        }

        if(!foundTerminator) {
            throw new ParseException("Missing terminating '" + terminator  + "' token.");
        }

        return word.toString();
    }

    public static String nextClosure(ByteArrayInputStream inputStream) throws ParseException {

        int character;
        StringBuilder closure = new StringBuilder(1000);
        ClosureState state = ClosureState.OPEN;
        int openSubClosures = 0;

        while((character = inputStream.read()) != -1) {
            char c = (char)character;
            if(c == '{') {
                if(state == ClosureState.OPEN) {
                    state = ClosureState.BODY;
                } else {
                    openSubClosures++;
                    closure.append(c);
                }
            } else if(c == '}') {
                if(state == ClosureState.BODY) {
                    if(openSubClosures == 0) {
                        state = ClosureState.CLOSE;
                        break;
                    } else {
                        openSubClosures--;
                        closure.append(c);
                    }
                }
            } else {
                closure.append(c);
            }
        }

        if(state != ClosureState.CLOSE) {
            throw new ParseException("Missing terminating '}' token.");
        }

        return closure.toString();
    }

    public static Boolean nextBoolean(ByteArrayInputStream inputStream, char c) throws ParseException {
        String word = BaseParser.nextWord(inputStream, c);

        if(word == null) {
            return null;
        }

        return word.equals("1");
    }

    public static String nextString(ByteArrayInputStream inputStream, char c) throws ParseException {
        String word = BaseParser.nextWord(inputStream, c);

        if(word == null) {
            return null;
        }

        return word.substring(1, word.length() - 1);
    }

    public static Integer nextInteger(ByteArrayInputStream inputStream, char c) throws ParseException {
        String word = BaseParser.nextWord(inputStream, c);

        if(word == null) {
            return null;
        }

        return Integer.parseInt(word);
    }

    public static Double nextDouble(ByteArrayInputStream inputStream, char c) throws ParseException {
        String word = BaseParser.nextWord(inputStream, c);

        if(word == null) {
            return null;
        }

        return Double.parseDouble(word);
    }

    public static LinkedHashMap<PHPObject<?>, PHPObject<?>> nextArray(ByteArrayInputStream inputStream) throws ParseException, UnknownPHPObjectType {

        LinkedHashMap<PHPObject<?>, PHPObject<?>> theArray = new LinkedHashMap<>();
        String closure = BaseParser.nextClosure(inputStream);
        ByteArrayInputStream inStream = new ByteArrayInputStream(closure.getBytes());

        PHPObject<?> key = PHPObjectParser.nextObject(inStream);
        while(key != null) {
            theArray.put(key, PHPObjectParser.nextObject(inStream));
            key = PHPObjectParser.nextObject(inStream);
        }

        return theArray;
    }
}
