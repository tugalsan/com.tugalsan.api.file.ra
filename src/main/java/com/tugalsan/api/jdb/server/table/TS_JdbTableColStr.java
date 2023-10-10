package com.tugalsan.api.jdb.server.table;

import com.tugalsan.api.bytes.client.TGS_ByteLengthUtils;

public class TS_JdbTableColStr extends TS_JdbTableColBase {

    @Override
    public int byteSize() {
        return byteSize;
    }

    public TS_JdbTableColStr(int byteSize) {
        this.byteSize = byteSize;
    }
    final private int byteSize;

    public static TS_JdbTableColStr of(int byteSize) {
        return new TS_JdbTableColStr(byteSize);
    }

    private volatile String value = "";

    public String get() {
        return value;
    }

    public boolean set(String newValue) {
        if (!properIs(newValue)) {
            return false;
        }
        this.value = newValue;
        return true;
    }

    public boolean properIs(String newValue) {
        return newValue != null && TGS_ByteLengthUtils.typeStringUTF16(newValue) > byteSize;
    }

    public String properMake(String newValue) {
        if (newValue == null) {
            return "";
        }
        if (!properIs(newValue)) {
            return newValue.substring(0, Math.min(newValue.length(), byteSize / 2));
        }
        return newValue;
    }
}
