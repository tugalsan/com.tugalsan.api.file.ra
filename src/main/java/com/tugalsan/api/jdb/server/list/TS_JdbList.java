package com.tugalsan.api.jdb.server.list;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.jdb.server.simple.TS_JdbSimple;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TS_JdbList {

    private TS_JdbList(Path path, TS_JdbListColumConfig colConfig) {
        this.simple = TS_JdbSimple.of(path);
        this.colConfig = colConfig;
    }
    final private TS_JdbSimple simple;
    final public TS_JdbListColumConfig colConfig;

    public static TS_JdbList of(Path path, TS_JdbListColumConfig colConfig) {
        return new TS_JdbList(path, colConfig);
    }

    public Path path() {
        return simple.path;
    }

    public long rowSize() {
        var sizeInBytes_db = TS_FileUtils.getFileSizeInBytes(path());
        var sizeInBytes_row = colConfig.byteSize();
        return sizeInBytes_db / sizeInBytes_row;
    }

    private long position(long idx) {
        return idx * colConfig.byteSize();
    }

    //TODO List should be ? extends
    public List<TS_JdbListColumnBase> rowGet(long idx) {
        List<TS_JdbListColumnBase> lst = new ArrayList();
        var position = position(idx);
        for (var i = 0; i < colConfig.types.size(); i++) {
            var type = colConfig.types.get(i);
            if (type instanceof TS_JdbListColDbl) {
                var val = simple.getDoubleFromPostion(position).orThrowFirstInfo();
                var value = TS_JdbListColDbl.of(val);
                lst.add(value);
            } else if (type instanceof TS_JdbListColLng) {
                var val = simple.getLongFromPostion(position).orThrowFirstInfo();
                var value = TS_JdbListColLng.of(val);
                lst.add(value);
            } else if (type instanceof TS_JdbListColStr typeStr) {
                var val = simple.getStringFromPostion(position).orThrowFirstInfo();
                var valueStr = TS_JdbListColStr.of(typeStr.byteSize());
                valueStr.set(val);
                var value = valueStr;
                lst.add(value);
            } else if (type instanceof TS_JdbListColStr) {
                throw new RuntimeException("ERROR @ TS_JdbList.rowGet: unkwon col type");
            }
            position += type.byteSize();
        }
        return lst;
    }

    public Exception setGet(long idx, List<? extends TS_JdbListColumnBase> newRow) {
        return TGS_UnSafe.call(() -> {
            var position = position(idx);
            for (var i = 0; i < colConfig.types.size(); i++) {
                var type = colConfig.types.get(i);
                Object value = newRow.get(i);
                if (type instanceof TS_JdbListColDbl) {
                    position = simple.setDoubleFromPostion_calcNextPosition(position, (Double) value).orThrowFirstInfo();
                } else if (type instanceof TS_JdbListColLng) {
                    position = simple.setLongFromPostion_calcNextPosition(position, (Long) value).orThrowFirstInfo();
                } else if (type instanceof TS_JdbListColStr) {
                    position = simple.setStringFromPostion_calcNextPosition(position, (String) value).orThrowFirstInfo();
                } else if (type instanceof TS_JdbListColStr) {
                    throw new RuntimeException("ERROR @ TS_JdbList.rowGet: unkwon col type");
                }
            }
            return null;
        }, e -> e);
    }
}
