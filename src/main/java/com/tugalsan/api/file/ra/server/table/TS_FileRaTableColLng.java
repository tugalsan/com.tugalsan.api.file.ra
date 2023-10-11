package com.tugalsan.api.file.ra.server.table;

public class TS_FileRaTableColLng extends TS_FileRaTableColBase {

    @Override
    public int byteSize() {
        return 8;
    }

    private TS_FileRaTableColLng(long value) {
        this.value = value;
    }

    public static TS_FileRaTableColLng of(long value) {
        return new TS_FileRaTableColLng(value);
    }

    public volatile long value = 0;
}
