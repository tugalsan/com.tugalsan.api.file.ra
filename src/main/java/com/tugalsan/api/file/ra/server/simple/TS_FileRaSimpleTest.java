package com.tugalsan.api.file.ra.server.simple;

import com.tugalsan.api.file.server.TS_PathUtils;
import com.tugalsan.api.log.server.TS_Log;
import java.nio.file.Path;
import java.util.function.Supplier;

public class TS_FileRaSimpleTest {

    private TS_FileRaSimpleTest() {

    }

    final private static Supplier<TS_Log> d = StableValue.supplier(() -> TS_Log.of(TS_FileRaSimpleTest.class));

    public static void main(String... s) {
        var dbPath = TS_PathUtils.getPathCurrent_nio(TS_FileRaSimpleTest.class.getName() + ".ra");
        d.get().cr("main", "dbPath", dbPath);
        text(dbPath);
    }

    public static void text(Path dbPath) {
        var positionInit = 100;
        d.get().cr("main", "positionInit", positionInit);
        var jdbs = TS_FileRaSimple.of(dbPath);
        var positionNext = jdbs.setStringFromPostion_calcNextPosition(positionInit, "Hello World!");
        var hw = jdbs.getStringFromPostion(positionInit);
        d.get().cr("main", "hw", hw);
        d.get().cr("main", "positionNext", positionNext);
    }
}
