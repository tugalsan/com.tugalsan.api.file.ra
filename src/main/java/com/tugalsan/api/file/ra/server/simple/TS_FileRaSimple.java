package com.tugalsan.api.file.ra.server.simple;

import com.tugalsan.api.file.ra.server.simple.core.TS_FileRaSimpleUtils;
import com.tugalsan.api.function.client.TGS_Func_OutTyped_In1;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TS_FileRaSimple {

    private TS_FileRaSimple(Path path) {
        this.path = path;
        this.file = path.toFile();
    }
    final public Path path;
    final private File file;

    public static TS_FileRaSimple of(Path path) {
        return new TS_FileRaSimple(path);
    }

    private <T> TGS_UnionExcuse<T> call(TGS_Func_OutTyped_In1<TGS_UnionExcuse<T>, RandomAccessFile> call) {
        return TGS_UnSafe.call(() -> {
            lock.lock();
            try (var raf = TS_FileRaSimpleUtils.create(file)) {
                var u = call.call(raf);
                if (u.isExcuse()) {
                    return u.toExcuse();
                }
                return TGS_UnionExcuse.of(u.value());
            }
        }, e -> TGS_UnionExcuse.ofExcuse(e), () -> lock.unlock());
    }
    final private Lock lock = new ReentrantLock();

    public TGS_UnionExcuse<Double> getDoubleFromPostion(long position) {
        return call(jdb -> TS_FileRaSimpleUtils.getDoubleFromPostion(jdb, position));
    }

    public TGS_UnionExcuse<Long> setDoubleFromPostion_calcNextPosition(long position, double value) {
        return call(raf -> TS_FileRaSimpleUtils.setDoubleFromPostion_calcNextPosition(raf, position, value));
    }

    public TGS_UnionExcuse<Long> getLongFromPostion(long position) {
        return call(jdb -> TS_FileRaSimpleUtils.getLongFromPostion(jdb, position));
    }

    public TGS_UnionExcuse<Long> setLongFromPostion_calcNextPosition(long position, long value) {
        return call(raf -> TS_FileRaSimpleUtils.setLongFromPostion_calcNextPosition(raf, position, value));
    }

    public TGS_UnionExcuse<String> getStringFromPostion(long position) {
        return call(jdb -> TS_FileRaSimpleUtils.getStringFromPostion(jdb, position));
    }

    @Deprecated //WARNING: CHECK BYTE SIZE
    public TGS_UnionExcuse<Long> setStringFromPostion_calcNextPosition(long position, String value) {
        return call(raf -> TS_FileRaSimpleUtils.setStringFromPostion_calcNextPosition(raf, position, value));
    }
}
