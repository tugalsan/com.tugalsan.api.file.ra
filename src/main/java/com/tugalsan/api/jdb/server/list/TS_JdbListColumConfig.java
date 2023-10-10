package com.tugalsan.api.jdb.server.list;

import java.util.List;

public class TS_JdbListColumConfig {

    private TS_JdbListColumConfig(List<? extends TS_JdbListColumnBase> types) {
        this.types = types;
    }
    public List<? extends TS_JdbListColumnBase> types;

    public static TS_JdbListColumConfig of(List<? extends TS_JdbListColumnBase> types) {
        return new TS_JdbListColumConfig(types);
    }

    public int byteSize() {
        return types.stream().mapToInt(type -> type.byteSize()).sum();
    }
}
