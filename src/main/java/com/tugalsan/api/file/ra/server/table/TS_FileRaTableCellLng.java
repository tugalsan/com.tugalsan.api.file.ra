package com.tugalsan.api.file.ra.server.table;

public class TS_FileRaTableCellLng extends TS_FileRaTableCellBase {

    @Override
    public int byteSize() {
        return 8;
    }

    private TS_FileRaTableCellLng(long value) {
        this.value = value;
    }

    public static TS_FileRaTableCellLng ofEmpty() {
        return new TS_FileRaTableCellLng(0);
    }

    public TS_FileRaTableCellLng set(long value) {
        this.value = value;
        return this;
    }

    public long get() {
        return value;
    }

    private volatile long value;

    @Override
    public String toString() {
        return TS_FileRaTableCellLng.class.getSimpleName() + "{" + "value=" + value + '}';
    }
}
