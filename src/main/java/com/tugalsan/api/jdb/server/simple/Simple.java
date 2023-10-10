package com.tugalsan.api.jdb.server.simple;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.file.server.TS_PathUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.optional.client.TGS_Optional;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public class Simple {

    final private static TS_Log d = TS_Log.of(false, Simple.class);

    public static void main(String... s) {
        var dbPath = TS_PathUtils.getPathCurrent_nio(d.className + ".jdb");
        String data = "my custom data";

        writeToRandomAccessFile("sample.store", 100, data);

        String fromFile = readFromRandomAccessFile("sample.store", 100);
        System.out.println("Read: " + fromFile);

    }

    public <T> TGS_Optional<T> with(Path dbPath, TGS_CallableType1<T, RandomAccessFile> call) {
        return TGS_UnSafe.call(() -> {
            try (var jdb = new RandomAccessFile(dbPath.toFile(), "rw")) {
                return TGS_Optional.of(call.call(jdb));
            }
        }, e -> TGS_Optional.ofEmpty(e.getMessage()));
    }
    public static 

    public static String readFromRandomAccessFile(Path dbPath, int position) {
        String record = null;
        try {
            var jdb = new RandomAccessFile(dbPath.toFile(), "rw");
            jdb.seek(position);
            record = jdb.readUTF();
            jdb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return record;
    }

    public static void writeToRandomAccessFile(Path dbPath, int position, String record) {
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
