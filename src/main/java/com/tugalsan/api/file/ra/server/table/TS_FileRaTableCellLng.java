package com.tugalsan.api.file.ra.server.table;

public class TS_FileRaTableCellLng implements TS_FileRaTableCellBase {

    @Override
    public int byteSize() {
        return 8;
    }

    private TS_FileRaTableCellLng(long value, boolean isValue) {
        this.value = value;
        this.isValue = isValue;
    }
    private volatile long value;
    final public boolean isValue;

    @Override
    public boolean isEmpty() {
        return value == 0;
    }

    public static TS_FileRaTableCellLng ofTemplate() {
        return new TS_FileRaTableCellLng(0, false);
    }

    public TS_FileRaTableCellLng toValue(long value) {
        return new TS_FileRaTableCellLng(value, true);
    }

    public TS_FileRaTableCellLng toValueEmpty() {
        return new TS_FileRaTableCellLng(0, true);
    }

    public boolean set(long value) {
        if (!isValue) {
            return false;
        }
        this.value = value;
        return true;
    }

    public long get() {
        return value;
    }

    @Override
    public String toString() {
        return TS_FileRaTableCellLng.class.getSimpleName() + "{" + "value=" + value + '}';
    }
}
