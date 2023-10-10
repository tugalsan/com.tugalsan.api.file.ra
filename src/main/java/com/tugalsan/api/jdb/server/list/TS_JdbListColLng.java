package com.tugalsan.api.jdb.server.list;

public class TS_JdbListColLng extends TS_JdbListColumnBase {

    @Override
    public int byteSize() {
        return 8;
    }

    private TS_JdbListColLng(long value) {
        this.value = value;
    }

    public static TS_JdbListColLng of(long value) {
        return new TS_JdbListColLng(value);
    }

    public volatile long value = 0;
}
