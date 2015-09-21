package parser.util;


import exceptions.ParseException;
import parser.objects.PHPObject;

import java.io.ByteArrayInputStream;
import java.util.*;

public class ParserUtil {


    public static Object mapFlatten(LinkedHashMap<PHPObject<?>, PHPObject<?>> map) {
        int index = 0;
        ArrayList<Object> basicArray = new ArrayList<>();
        LinkedHashMap<Object, Object> complexArray = new LinkedHashMap<>();
        Boolean isBasicArray = true;

        for(PHPObject<?> key : map.keySet()) {
            if(!key.getType().equals("i") || (Integer)key.getValue() != index) {
                isBasicArray = false;
            }
            index++;
        }

        Set<Map.Entry<PHPObject<?>, PHPObject<?>>> entrySet = map.entrySet();
        if(isBasicArray) {
            for(Map.Entry<PHPObject<?>, PHPObject<?>> entry : entrySet) {
                if(entry.getValue().getType().equals("a")) {
                    basicArray.add(mapFlatten((LinkedHashMap<PHPObject<?>, PHPObject<?>>) entry.getValue().getValue()));
                } else {
                    basicArray.add(entry.getValue().getValue());
                }
            }
            return basicArray;
        } else {
            for(Map.Entry<PHPObject<?>, PHPObject<?>> entry : entrySet) {
                if(entry.getValue().getType().equals("a")) {
                    complexArray.put(entry.getKey().getValue(), mapFlatten((LinkedHashMap<PHPObject<?>, PHPObject<?>>) entry.getValue().getValue()));
                } else {
                    complexArray.put(entry.getKey().getValue(), entry.getValue().getValue());
                }
            }
            return complexArray;
        }
    }
}
