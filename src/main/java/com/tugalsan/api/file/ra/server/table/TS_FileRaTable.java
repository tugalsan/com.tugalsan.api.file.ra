package com.tugalsan.api.file.ra.server.table;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.file.ra.server.simple.TS_FileRaSimple;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.optional.client.TGS_Optional;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
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

    public long rowSize() {
        if (!TS_FileUtils.isExistFile(path())) {
            return 0L;
        }
        var sizeInBytes_db = TS_FileUtils.getFileSizeInBytes(path());
        var sizeInBytes_row = template.byteSize();
        return sizeInBytes_db / sizeInBytes_row;
    }

    private long position(long idx) {
        var pos = idx * template.byteSize();
        d.ci("position", pos);
        return pos;
    }

    public TGS_Optional<List<TS_FileRaTableCellBase>> rowGet(long idx) {
        List<TS_FileRaTableCellBase> lst = new ArrayList();
        var position = position(idx);
        for (var i = 0; i < template.columns.size(); i++) {
            var colConfig_emptyRowI = template.columns.get(i);
            if (colConfig_emptyRowI instanceof TS_FileRaTableCellDbl templateDbl) {
                var value = simple.getDoubleFromPostion(position).orThrowFirstInfo();
                lst.add(templateDbl.toValue(value));
                position += templateDbl.byteSize();
                d.ci("rowGet", "i", i, "pos", position);
            } else if (colConfig_emptyRowI instanceof TS_FileRaTableCellLng templateLng) {
                var value = simple.getLongFromPostion(position).orThrowFirstInfo();
                lst.add(templateLng.toValue(value));
                position += templateLng.byteSize();
                d.ci("rowGet", "i", i, "pos", position);
            } else if (colConfig_emptyRowI instanceof TS_FileRaTableCellStr templateStr) {
                var value = simple.getStringFromPostion(position).orThrowFirstInfo();
                lst.add(templateStr.toValue_cropIfNotProper(value));
                position += templateStr.byteSize();
                d.ci("rowGet", "i", i, "pos", position);
            } else {
                TGS_Optional.ofEmpty("ERROR @ TS_JdbList.rowGet: unkwon col type");
            }
        }
        return TGS_Optional.of(lst);
    }

    public TGS_Optional<Boolean> rowIsEmpty(long idx) {
        var rowOp = rowGet(idx);
        if (rowOp.payload.isEmpty()) {
            return rowOp.toEmptyFirstInfoOr("ERROR @ TS_FileRaTable.rowIsEmpty: rowOp.info.isEmpty()");
        }
        return TGS_Optional.of(template.rowIsEmpty(rowOp.payload.get()));
    }

    public Exception rowSetEmpty(long idx) {
        return rowSet(idx, template.rowCreateEmpty());
    }

    public Exception rowSet(long idx, TS_FileRaTableCellBase... rowValues) {
        return rowSet(idx, List.of(rowValues));
    }

    public Exception rowSet(long idx, List<? extends TS_FileRaTableCellBase> newRow) {
        return TGS_UnSafe.call(() -> {
            var position = position(idx);
            for (var i = 0; i < template.columns.size(); i++) {
                var colConfig_emptyRowI = template.columns.get(i);
                var newRowValueI = newRow.get(i);
                if (!Objects.equals(newRowValueI.getClass(), colConfig_emptyRowI.getClass())) {
                    throw new RuntimeException("ERROR @ TS_JdbList.rowSet: !Objects.equals(newRowValueI.getClass(), colConfig_emptyRowI.getClass())");
                }
                if (newRowValueI instanceof TS_FileRaTableCellDbl valueDbl) {
                    position = simple.setDoubleFromPostion_calcNextPosition(position, valueDbl.get()).orThrowFirstInfo();
                    d.ci("rowSet", "i", i, "pos", position);
                } else if (newRowValueI instanceof TS_FileRaTableCellLng valueDbl) {
                    position = simple.setLongFromPostion_calcNextPosition(position, valueDbl.get()).orThrowFirstInfo();
                    d.ci("rowSet", "i", i, "pos", position);
                } else if (newRowValueI instanceof TS_FileRaTableCellStr valueStr) {
                    var emptyValue = (TS_FileRaTableCellStr) colConfig_emptyRowI;
                    if (emptyValue.byteSize() != valueStr.byteSize()) {
                        throw new RuntimeException("ERROR @ TS_JdbList.rowSet: emptyValue.byteSize() != newRowCell.byteSize()");
                    }
                    simple.setStringFromPostion_calcNextPosition(position, valueStr.get()).orThrowFirstInfo();
                    position += valueStr.byteSize();
                    d.ci("rowSet", "i", i, "pos", position);
                } else {
                    throw new RuntimeException("ERROR @ TS_JdbList.rowSet: unkwon col type");
                }
            }
            return null;
        }, e -> e);
    }
}
