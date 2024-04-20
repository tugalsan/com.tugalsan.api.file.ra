package com.tugalsan.api.file.ra.server.simple;

import com.tugalsan.api.file.server.TS_PathUtils;
import com.tugalsan.api.log.server.TS_Log;
import java.nio.file.Path;

public class TS_FileRaSimpleTest {

    final private static TS_Log d = TS_Log.of(false, TS_FileRaSimpleTest.class);

    public static void main(String... s) {
        var dbPath = TS_PathUtils.getPathCurrent_nio(TS_FileRaSimpleTest.class.getName() + ".ra");
        d.cr("main", "dbPath", dbPath);
        text(dbPath);
    }

    public static void text(Path dbPath) {
        var positionInit = 100;
        d.cr("main", "positionInit", positionInit);
        var jdbs = TS_FileRaSimple.of(dbPath);
        var positionNext = jdbs.setStringFromPostion_calcNextPosition(positionInit, "Hello World!");
        var hw = jdbs.getStringFromPostion(positionInit);
        d.cr("main", "hw", hw);
        d.cr("main", "positionNext", positionNext);
    }
}
