package com.tugalsan.api.file.ra.server.table;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.file.ra.server.simple.TS_FileRaSimple;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;
import com.tugalsan.api.function.client.maythrowexceptions.checked.TGS_FuncMTCUtils;
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

    public TGS_UnionExcuse<List<TS_FileRaTableCellBase>> rowGet(long idx) {
        List<TS_FileRaTableCellBase> lst = new ArrayList();
        var position = position(idx);
        for (var i = 0; i < template.columns.size(); i++) {
            var colConfig_emptyRowI = template.columns.get(i);
            switch (colConfig_emptyRowI) {
                case TS_FileRaTableCellDbl templateDbl -> {
                    var u = simple.getDoubleFromPostion(position);
                    if (u.isExcuse()) {
                        return u.toExcuse();
                    }
                    lst.add(templateDbl.toValue(u.value()));
                    position += templateDbl.byteSize();
                    d.ci("rowGet", "i", i, "pos", position);
                }
                case TS_FileRaTableCellLng templateLng -> {
                    var u = simple.getLongFromPostion(position);
                    if (u.isExcuse()) {
                        return u.toExcuse();
                    }
                    lst.add(templateLng.toValue(u.value()));
                    position += templateLng.byteSize();
                    d.ci("rowGet", "i", i, "pos", position);
                }
                case TS_FileRaTableCellStr templateStr -> {
                    var u = simple.getStringFromPostion(position);
                    if (u.isExcuse()) {
                        return u.toExcuse();
                    }
                    lst.add(templateStr.toValue_cropIfNotProper(u.value()));
                    position += templateStr.byteSize();
                    d.ci("rowGet", "i", i, "pos", position);
                }
                default ->
                    TGS_UnionExcuse.ofExcuse(d.className, "rowGet", "unkwon col type");
            }
        }
        return TGS_UnionExcuse.of(lst);
    }

    public TGS_UnionExcuse<Boolean> rowIsEmpty(long idx) {
        var rowOp = rowGet(idx);
        if (rowOp.isExcuse()) {
            return rowOp.ofExcuse(d.className, "rowIsEmpty", "rowOp.info.isEmpty()");
        }
        return TGS_UnionExcuse.of(template.rowIsEmpty(rowOp.value()));
    }

    public TGS_UnionExcuseVoid rowSetEmpty(long idx) {
        return rowSet(idx, template.rowCreateEmpty());
    }

    public TGS_UnionExcuseVoid rowSet(long idx, TS_FileRaTableCellBase... rowValues) {
        return rowSet(idx, List.of(rowValues));
    }

    public TGS_UnionExcuseVoid rowSet(long idx, List<? extends TS_FileRaTableCellBase> newRow) {
        return TGS_FuncMTCUtils.call(() -> {
            var position = position(idx);
            for (var i = 0; i < template.columns.size(); i++) {
                var colConfig_emptyRowI = template.columns.get(i);
                var newRowValueI = newRow.get(i);
                if (!Objects.equals(newRowValueI.getClass(), colConfig_emptyRowI.getClass())) {
                    throw new RuntimeException("ERROR @ TS_JdbList.rowSet: !Objects.equals(newRowValueI.getClass(), colConfig_emptyRowI.getClass())");
                }
                switch (newRowValueI) {
                    case TS_FileRaTableCellDbl valueDbl -> {
                        var u = simple.setDoubleFromPostion_calcNextPosition(position, valueDbl.get());
                        if (u.isExcuse()) {
                            return u.toExcuseVoid();
                        }
                        position = u.value();
                        d.ci("rowSet", "i", i, "pos", position);
                    }
                    case TS_FileRaTableCellLng valueDbl -> {
                        var u = simple.setLongFromPostion_calcNextPosition(position, valueDbl.get());
                        if (u.isExcuse()) {
                            return u.toExcuseVoid();
                        }
                        position = u.value();
                        d.ci("rowSet", "i", i, "pos", position);
                    }
                    case TS_FileRaTableCellStr valueStr -> {
                        var emptyValue = (TS_FileRaTableCellStr) colConfig_emptyRowI;
                        if (emptyValue.byteSize() != valueStr.byteSize()) {
                            throw new RuntimeException("ERROR @ TS_JdbList.rowSet: emptyValue.byteSize() != newRowCell.byteSize()");
                        }
                        var u = simple.setStringFromPostion_calcNextPosition(position, valueStr.get());
                        if (u.isExcuse()) {
                            return u.toExcuseVoid();
                        }
                        position += valueStr.byteSize();
                        d.ci("rowSet", "i", i, "pos", position);
                    }
                    default ->
                        throw new RuntimeException("ERROR @ TS_JdbList.rowSet: unkwon col type");
                }
            }
            return TGS_UnionExcuseVoid.ofVoid();
        }, e -> TGS_UnionExcuseVoid.ofExcuse(e));
    }
}
