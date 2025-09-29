package com.tugalsan.api.file.ra.server.object;

import module com.tugalsan.api.file;
import module com.tugalsan.api.log;
import module com.tugalsan.api.function;
import java.util.*;

public class TS_FileRaObjectTest {

    private TS_FileRaObjectTest() {

    }

    final private static TS_Log d = TS_Log.of(TS_FileRaObjectTest.class);

    public static void test() {
        TGS_FuncMTCUtils.run(() -> {
            var dbPath = TS_PathUtils.getPathCurrent_nio(TS_FileRaObjectTest.class.getName() + ".ra");
            var u = TS_FileRaObjectFile.of(dbPath);
            if (u.isExcuse()) {
                d.ce("main", "ERROR @ RecordsFile.of", u.excuse().getMessage());
                return;
            }
            var db = u.value();
            var r = new Random();
            {//load
                var rw = new TS_FileRaObjectWriter("foo.lastAccessTime");
                rw.writeObject(r.nextInt());
                db.insertRecord(rw);
            }
            {//retrive
                var rec = db.readRecord("foo.lastAccessTime");
                var object = (Integer) rec.readObject();
                d.cr("last access was at: " + object.toString());
            }
            {//write
                var rw = new TS_FileRaObjectWriter("foo.lastAccessTime");
                rw.writeObject(r.nextInt());
                db.updateRecord(rw);
            }
            {//delete
                db.deleteRecord("foo.lastAccessTime");
            }
        });
    }
}
