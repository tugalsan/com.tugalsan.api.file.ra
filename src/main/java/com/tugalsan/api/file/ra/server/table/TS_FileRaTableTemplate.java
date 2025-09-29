package com.tugalsan.api.file.ra.server.table;

import module com.tugalsan.api.stream;
import java.util.*;

public class TS_FileRaTableTemplate {

    private TS_FileRaTableTemplate(List<? extends TS_FileRaTableCellBase> template) {
        this.columns = template;
    }
    public List<? extends TS_FileRaTableCellBase> columns;

    public static TS_FileRaTableTemplate of(List<? extends TS_FileRaTableCellBase> template) {
        return new TS_FileRaTableTemplate(template);
    }

    public static TS_FileRaTableTemplate of(TS_FileRaTableCellBase... template) {
        return new TS_FileRaTableTemplate(List.of(template));
    }

    public int byteSize() {
        return columns.stream().mapToInt(type -> type.byteSize()).sum();
    }

    public List<TS_FileRaTableCellBase> rowCreateEmpty() {
        return TGS_StreamUtils.toLst(columns.stream().map(template -> {
            switch (template) {
                case TS_FileRaTableCellDbl templateDbl -> {
                    return templateDbl.toValueEmpty();
                }
                case TS_FileRaTableCellLng templateLng -> {
                    return templateLng.toValueEmpty();
                }
                case TS_FileRaTableCellStr templateStr -> {
                    return templateStr.toValueEmpty();
                }
                default ->
                    throw new RuntimeException("ERROR @ TS_JdbList.rowNew: unkwon col type");
            }
        }));
    }

    public boolean rowIsEmpty(List<? extends TS_FileRaTableCellBase> rowValues) {
        return rowValues.stream().noneMatch(value -> !value.isEmpty());
    }
}
