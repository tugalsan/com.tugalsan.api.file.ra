package com.tugalsan.api.jdb.server.simple;

import com.tugalsan.api.file.server.TS_PathUtils;
import com.tugalsan.api.log.server.TS_Log;

public class TS_JdbSimpleTest {

    final private static TS_Log d = TS_Log.of(false, TS_JdbSimpleTest.class);

    public static void main(String... s) {
        var dbPath = TS_PathUtils.getPathCurrent_nio(TS_JdbSimpleTest.class.getName() + ".jdb");
        d.cr("main", "dbPath", dbPath);
        var positionInit = 100;
        d.cr("main", "positionInit", positionInit);
        var jdbs = TS_JdbSimple.of(dbPath);
        var positionNext = jdbs.setStringFromPostion_calcNextPosition(positionInit, "Hello World!").orThrowFirstInfo();
        var hw = jdbs.getStringFromPostion(positionInit).orThrowFirstInfo();
        d.cr("main", "hw", hw);
        d.cr("main", "positionNext", positionNext);
    }
}
