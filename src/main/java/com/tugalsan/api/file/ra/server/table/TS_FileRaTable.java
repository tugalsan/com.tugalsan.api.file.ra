package com.tugalsan.api.file.ra.server.table;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.file.ra.server.simple.TS_FileRaSimple;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

    public static TS_FileRaTable of(Path path, TS_FileRaTableColBase... types) {
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

    public List<TS_FileRaTableColBase> rowGet(long idx) {
        List<TS_FileRaTableColBase> lst = new ArrayList();
        var position = position(idx);
        for (var i = 0; i < colConfig.types.size(); i++) {
            var type = colConfig.types.get(i);
            if (type instanceof TS_FileRaTableColDbl) {
                var val = simple.getDoubleFromPostion(position).orThrowFirstInfo();
                var value = TS_FileRaTableColDbl.of(val);
                lst.add(value);
                position += value.byteSize();
                d.ci("rowGet", "i", i, "pos", position);
            } else if (type instanceof TS_FileRaTableColLng) {
                var val = simple.getLongFromPostion(position).orThrowFirstInfo();
                var value = TS_FileRaTableColLng.of(val);
                lst.add(value);
                position += value.byteSize();
                d.ci("rowGet", "i", i, "pos", position);
            } else if (type instanceof TS_FileRaTableColStr typeStr) {
                var valOp = simple.getStringFromPostion(position);
                d.ci("valOp", valOp);
                var val = valOp.orThrowFirstInfo();
                var valueStr = TS_FileRaTableColStr.of(typeStr.byteSize());
                valueStr.set(val);
                var value = valueStr;
                lst.add(value);
                position += value.byteSize();
                d.ci("rowGet", "i", i, "pos", position);
            } else if (type instanceof TS_FileRaTableColStr) {
                throw new RuntimeException("ERROR @ TS_JdbList.rowGet: unkwon col type");
            }
        }
        return lst;
    }

    public Exception rowSet(long idx, TS_FileRaTableColBase... rowValues) {
        return rowSet(idx, List.of(rowValues));
    }

    public Exception rowSet(long idx, List<? extends TS_FileRaTableColBase> newRow) {
        return TGS_UnSafe.call(() -> {
            var position = position(idx);
            for (var i = 0; i < colConfig.types.size(); i++) {
                var type = colConfig.types.get(i);
                Object value = newRow.get(i);
                if (type instanceof TS_FileRaTableColDbl) {
                    position = simple.setDoubleFromPostion_calcNextPosition(position, ((TS_FileRaTableColDbl) value).value).orThrowFirstInfo();
                    d.ci("rowSet", "i", i, "pos", position);
                } else if (type instanceof TS_FileRaTableColLng) {
                    position = simple.setLongFromPostion_calcNextPosition(position, ((TS_FileRaTableColLng) value).value).orThrowFirstInfo();
                    d.ci("rowSet", "i", i, "pos", position);
                } else if (type instanceof TS_FileRaTableColStr typeStr) {
                    simple.setStringFromPostion_calcNextPosition(position, ((TS_FileRaTableColStr) value).get()).orThrowFirstInfo();
                    position += typeStr.byteSize();
                    d.ci("rowSet", "i", i, "pos", position);
                } else {
                    throw new RuntimeException("ERROR @ TS_JdbList.rowGet: unkwon col type");
                }
            }
            return null;
        }, e -> e);
    }
}
