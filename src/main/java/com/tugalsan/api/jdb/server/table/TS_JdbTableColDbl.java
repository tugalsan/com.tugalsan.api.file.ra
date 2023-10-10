package com.tugalsan.api.jdb.server.table;

public class TS_JdbTableColDbl extends TS_JdbTableColBase {

    @Override
    public int byteSize() {
        return 8;
    }

    private TS_JdbTableColDbl(double value) {
        this.value = value;
    }

    public static TS_JdbTableColDbl of(double value) {
        return new TS_JdbTableColDbl(value);
    }

    public volatile double value = 0;
}
