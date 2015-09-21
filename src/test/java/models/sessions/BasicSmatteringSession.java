package models.sessions;

import parser.annotations.SerializeName;

import java.util.ArrayList;

public class BasicSmatteringSession {

    @SerializeName("test_bool")
    private Boolean testBool;

    @SerializeName("tEsTsTrInG")
    private String testString;

    @SerializeName("TestNum")
    private Integer testNumber;

    private ArrayList<Object> testArray;

    public BasicSmatteringSession() {
    }

    public Boolean getTestBool() {
        return testBool;
    }

    public void setTestBool(Boolean testBool) {
        this.testBool = testBool;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public Integer getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(Integer testNumber) {
        this.testNumber = testNumber;
    }

    public ArrayList<Object> getTestArray() {
        return testArray;
    }

    public void setTestArray(ArrayList<Object> testArray) {
        this.testArray = testArray;
    }
}
