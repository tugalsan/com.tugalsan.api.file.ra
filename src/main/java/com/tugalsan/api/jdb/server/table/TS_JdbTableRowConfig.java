package com.tugalsan.api.jdb.server.table;

import java.util.List;

public class TS_JdbTableRowConfig {

    private TS_JdbTableRowConfig(List<? extends TS_JdbTableColBase> types) {
        this.types = types;
    }
    public List<? extends TS_JdbTableColBase> types;

    public static TS_JdbTableRowConfig of(List<? extends TS_JdbTableColBase> types) {
        return new TS_JdbTableRowConfig(types);
    }

    public int byteSize() {
        return types.stream().mapToInt(type -> type.byteSize()).sum();
    }
}
