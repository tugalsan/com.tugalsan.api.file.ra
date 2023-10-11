package com.tugalsan.api.file.ra.server.table;

import java.util.List;

public class TS_FileRaTableConfig {

    private TS_FileRaTableConfig(List<? extends TS_FileRaTableCellBase> emptyRow) {
        this.emptyRow = emptyRow;
    }
    public List<? extends TS_FileRaTableCellBase> emptyRow;

    public static TS_FileRaTableConfig of(List<? extends TS_FileRaTableCellBase> emptyRow) {
        return new TS_FileRaTableConfig(emptyRow);
    }

    public static TS_FileRaTableConfig of(TS_FileRaTableCellBase... emptyRow) {
        return new TS_FileRaTableConfig(List.of(emptyRow));
    }

    public int byteSize() {
        return emptyRow.stream().mapToInt(type -> type.byteSize()).sum();
    }
}
