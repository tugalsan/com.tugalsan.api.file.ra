package com.tugalsan.api.jdb.server.list;

public class TS_JdbListColDbl extends TS_JdbListColumnBase {

    @Override
    public int byteSize() {
        return 8;
    }

    private TS_JdbListColDbl(double value) {
        this.value = value;
    }

    public static TS_JdbListColDbl of(double value) {
        return new TS_JdbListColDbl(value);
    }

    public volatile double value = 0;
}
