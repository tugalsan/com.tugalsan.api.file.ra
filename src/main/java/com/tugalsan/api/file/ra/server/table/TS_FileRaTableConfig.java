package com.tugalsan.api.file.ra.server.table;

import java.util.List;

public class TS_FileRaTableConfig {

    private TS_FileRaTableConfig(List<? extends TS_FileRaTableColBase> types) {
        this.types = types;
    }
    public List<? extends TS_FileRaTableColBase> types;

    public static TS_FileRaTableConfig of(List<? extends TS_FileRaTableColBase> types) {
        return new TS_FileRaTableConfig(types);
    }

    public static TS_FileRaTableConfig of(TS_FileRaTableColBase... types) {
        return new TS_FileRaTableConfig(List.of(types));
    }

    public int byteSize() {
        return types.stream().mapToInt(type -> type.byteSize()).sum();
    }
}
