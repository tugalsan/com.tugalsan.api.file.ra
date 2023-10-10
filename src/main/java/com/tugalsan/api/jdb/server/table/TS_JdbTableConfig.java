package com.tugalsan.api.jdb.server.table;

import java.util.List;

public class TS_JdbTableConfig {

    private TS_JdbTableConfig(List<? extends TS_JdbTableColBase> types) {
        this.types = types;
    }
    public List<? extends TS_JdbTableColBase> types;

    public static TS_JdbTableConfig of(List<? extends TS_JdbTableColBase> types) {
        return new TS_JdbTableConfig(types);
    }

    public static TS_JdbTableConfig of(TS_JdbTableColBase... types) {
        return new TS_JdbTableConfig(List.of(types));
    }

    public int byteSize() {
        return types.stream().mapToInt(type -> type.byteSize()).sum();
    }
}
