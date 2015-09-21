import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import exceptions.InvalidSessionEntryException;
import exceptions.UnknownPHPObjectType;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.PHPSessionEntry;
import parser.PHPSessionEntryParser;
import parser.annotations.SerializeName;
import parser.objects.PHPObject;
import parser.util.ParserUtil;

import java.io.ByteArrayInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class PHPSessionRepresentationDeserializer<T> {

    final Logger logger = LoggerFactory.getLogger(PHPSessionRepresentationDeserializer.class);

    private ArrayList<Class> registeredTypes = new ArrayList<Class>();

    public PHPSessionRepresentationDeserializer() {}

    public void registerType(Class c) {
        registeredTypes.add(c);
    }

    private String getSerializeName(Method m, Class c) {
        String methodName = m.getName().substring(3);
        String fieldName = WordUtils.uncapitalize(methodName);

        try {
            Field f = c.getDeclaredField(fieldName);
            for(Annotation annotation : f.getDeclaredAnnotations()) {
                if(annotation instanceof SerializeName) {
                    return ((SerializeName) annotation).value();
                }
            }
        } catch (NoSuchFieldException e) {}

        return fieldName;
    }

    private Method findSetterForField(Class theClass, String field) {
        for(Method m : theClass.getDeclaredMethods()) {
            try {
                String prefix = m.getName().substring(0, 3);
                Boolean isSetter = prefix.equals("set");

                if(getSerializeName(m, theClass).equals(field)) {
                    if(isSetter) {
                        return m;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                // Not the correct method.
            }
        }

        return null;
    }

    public T deserialize(ByteArrayInputStream input, Class theClass) throws InvalidSessionEntryException, UnknownPHPObjectType, IllegalAccessException, InstantiationException {
        PHPSessionEntryParser parser = new PHPSessionEntryParser(input);
        PHPSessionEntry entry;

        T dSerializeClass = (T)theClass.newInstance();

        while((entry = parser.nextEntry()) != null) {
            Method setter = findSetterForField(theClass, entry.getFieldName());
            PHPObject sessionEntryData = entry.getValue();

            if(sessionEntryData == null) {
                logger.info("Skipping, couldn't decode field: " + entry.getFieldName());
                continue;
            }

            if(setter == null) {
                logger.info("Skipping, no setter found in pojo. Field name: " + entry.getFieldName());
                continue;
            }

            try {
                if(sessionEntryData.getType().equals("a")) {
                    setter.invoke(dSerializeClass, ParserUtil.mapFlatten((LinkedHashMap)sessionEntryData.getValue()));
                } else {
                    setter.invoke(dSerializeClass, sessionEntryData.getValue());
                }
            } catch (InvocationTargetException e) {
                logger.warn("Skipping, invocation of the setter found in pojo crashed. Field name: " + entry.getFieldName(), e);
            } catch (IllegalArgumentException e) {
                logger.warn("Skipping, invocation of the setter found in pojo crashed, wrong type. Field name: " + entry.getFieldName(), e);
            }
        }

        return dSerializeClass;
    }
}
