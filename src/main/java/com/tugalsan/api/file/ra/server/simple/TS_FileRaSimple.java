package com.tugalsan.api.file.ra.server.simple;

import com.tugalsan.api.file.ra.server.simple.core.TS_FileRaSimpleUtils;
import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.optional.client.TGS_Optional;
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

    private <T> TGS_Optional<T> call(TGS_CallableType1<T, RandomAccessFile> call) {
        return TGS_UnSafe.call(() -> {
            lock.lock();
            try (var raf = TS_FileRaSimpleUtils.create(file)) {
                return TGS_Optional.of(call.call(raf));
            }
        }, e -> TGS_Optional.ofEmpty(e.getClass().getSimpleName() + ":" + e.getMessage()), () -> lock.unlock());
    }
    final private Lock lock = new ReentrantLock();

    public TGS_Optional<Double> getDoubleFromPostion(long position) {
        return call(jdb -> TS_FileRaSimpleUtils.getDoubleFromPostion(jdb, position).orThrowFirstInfo());
    }

    public TGS_Optional<Long> setDoubleFromPostion_calcNextPosition(long position, double value) {
        return call(raf -> TS_FileRaSimpleUtils.setDoubleFromPostion_calcNextPosition(raf, position, value).orThrowFirstInfo());
    }

    public TGS_Optional<Long> getLongFromPostion(long position) {
        return call(jdb -> TS_FileRaSimpleUtils.getLongFromPostion(jdb, position).orThrowFirstInfo());
    }

    public TGS_Optional<Long> setLongFromPostion_calcNextPosition(long position, long value) {
        return call(raf -> TS_FileRaSimpleUtils.setLongFromPostion_calcNextPosition(raf, position, value).orThrowFirstInfo());
    }

    public TGS_Optional<String> getStringFromPostion(long position) {
        return call(jdb -> TS_FileRaSimpleUtils.getStringFromPostion(jdb, position).orThrowFirstInfo());
    }

    @Deprecated //WARNING: CHECK BYTE SIZE
    public TGS_Optional<Long> setStringFromPostion_calcNextPosition(long position, String value) {
        return call(raf -> TS_FileRaSimpleUtils.setStringFromPostion_calcNextPosition(raf, position, value).orThrowFirstInfo());
    }
}
