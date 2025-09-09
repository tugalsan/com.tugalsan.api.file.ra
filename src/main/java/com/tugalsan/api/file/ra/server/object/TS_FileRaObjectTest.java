package com.tugalsan.api.file.ra.server.object;

import com.tugalsan.api.file.server.TS_PathUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.function.client.maythrowexceptions.checked.TGS_FuncMTCUtils;
import java.util.Random;
import java.util.function.Supplier;

public class TS_FileRaObjectTest {

    private TS_FileRaObjectTest() {

    }

    final private static Supplier<TS_Log> d = StableValue.supplier(() -> TS_Log.of(TS_FileRaObjectTest.class));

    public static void test() {
        TGS_FuncMTCUtils.run(() -> {
            var dbPath = TS_PathUtils.getPathCurrent_nio(TS_FileRaObjectTest.class.getName() + ".ra");
            var u = TS_FileRaObjectFile.of(dbPath);
            if (u.isExcuse()) {
                d.get().ce("main", "ERROR @ RecordsFile.of", u.excuse().getMessage());
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
                d.get().cr("last access was at: " + object.toString());
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
