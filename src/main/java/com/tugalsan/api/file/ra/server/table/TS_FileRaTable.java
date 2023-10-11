package com.tugalsan.api.file.ra.server.table;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.file.ra.server.simple.TS_FileRaSimple;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TS_FileRaTable {

    final private static TS_Log d = TS_Log.of(false, TS_FileRaTable.class);

    private TS_FileRaTable(Path path, TS_FileRaTableConfig colConfig) {
        this.simple = TS_FileRaSimple.of(path);
        this.colConfig = colConfig;
    }
    final private TS_FileRaSimple simple;
    final public TS_FileRaTableConfig colConfig;

    public static TS_FileRaTable of(Path path, TS_FileRaTableConfig colConfig) {
        return new TS_FileRaTable(path, colConfig);
    }

    public static TS_FileRaTable of(Path path, TS_FileRaTableCellBase... types) {
        return new TS_FileRaTable(path, TS_FileRaTableConfig.of(types));
    }

    public Path path() {
        return simple.path;
    }

    public long rowSize() {
        if (!TS_FileUtils.isExistFile(path())) {
            return 0L;
        }
        var sizeInBytes_db = TS_FileUtils.getFileSizeInBytes(path());
        var sizeInBytes_row = colConfig.byteSize();
        return sizeInBytes_db / sizeInBytes_row;
    }

    private long position(long idx) {
        var pos = idx * colConfig.byteSize();
        d.ci("position", pos);
        return pos;
    }

    public List<TS_FileRaTableCellBase> rowGet(long idx) {
        List<TS_FileRaTableCellBase> lst = new ArrayList();
        var position = position(idx);
        for (var i = 0; i < colConfig.emptyRow.size(); i++) {
            var colConfig_emptyRowI = colConfig.emptyRow.get(i);
            if (colConfig_emptyRowI instanceof TS_FileRaTableCellDbl) {
                var value = simple.getDoubleFromPostion(position).orThrowFirstInfo();
                var rowCell = TS_FileRaTableCellDbl.ofEmpty().set(value);
                lst.add(rowCell);
                position += rowCell.byteSize();
                d.ci("rowGet", "i", i, "pos", position);
            } else if (colConfig_emptyRowI instanceof TS_FileRaTableCellLng) {
                var value = simple.getLongFromPostion(position).orThrowFirstInfo();
                var rowCell = TS_FileRaTableCellLng.ofEmpty().set(value);
                lst.add(rowCell);
                position += rowCell.byteSize();
                d.ci("rowGet", "i", i, "pos", position);
            } else if (colConfig_emptyRowI instanceof TS_FileRaTableCellStr typeStr) {
                var value = simple.getStringFromPostion(position).orThrowFirstInfo();
                var rowCell = TS_FileRaTableCellStr.ofEmpty(typeStr.byteSize()).set_cropIfNotProper(value);
                lst.add(rowCell);
                position += rowCell.byteSize();
                d.ci("rowGet", "i", i, "pos", position);
            } else if (colConfig_emptyRowI instanceof TS_FileRaTableCellStr) {
                throw new RuntimeException("ERROR @ TS_JdbList.rowGet: unkwon col type");
            }
        }
        return lst;
    }

    public Exception rowSet(long idx, TS_FileRaTableCellBase... rowValues) {
        return rowSet(idx, List.of(rowValues));
    }

    public Exception rowSet(long idx, List<? extends TS_FileRaTableCellBase> newRow) {
        return TGS_UnSafe.call(() -> {
            var position = position(idx);
            for (var i = 0; i < colConfig.emptyRow.size(); i++) {
                var colConfig_emptyRowI = colConfig.emptyRow.get(i);
                var newRowValueI = newRow.get(i);
                if (!Objects.equals(newRowValueI.getClass(), colConfig_emptyRowI.getClass())) {
                    throw new RuntimeException("ERROR @ TS_JdbList.rowGet: !Objects.equals(newRowValueI.getClass(), colConfig_emptyRowI.getClass())");
                }
                if (newRowValueI instanceof TS_FileRaTableCellDbl newRowCell) {
                    position = simple.setDoubleFromPostion_calcNextPosition(position, newRowCell.get()).orThrowFirstInfo();
                    d.ci("rowSet", "i", i, "pos", position);
                } else if (newRowValueI instanceof TS_FileRaTableCellLng newRowCell) {
                    position = simple.setLongFromPostion_calcNextPosition(position, newRowCell.get()).orThrowFirstInfo();
                    d.ci("rowSet", "i", i, "pos", position);
                } else if (newRowValueI instanceof TS_FileRaTableCellStr newRowCell) {
                    var emptyValue = (TS_FileRaTableCellStr) colConfig_emptyRowI;
                    if (emptyValue.byteSize() != newRowCell.byteSize()) {
                        throw new RuntimeException("ERROR @ TS_JdbList.rowGet: emptyValue.byteSize() != newRowCell.byteSize()");
                    }
                    simple.setStringFromPostion_calcNextPosition(position, newRowCell.get()).orThrowFirstInfo();
                    position += newRowCell.byteSize();
                    d.ci("rowSet", "i", i, "pos", position);
                } else {
                    throw new RuntimeException("ERROR @ TS_JdbList.rowGet: unkwon col type");
                }
            }
            return null;
        }, e -> e);
    }
}
