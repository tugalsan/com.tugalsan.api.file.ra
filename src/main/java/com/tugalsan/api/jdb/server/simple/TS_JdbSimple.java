package com.tugalsan.api.jdb.server.simple;

import com.tugalsan.api.jdb.server.simple.core.TS_JdbSimpleUtils;
import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.optional.client.TGS_Optional;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.io.File;
import java.io.IOException;
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

    private <T> TGS_Optional<T> use(TGS_CallableType1<T, RandomAccessFile> jdb) {
        return TGS_UnSafe.call(() -> {
            lock.lock();
            try (var raf = TS_JdbSimpleUtils.create(file)) {
                return TGS_Optional.of(jdb.call(raf));
            }
        }, e -> TGS_Optional.ofEmpty(e.getMessage()), () -> lock.unlock());
    }
    final private Lock lock = new ReentrantLock();

    public TGS_Optional<String> getStringFromPostion(int position) {
        return use(jdb -> {
            return TS_JdbSimpleUtils.getStringFromPostion(jdb, position);
        });
    }

    

    public void writeToRandomAccessFile(Path dbPath, int position, String record) {
        try {
            var jdb = new RandomAccessFile(dbPath.toFile(), "rw");
            jdb.seek(position);
            jdb.writeUTF(record);
            jdb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
