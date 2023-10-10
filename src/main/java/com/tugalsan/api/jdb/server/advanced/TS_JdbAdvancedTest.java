package com.tugalsan.api.jdb.server.advanced;

import com.tugalsan.api.file.server.TS_PathUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.time.client.TGS_Time;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import static java.lang.System.out;
import java.util.Date;

public class TS_JdbAdvancedTest {

    final private static TS_Log d = TS_Log.of(false, TS_JdbAdvancedTest.class);

    public static void main(String... s) {
        TGS_UnSafe.run(() -> {
            var dbPath = TS_PathUtils.getPathCurrent_nio(d.className + ".jdb");
            var dbOp = TS_JdbAdvancedFile.of(dbPath);
            if (dbOp.payload.isEmpty()) {
                d.ce("main", "ERROR @ RecordsFile.of", dbOp.info);
                return;
            }
            var db = dbOp.payload.get();
            {//load
                var rw = new TS_JdbAdvancedWriter("foo.lastAccessTime");
                rw.writeObject(TGS_Time.of());
                db.insertRecord(rw);
            }
            {//retrive
                var record = db.readRecord("foo.lastAccessTime");
                var time = (TGS_Time) record.readObject();
                out.println("last access was at: " + time.toString());
            }
            {//write
                var rw = new TS_JdbAdvancedWriter("foo.lastAccessTime");
                rw.writeObject(new Date());
                db.updateRecord(rw);
            }
            {//delete
                db.deleteRecord("foo.lastAccessTime");
            }
        });
    }
}
