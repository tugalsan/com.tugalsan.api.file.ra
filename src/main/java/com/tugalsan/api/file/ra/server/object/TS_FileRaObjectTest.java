package com.tugalsan.api.file.ra.server.object;

import com.tugalsan.api.file.server.TS_PathUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import static java.lang.System.out;
import java.util.Random;

public class TS_FileRaObjectTest {

    final private static TS_Log d = TS_Log.of(false, TS_FileRaObjectTest.class);

    public static void main(String... s) {
        TGS_UnSafe.run(() -> {
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
                var record = db.readRecord("foo.lastAccessTime");
                var object = (Integer) record.readObject();
                out.println("last access was at: " + object.toString());
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
