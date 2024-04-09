package com.tugalsan.api.file.ra.server.object;

import com.tugalsan.api.file.server.TS_PathUtils;
import com.tugalsan.api.log.server.TS_Log;
import java.io.IOException;
import static java.lang.System.out;
import java.util.Random;

public class TS_FileRaObjectTest {

    final private static TS_Log d = TS_Log.of(false, TS_FileRaObjectTest.class);

    public static void main(String... s) {
        var dbPath = TS_PathUtils.getPathCurrent_nio(TS_FileRaObjectTest.class.getName() + ".ra");
        var dbOp = TS_FileRaObjectFile.of(dbPath);
        if (dbOp.isError()) {
            d.ct("main", dbOp.excuse());
            return;
        }
        var db = dbOp.value();
        var r = new Random();
        var rw = new TS_FileRaObjectWriter("foo.lastAccessTime");
        try {//load
            rw.writeObject(r.nextInt());
            db.insertRecord(rw);
        } catch (TS_FileRaObjectException | IOException ex) {
            d.ct("main.load", ex);
            return;
        }
        try {//retrive
            var record = db.readRecord("foo.lastAccessTime");
            var object = (Integer) record.readObject();
            out.println("last access was at: " + object.toString());
        } catch (TS_FileRaObjectException | IOException | ClassNotFoundException ex) {
            d.ct("main.retrive", ex);
            return;
        }
        try {//write
            rw.writeObject(r.nextInt());
            db.updateRecord(rw);
        } catch (TS_FileRaObjectException | IOException ex) {
            d.ct("main.write", ex);
            return;
        }
        try {//delete
            db.deleteRecord("foo.lastAccessTime");
        } catch (TS_FileRaObjectException | IOException ex) {
            d.ct("main.delete", ex);
            return;
        }
    }
}
