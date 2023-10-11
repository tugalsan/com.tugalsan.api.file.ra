package com.tugalsan.api.file.ra.server.table;

public class TS_FileRaTableCellDbl extends TS_FileRaTableCellBase {

    @Override
    public int byteSize() {
        return 8;
    }

    private TS_FileRaTableCellDbl(double value) {
        this.value = value;
    }

    public static TS_FileRaTableCellDbl ofEmpty() {
        return new TS_FileRaTableCellDbl(0);
    }

    public TS_FileRaTableCellDbl set(double value) {
        this.value = value;
        return this;
    }

    public double get() {
        return value;
    }

    private volatile double value;
}
