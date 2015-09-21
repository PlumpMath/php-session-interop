import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import exceptions.InvalidSessionEntryException;
import exceptions.UnknownPHPObjectType;
import models.sessions.BasicSmatteringSession;
import models.testObjects.BasicSmatteringObjectC;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class DeserializeTests {

    @Test
    public void deserializeBasicSession() {

        // Test deserialization on a simple smattering of different basic types
        String testInput = "test_bool|b:1;tEsTsTrInG|s:4:\"sica\";TestNum|i:34;testArray|a:5:{i:0;s:1:\"a\";i:1;" +
                "i:555;i:2;d:1.05;i:3;d:5.0000000000000003E-10;i:4;a:3:{i:0;s:6:\"nested\";i:1;s:5:" +
                "\"nelly\";i:2;a:4:{i:0;i:1;i:1;i:2;s:5:\"pizza\";a:1:{s:5:\"sauce\";b:0;}i:2;a:2:{s:3:" +
                "\"abc\";i:123;s:3:\"zyx\";i:321;}}}}";

        PHPSessionRepresentationDeserializer<BasicSmatteringSession> deserializer = new PHPSessionRepresentationDeserializer();
        deserializer.registerType(BasicSmatteringObjectC.class);

        BasicSmatteringSession session = null;
        try {
            session = deserializer.deserialize(new ByteArrayInputStream(testInput.getBytes()),
                    BasicSmatteringSession.class);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Deserialization failed.");
        }

        Assert.assertTrue(session.getTestBool());
        Assert.assertEquals(session.getTestString(), "sica");
        Assert.assertEquals(session.getTestNumber(), (Integer)34);


        ArrayList<Object> objs = session.getTestArray();
        Assert.assertEquals(objs.get(0), "a");
        Assert.assertEquals(objs.get(1), 555);
        Assert.assertEquals(objs.get(2), 1.05);
        Assert.assertEquals(objs.get(3), 5E-10);

        ArrayList<Object> nested = (ArrayList<Object>) objs.get(4);
        Assert.assertEquals(nested.get(0), "nested");
        Assert.assertEquals(nested.get(1), "nelly");

        LinkedHashMap<Object, Object> nested2 = (LinkedHashMap<Object, Object>) nested.get(2);
        Assert.assertEquals(nested2.get(0), 1);
        Assert.assertEquals(nested2.get(1), 2);

        LinkedHashMap<Object, Object> nested3 = (LinkedHashMap<Object, Object>) nested2.get("pizza");
        Assert.assertFalse((Boolean)nested3.get("sauce"));

        LinkedHashMap<Object, Object> nested4 = (LinkedHashMap<Object, Object>) nested2.get(2);
        Assert.assertEquals(nested4.get("zyx"), 321);
        Assert.assertEquals(nested4.get("abc"), 123);
    }
}
