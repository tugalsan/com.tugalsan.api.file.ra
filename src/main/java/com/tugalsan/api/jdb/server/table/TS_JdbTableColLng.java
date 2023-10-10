package com.tugalsan.api.jdb.server.table;

public class TS_JdbTableColLng extends TS_JdbTableColBase {

    @Override
    public int byteSize() {
        return 8;
    }

    private TS_JdbTableColLng(long value) {
        this.value = value;
    }

    public static TS_JdbTableColLng of(long value) {
        return new TS_JdbTableColLng(value);
    }

    public volatile long value = 0;
}
