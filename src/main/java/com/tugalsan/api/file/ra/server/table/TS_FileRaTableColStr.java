package com.tugalsan.api.file.ra.server.table;

import com.tugalsan.api.bytes.client.TGS_ByteLengthUtils;

public class TS_FileRaTableColStr extends TS_FileRaTableColBase {

    @Override
    public int byteSize() {
        return byteSize;
    }

    public TS_FileRaTableColStr(int byteSize) {
        this.byteSize = byteSize;
    }
    final private int byteSize;

    public static TS_FileRaTableColStr of(int byteSize) {
        return new TS_FileRaTableColStr(byteSize);
    }

    private volatile String value = "";

    public String get() {
        return value;
    }

    public void set_cropIfNotProper(String newValue) {
        if (!properIs(newValue)) {
            newValue = properMake(newValue);
        }
        this.value = newValue;
    }

    @Deprecated //WARNING MAY NOT SET IF NOT PROPER!
    public boolean set(String newValue) {
        if (!properIs(newValue)) {
            return false;
        }
        this.value = newValue;
        return true;
    }

    public boolean properIs(String newValue) {
        return newValue != null && TGS_ByteLengthUtils.typeStringUTF8(newValue) <= byteSize;
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
