package com.tugalsan.api.file.ra.server.simple.core;

import com.tugalsan.api.bytes.client.TGS_ByteLengthUtils;
import com.tugalsan.api.file.ra.server.simple.TS_FileRaSimpleTest;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.function.client.maythrowexceptions.checked.TGS_FuncMTCUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.function.Supplier;

public class TS_FileRaSimpleUtils {

    private TS_FileRaSimpleUtils() {

    }

    final private static Supplier<TS_Log> d = StableValue.supplier(() -> TS_Log.of(TS_FileRaSimpleUtils.class));

    public static RandomAccessFile create(File file) throws FileNotFoundException {
        return new RandomAccessFile(file, "rw");
    }

    public static TGS_UnionExcuse<Double> getDoubleFromPostion(RandomAccessFile raf, long position) {
        return TGS_FuncMTCUtils.call(() -> {
            raf.seek(position);
            return TGS_UnionExcuse.of(raf.readDouble());
        }, e -> TGS_UnionExcuse.ofExcuse(e));
    }

    public static TGS_UnionExcuse<Long> setDoubleFromPostion_calcNextPosition(RandomAccessFile raf, long position, double value) {
        return TGS_FuncMTCUtils.call(() -> {
            raf.seek(position);
            raf.writeDouble(value);
            return TGS_UnionExcuse.of(position + TGS_ByteLengthUtils.typeDouble());
        }, e -> TGS_UnionExcuse.ofExcuse(e));
    }

    public static TGS_UnionExcuse<Long> getLongFromPostion(RandomAccessFile raf, long position) {
        return TGS_FuncMTCUtils.call(() -> {
            raf.seek(position);
            return TGS_UnionExcuse.of(raf.readLong());
        }, e -> TGS_UnionExcuse.ofExcuse(e));
    }

    public static TGS_UnionExcuse<Long> setLongFromPostion_calcNextPosition(RandomAccessFile raf, long position, long value) {
        return TGS_FuncMTCUtils.call(() -> {
            raf.seek(position);
            raf.writeLong(value);
            return TGS_UnionExcuse.of(position + TGS_ByteLengthUtils.typeLong());
        }, e -> TGS_UnionExcuse.ofExcuse(e));
    }

    public static TGS_UnionExcuse<String> getStringFromPostion(RandomAccessFile raf, long position) {
        return TGS_FuncMTCUtils.call(() -> {
            raf.seek(position);
            var op = TGS_UnionExcuse.of(raf.readUTF());
            d.get().ci("getStringFromPostion", "op", op);
            return op;
        }, e -> TGS_UnionExcuse.ofExcuse(e));
    }

    @Deprecated //WARNING: CHECK BYTE SIZE
    public static TGS_UnionExcuse<Long> setStringFromPostion_calcNextPosition(RandomAccessFile raf, long position, String value) {
        return TGS_FuncMTCUtils.call(() -> {
            raf.seek(position);
            raf.writeUTF(value);
            return TGS_UnionExcuse.of(position + TGS_ByteLengthUtils.typeStringUTF8(value));
        }, e -> TGS_UnionExcuse.ofExcuse(e));
    }
}
