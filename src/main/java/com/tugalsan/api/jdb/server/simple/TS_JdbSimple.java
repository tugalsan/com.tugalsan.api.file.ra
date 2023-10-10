package com.tugalsan.api.jdb.server.simple;

import com.tugalsan.api.jdb.server.simple.core.TS_JdbSimpleUtils;
import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.optional.client.TGS_Optional;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TS_JdbSimple {

    private TS_JdbSimple(Path path) {
        this.path = path;
        this.file = path.toFile();
    }
    final public Path path;
    final private File file;

    public static TS_JdbSimple of(Path path) {
        return new TS_JdbSimple(path);
    }

    private <T> TGS_Optional<T> call(TGS_CallableType1<T, RandomAccessFile> call) {
        return TGS_UnSafe.call(() -> {
            lock.lock();
            try (var raf = TS_JdbSimpleUtils.create(file)) {
                return TGS_Optional.of(call.call(raf));
            }
        }, e -> TGS_Optional.ofEmpty(e.getMessage()), () -> lock.unlock());
    }
    final private Lock lock = new ReentrantLock();

    public TGS_Optional<Double> getDoubleFromPostion(int position) {
        return call(jdb -> TS_JdbSimpleUtils.getDoubleFromPostion(jdb, position).orThrowFirstInfo());
    }

    public TGS_Optional<Integer> setDoubleFromPostion_calcNextPosition(int position, double value) {
        return call(raf -> TS_JdbSimpleUtils.setDoubleFromPostion_calcNextPosition(raf, position, value).orThrowFirstInfo());
    }

    public TGS_Optional<Long> getLongFromPostion(int position) {
        return call(jdb -> TS_JdbSimpleUtils.getLongFromPostion(jdb, position).orThrowFirstInfo());
    }

    public TGS_Optional<Integer> setLongFromPostion_calcNextPosition(int position, long value) {
        return call(raf -> TS_JdbSimpleUtils.setLongFromPostion_calcNextPosition(raf, position, value).orThrowFirstInfo());
    }

    public TGS_Optional<String> getStringFromPostion(int position) {
        return call(jdb -> TS_JdbSimpleUtils.getStringFromPostion(jdb, position).orThrowFirstInfo());
    }

    public TGS_Optional<Integer> setStringFromPostion_calcNextPosition(int position, String value) {
        return call(raf -> TS_JdbSimpleUtils.setStringFromPostion_calcNextPosition(raf, position, value).orThrowFirstInfo());
    }
}
