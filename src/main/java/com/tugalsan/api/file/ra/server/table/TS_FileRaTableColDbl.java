package com.tugalsan.api.file.ra.server.table;

public class TS_FileRaTableColDbl extends TS_FileRaTableColBase {

    @Override
    public int byteSize() {
        return 8;
    }

    private TS_FileRaTableColDbl(double value) {
        this.value = value;
    }

    public static TS_FileRaTableColDbl of(double value) {
        return new TS_FileRaTableColDbl(value);
    }

    public volatile double value = 0;
}
