package com.addrapp;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.util.ArrayList;

public class WritableStringArray implements WritableArray {

    private ArrayList<String> result = new ArrayList<String>();

    @Override
    public void pushNull() {

    }

    @Override
    public void pushBoolean(boolean value) {

    }

    @Override
    public void pushDouble(double value) {

    }

    @Override
    public void pushInt(int value) {

    }

    @Override
    public void pushString(String value) {
        result.add(value);
    }

    @Override
    public void pushArray(WritableArray array) {

    }

    @Override
    public void pushMap(WritableMap map) {

    }

    @Override
    public int size() {
        return result.size();
    }

    @Override
    public boolean isNull(int index) {
        return false;
    }

    @Override
    public boolean getBoolean(int index) {
        return false;
    }

    @Override
    public double getDouble(int index) {
        return 0;
    }

    @Override
    public int getInt(int index) {
        return 0;
    }

    @Override
    public String getString(int index) {
        return result.get(index);
    }

    @Override
    public ReadableArray getArray(int index) {
        return null;
    }

    @Override
    public ReadableMap getMap(int index) {
        return null;
    }

    @Override
    public Dynamic getDynamic(int index) {
        return null;
    }

    @Override
    public ReadableType getType(int index) {
        return null;
    }

    @Override
    public ArrayList<Object> toArrayList() {
        ArrayList<Object> lst = new ArrayList<Object>(result);
        return lst;
    }
}
