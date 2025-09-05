package com.tugalsan.api.file.ra.server.table;

public class TS_FileRaTableCellDbl implements TS_FileRaTableCellBase {

    @Override
    public int byteSize() {
        return 8;
    }

    private TS_FileRaTableCellDbl(double value, boolean isValue) {
        this.value = value;
        this.isValue = isValue;
    }
    private volatile double value;
    final public boolean isValue;
    
    @Override
    public boolean isEmpty(){
        return value == 0;
    }

    public static TS_FileRaTableCellDbl ofTemplate() {
        return new TS_FileRaTableCellDbl(0, false);
    }

    public TS_FileRaTableCellDbl toValue(double value) {
        return new TS_FileRaTableCellDbl(value, true);
    }

    public TS_FileRaTableCellDbl toValueEmpty() {
        return new TS_FileRaTableCellDbl(0, true);
    }

    public boolean set(double value) {
        if (!isValue) {
            return false;
        }
        this.value = value;
        return true;
    }

    public double get() {
        return value;
    }

    @Override
    public String toString() {
        return TS_FileRaTableCellDbl.class.getSimpleName() + "{" + "value=" + value + '}';
    }
}
