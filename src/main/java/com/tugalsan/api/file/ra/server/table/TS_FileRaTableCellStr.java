package com.tugalsan.api.file.ra.server.table;

import com.tugalsan.api.bytes.client.TGS_ByteLengthUtils;

public class TS_FileRaTableCellStr extends TS_FileRaTableCellBase {

    @Override
    public int byteSize() {
        return byteSize;
    }

    public TS_FileRaTableCellStr(int byteSize, String value) {
        this.byteSize = byteSize;
        this.value = value;
    }
    final private int byteSize;
    private volatile String value;

    public static TS_FileRaTableCellStr ofEmpty(int byteSize) {
        return new TS_FileRaTableCellStr(byteSize, "");
    }

    public String get() {
        return value;
    }

    public TS_FileRaTableCellStr set_cropIfNotProper(String newValue) {
        if (!properIs(newValue)) {
            newValue = properMake(newValue);
        }
        this.value = newValue;
        return this;
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
