package com.tugalsan.api.file.ra.server.table;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.file.ra.server.simple.TS_FileRaSimple;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.union.client.TGS_Union;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TS_FileRaTable {

    final private static TS_Log d = TS_Log.of(false, TS_FileRaTable.class);

    private TS_FileRaTable(Path path, TS_FileRaTableTemplate colConfig) {
        this.simple = TS_FileRaSimple.of(path);
        this.template = colConfig;
    }
    final private TS_FileRaSimple simple;
    final public TS_FileRaTableTemplate template;

    public static TS_FileRaTable of(Path path, TS_FileRaTableTemplate colConfig) {
        return new TS_FileRaTable(path, colConfig);
    }

    public static TS_FileRaTable of(Path path, TS_FileRaTableCellBase... types) {
        return new TS_FileRaTable(path, TS_FileRaTableTemplate.of(types));
    }

    public Path path() {
        return simple.path;
    }

    public TGS_Union<Long> rowSize() {
        if (!TS_FileUtils.isExistFile(path())) {
            return TGS_Union.of(0L);
        }
        var u_sizeInBytes_db = TS_FileUtils.getFileSizeInBytes(path());
        if (u_sizeInBytes_db.isExcuse()) {
            return u_sizeInBytes_db;
        }
        var sizeInBytes_db = u_sizeInBytes_db.value();
        var sizeInBytes_row = template.byteSize();
        return TGS_Union.of(sizeInBytes_db / sizeInBytes_row);
    }

    private long position(long idx) {
        var pos = idx * template.byteSize();
        d.ci("position", pos);
        return pos;
    }

    public TGS_Union<List<TS_FileRaTableCellBase>> rowGet(long idx) {
        List<TS_FileRaTableCellBase> lst = new ArrayList();
        var position = position(idx);
        for (var i = 0; i < template.columns.size(); i++) {
            var colConfig_emptyRowI = template.columns.get(i);
            switch (colConfig_emptyRowI) {
                case TS_FileRaTableCellDbl templateDbl -> {
                    var u_value = simple.getDoubleFromPostion(position);
                    if (u_value.isExcuse()) {
                        return u_value.toExcuse();
                    }
                    var value = u_value.value();
                    lst.add(templateDbl.toValue(value));
                    position += templateDbl.byteSize();
                    d.ci("rowGet", "i", i, "pos", position);
                }
                case TS_FileRaTableCellLng templateLng -> {
                    var u_value = simple.getLongFromPostion(position);
                    if (u_value.isExcuse()) {
                        return u_value.toExcuse();
                    }
                    var value = u_value.value();
                    lst.add(templateLng.toValue(value));
                    position += templateLng.byteSize();
                    d.ci("rowGet", "i", i, "pos", position);
                }
                case TS_FileRaTableCellStr templateStr -> {
                    var u_value = simple.getStringFromPostion(position);
                    if (u_value.isExcuse()) {
                        return u_value.toExcuse();
                    }
                    var value = u_value.value();
                    lst.add(templateStr.toValue_cropIfNotProper(value));
                    position += templateStr.byteSize();
                    d.ci("rowGet", "i", i, "pos", position);
                }
                default -> {
                    return TGS_Union.ofExcuse(d.className, "rowGet", "ERROR @ TS_JdbList.rowGet: unkwon col type");
                }
            }
        }
        return TGS_Union.of(lst);
    }

    public TGS_Union<Boolean> rowIsEmpty(long idx) {
        var rowOp = rowGet(idx);
        if (rowOp.isExcuse()) {
            return TGS_Union.ofExcuse(d.className, "rowIsEmpty", "ERROR @ TS_FileRaTable.rowIsEmpty: rowOp.info.isEmpty()");
        }
        return TGS_Union.of(template.rowIsEmpty(rowOp.value()));
    }

    public TGS_UnionExcuse rowSetEmpty(long idx) {
        return rowSet(idx, template.rowCreateEmpty());
    }

    public TGS_UnionExcuse rowSet(long idx, TS_FileRaTableCellBase... rowValues) {
        return rowSet(idx, List.of(rowValues));
    }

    public TGS_UnionExcuse rowSet(long idx, List<? extends TS_FileRaTableCellBase> newRow) {
        var position = position(idx);
        for (var i = 0; i < template.columns.size(); i++) {
            var colConfig_emptyRowI = template.columns.get(i);
            var newRowValueI = newRow.get(i);
            if (!Objects.equals(newRowValueI.getClass(), colConfig_emptyRowI.getClass())) {
                return TGS_UnionExcuse.ofExcuse(d.className, "rowSet", "ERROR @ TS_JdbList.rowSet: !Objects.equals(newRowValueI.getClass(), colConfig_emptyRowI.getClass())");
            }
            switch (newRowValueI) {
                case TS_FileRaTableCellDbl valueDbl -> {
                    var u_position = simple.setDoubleFromPostion_calcNextPosition(position, valueDbl.get());
                    if (u_position.isExcuse()) {
                        return TGS_UnionExcuse.ofExcuse(d.className, "rowSet", "TS_FileRaTableCellDbl->" + u_position.excuse().getMessage());
                    }
                    position = u_position.value();
                    d.ci("rowSet", "i", i, "pos", position);
                }
                case TS_FileRaTableCellLng valueLng -> {
                    var u_position = simple.setDoubleFromPostion_calcNextPosition(position, valueLng.get());
                    if (u_position.isExcuse()) {
                        return TGS_UnionExcuse.ofExcuse(d.className, "rowSet", "TS_FileRaTableCellLng->" + u_position.excuse().getMessage());
                    }
                    position = u_position.value();
                    d.ci("rowSet", "i", i, "pos", position);
                }
                case TS_FileRaTableCellStr valueStr -> {
                    var emptyValue = (TS_FileRaTableCellStr) colConfig_emptyRowI;
                    if (emptyValue.byteSize() != valueStr.byteSize()) {
                        return TGS_UnionExcuse.ofExcuse(d.className, "rowSet", "ERROR @ TS_JdbList.rowSet: emptyValue.byteSize() != newRowCell.byteSize()");
                    }
                    var u = simple.setStringFromPostion_calcNextPosition(position, valueStr.get());
                    if (u.isExcuse()) {
                        return TGS_UnionExcuse.ofExcuse(d.className, "rowSet", "TS_FileRaTableCellStr->" + u.excuse().getMessage());
                    }
                    position += valueStr.byteSize();
                    d.ci("rowSet", "i", i, "pos", position);
                }
                default ->
                    throw new RuntimeException("ERROR @ TS_JdbList.rowSet: unkwon col type");
            }
        }
        return TGS_UnionExcuse.ofVoid();
    }
}
