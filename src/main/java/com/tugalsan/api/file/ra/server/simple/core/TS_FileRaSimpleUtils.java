package com.tugalsan.api.file.ra.server.simple.core;

import com.tugalsan.api.bytes.client.TGS_ByteLengthUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.union.client.TGS_Union;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TS_FileRaSimpleUtils {

    final private static TS_Log d = TS_Log.of(false, TS_FileRaSimpleUtils.class);

    public static RandomAccessFile create(File file) throws FileNotFoundException {
        return new RandomAccessFile(file, "rw");
    }

    public static TGS_Union<Double> getDoubleFromPostion(RandomAccessFile raf, long position) {
        try {
            raf.seek(position);
            return TGS_Union.of(raf.readDouble());
        } catch (IOException ex) {
            return TGS_Union.ofExcuse(ex);
        }
    }

    public static TGS_Union<Long> setDoubleFromPostion_calcNextPosition(RandomAccessFile raf, long position, double value) {
        try {
            raf.seek(position);
            raf.writeDouble(value);
            return TGS_Union.of(position + TGS_ByteLengthUtils.typeDouble());
        } catch (IOException ex) {
            return TGS_Union.ofExcuse(ex);
        }
    }

    public static TGS_Union<Long> getLongFromPostion(RandomAccessFile raf, long position) {
        try {
            raf.seek(position);
            return TGS_Union.of(raf.readLong());
        } catch (IOException ex) {
            return TGS_Union.ofExcuse(ex);
        }
    }

    public static TGS_Union<Long> setLongFromPostion_calcNextPosition(RandomAccessFile raf, long position, long value) {
        try {
            raf.seek(position);
            raf.writeLong(value);
            return TGS_Union.of(position + TGS_ByteLengthUtils.typeLong());
        } catch (IOException ex) {
            return TGS_Union.ofExcuse(ex);
        }
    }

    public static TGS_Union<String> getStringFromPostion(RandomAccessFile raf, long position) {
        try {
            raf.seek(position);
            var op = TGS_Union.of(raf.readUTF());
            d.ci("getStringFromPostion", "op", op);
            return op;
        } catch (IOException ex) {
            return TGS_Union.ofExcuse(ex);
        }
    }

    @Deprecated //WARNING: CHECK BYTE SIZE
    public static TGS_Union<Long> setStringFromPostion_calcNextPosition(RandomAccessFile raf, long position, String value) {
        try {
            raf.seek(position);
            raf.writeUTF(value);
            return TGS_Union.of(position + TGS_ByteLengthUtils.typeStringUTF8(value));
        } catch (IOException ex) {
            return TGS_Union.ofExcuse(ex);
        }
    }
}
