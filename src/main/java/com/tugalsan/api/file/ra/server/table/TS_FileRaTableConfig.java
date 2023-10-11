package com.tugalsan.api.file.ra.server.table;

import java.util.List;

public class TS_FileRaTableConfig {

    private TS_FileRaTableConfig(List<? extends TS_FileRaTableCellBase> template) {
        this.template = template;
    }
    public List<? extends TS_FileRaTableCellBase> template;

    public static TS_FileRaTableConfig of(List<? extends TS_FileRaTableCellBase> template) {
        return new TS_FileRaTableConfig(template);
    }

    public static TS_FileRaTableConfig of(TS_FileRaTableCellBase... template) {
        return new TS_FileRaTableConfig(List.of(template));
    }

    public int byteSize() {
        return template.stream().mapToInt(type -> type.byteSize()).sum();
    }
}
