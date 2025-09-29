package com.tugalsan.api.file.ra.server.table;

import module com.tugalsan.api.bytes;

public class TS_FileRaTableCellStr implements TS_FileRaTableCellBase {

    @Override
    public int byteSize() {
        return byteSize;
    }

    public TS_FileRaTableCellStr(int byteSize, String value, boolean isValue) {
        this.byteSize = byteSize;
        this.value = value;
        this.isValue = isValue;
    }
    final private int byteSize;
    private volatile String value;
    final public boolean isValue;

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    public static TS_FileRaTableCellStr ofTemplate(int byteSize) {
        return new TS_FileRaTableCellStr(byteSize, "", false);
    }

    public TS_FileRaTableCellStr toValue_cropIfNotProper(String value) {
        return new TS_FileRaTableCellStr(byteSize, properMake(value), true);
    }

    public TS_FileRaTableCellStr toValueEmpty() {
        return new TS_FileRaTableCellStr(byteSize, "", true);
    }

    public String get() {
        return value;
    }

    public boolean set_cropIfNotProper(String newValue) {
        if (!isValue) {
            return false;
        }
        if (!properIs(newValue)) {
            newValue = properMake(newValue);
        }
        this.value = newValue;
        return true;
    }

    public boolean properIs(String newValue) {
        return newValue != null && TGS_ByteLengthUtils.typeStringUTF8(newValue) <= byteSize;
    }

    public String properMake(String newValue) {
        if (newValue == null) {
            return "";
        }
        if (!properIs(newValue)) {
            return newValue.substring(0, Math.min(newValue.length(), byteSize / 2));
        }
        return newValue;
    }

    @Override
    public String toString() {
        return TS_FileRaTableCellStr.class.getSimpleName() + "{" + "value=" + value + " , byteSize=" + byteSize + '}';
    }
}
