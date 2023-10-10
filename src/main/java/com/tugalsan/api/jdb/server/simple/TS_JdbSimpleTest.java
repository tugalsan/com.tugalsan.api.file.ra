package com.tugalsan.api.jdb.server.simple;

import com.tugalsan.api.file.server.TS_PathUtils;
import com.tugalsan.api.log.server.TS_Log;

public class TS_JdbSimpleTest {

    final private static TS_Log d = TS_Log.of(false, TS_JdbSimpleTest.class);

    public static void main(String... s) {
        var dbPath = TS_PathUtils.getPathCurrent_nio(d.className + ".jdb");
        d.cr("main", "dbPath", dbPath);
        var jdbs = TS_JdbSimple.of(dbPath);
        jdbs.setCharSequenceFromPostion(100, "Hello World!");
        var hw = jdbs.getStringFromPostion(100).orThrowFirstInfo();
        d.cr("main", "hw", hw);
    }
}
