package com.tugalsan.api.file.ra.server.simple.core;

import com.tugalsan.api.bytes.client.TGS_ByteLengthUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.optional.client.TGS_Optional;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class TS_FileRaSimpleUtils {

    final private static TS_Log d = TS_Log.of(false, TS_FileRaSimpleUtils.class);

    public static RandomAccessFile create(File file) throws FileNotFoundException {
        return new RandomAccessFile(file, "rw");
    }

    public static TGS_Optional<Double> getDoubleFromPostion(RandomAccessFile raf, long position) {
        return TGS_UnSafe.call(() -> {
            raf.seek(position);
            return TGS_Optional.of(raf.readDouble());
        }, e -> TGS_Optional.ofEmpty(e.getClass().getSimpleName() + ":" + e.getMessage()));
    }

    public static TGS_Optional<Long> setDoubleFromPostion_calcNextPosition(RandomAccessFile raf, long position, double value) {
        return TGS_UnSafe.call(() -> {
            raf.seek(position);
            raf.writeDouble(value);
            return TGS_Optional.of(position + TGS_ByteLengthUtils.typeDouble());
        }, e -> TGS_Optional.ofEmpty(e.getClass().getSimpleName() + ":" + e.getMessage()));
    }

    public static TGS_Optional<Long> getLongFromPostion(RandomAccessFile raf, long position) {
        return TGS_UnSafe.call(() -> {
            raf.seek(position);
            return TGS_Optional.of(raf.readLong());
        }, e -> TGS_Optional.ofEmpty(e.getClass().getSimpleName() + ":" + e.getMessage()));
    }

    public static TGS_Optional<Long> setLongFromPostion_calcNextPosition(RandomAccessFile raf, long position, long value) {
        return TGS_UnSafe.call(() -> {
            raf.seek(position);
            raf.writeLong(value);
            return TGS_Optional.of(position + TGS_ByteLengthUtils.typeLong());
        }, e -> TGS_Optional.ofEmpty(e.getClass().getSimpleName() + ":" + e.getMessage()));
    }

    public static TGS_Optional<String> getStringFromPostion(RandomAccessFile raf, long position) {
        return TGS_UnSafe.call(() -> {
            raf.seek(position);
            var op = TGS_Optional.of(raf.readUTF());
            d.ci("getStringFromPostion", "op", op);
            return op;
        }, e -> TGS_Optional.ofEmpty(e.getClass().getSimpleName() + ":" + e.getMessage()));
    }

    @Deprecated //WARNING: CHECK BYTE SIZE
    public static TGS_Optional<Long> setStringFromPostion_calcNextPosition(RandomAccessFile raf, long position, String value) {
        return TGS_UnSafe.call(() -> {
            raf.seek(position);
            raf.writeUTF(value);
            return TGS_Optional.of(position + TGS_ByteLengthUtils.typeStringUTF8(value));
        }, e -> TGS_Optional.ofEmpty(e.getClass().getSimpleName() + ":" + e.getMessage()));
    }
}
