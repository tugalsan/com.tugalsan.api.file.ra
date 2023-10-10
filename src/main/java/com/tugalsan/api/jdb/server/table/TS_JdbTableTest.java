package com.tugalsan.api.jdb.server.table;

import com.tugalsan.api.jdb.server.simple.*;
import com.tugalsan.api.file.server.TS_PathUtils;
import com.tugalsan.api.log.server.TS_Log;
import java.nio.file.Path;

public class TS_JdbTableTest {

    final private static TS_Log d = TS_Log.of(false, TS_JdbTableTest.class);

    public static void main(String... s) {
        var dbPath = TS_PathUtils.getPathCurrent_nio(TS_JdbTableTest.class.getName() + ".jdb");
        d.cr("main", "dbPath", dbPath);
        list(dbPath);
    }

    public static void list(Path dbPath) {
        var positionInit = 100;
        d.cr("main", "positionInit", positionInit);
        var jdbs = TS_JdbSimple.of(dbPath);
        var positionNext = jdbs.setStringFromPostion_calcNextPosition(positionInit, "Hello World!").orThrowFirstInfo();
        var hw = jdbs.getStringFromPostion(positionInit).orThrowFirstInfo();
        d.cr("main", "hw", hw);
        d.cr("main", "positionNext", positionNext);
    }
}
