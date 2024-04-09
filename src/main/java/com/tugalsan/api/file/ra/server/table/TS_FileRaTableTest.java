package com.tugalsan.api.file.ra.server.table;

import com.tugalsan.api.file.server.TS_PathUtils;
import com.tugalsan.api.log.server.TS_Log;
import java.nio.file.Path;
import java.util.Random;

public class TS_FileRaTableTest {

    final private static TS_Log d = TS_Log.of(false, TS_FileRaTableTest.class);

    public static void main(String... s) {
        var dbPath = TS_PathUtils.getPathCurrent_nio(TS_FileRaTableTest.class.getName() + ".ra");
        d.cr("main", "dbPath", dbPath);
//        table_size(dbPath);
        table_set_get(dbPath);
    }

    public static void table_size(Path dbPath) {
        var template0Id = TS_FileRaTableCellLng.ofTemplate();
        var template1Name = TS_FileRaTableCellStr.ofTemplate(20);
        var template2Price = TS_FileRaTableCellDbl.ofTemplate();
        var jdbt = TS_FileRaTable.of(dbPath, template0Id, template1Name, template2Price);
        template1Name.set_cropIfNotProper("Ali gel");
        for (var i = 0; i < 10000; i++) {
            var e = jdbt.rowSet(i, template0Id, template1Name, template2Price);
            if (e.isError()) {
                throw new RuntimeException(e.excuse());
            }
        }
        d.cr("main", "colConfig.byteSize", jdbt.template.byteSize());
        d.cr("main", "rowSize", jdbt.rowSize());
    }

    public static void table_set_get(Path dbPath) {
        var r = new Random();
        var template0Id = TS_FileRaTableCellLng.ofTemplate();
        var template1Name = TS_FileRaTableCellStr.ofTemplate(20);
        var template2Price = TS_FileRaTableCellDbl.ofTemplate();

        var jdbt = TS_FileRaTable.of(dbPath, template0Id, template1Name, template2Price);
        d.cr("main", "rowSize", jdbt.rowSize());

        {
            var e = jdbt.rowSet(
                    0,
                    template0Id.toValue(r.nextInt()),
                    template1Name.toValue_cropIfNotProper("FirstString"),
                    template2Price.toValue(r.nextDouble())
            );
            if (e.isError()) {
                throw new RuntimeException(e.excuse());
            }

            d.cr("main", "FirstString", "rowSize", jdbt.rowSize());
        }

        {
            var e = jdbt.rowSet(
                    1,
                    template0Id.toValue(r.nextInt()),
                    template1Name.toValue_cropIfNotProper("SecondString"),
                    template2Price.toValue(r.nextDouble())
            );
            if (e.isError()) {
                throw new RuntimeException(e.excuse());
            }

            d.cr("main", "SecondString", "rowSize", jdbt.rowSize());
        }

        {
            var e = jdbt.rowSet(
                    2,
                    template0Id.toValue(r.nextInt()),
                    template1Name.toValue_cropIfNotProper("ThirdString"),
                    template2Price.toValue(r.nextDouble())
            );
            if (e.isError()) {
                throw new RuntimeException(e.excuse());
            }

            d.cr("main", "ThirdString", "rowSize", jdbt.rowSize());
        }

        {
            var e = jdbt.rowSet(
                    1,
                    template0Id.toValue(r.nextInt()),
                    template1Name.toValue_cropIfNotProper("nSecondStringlksdjsald jlaskdj laskjd laskdj lkasjd laskdjlaskdj laskdj salkd "),
                    template2Price.toValue(r.nextDouble())
            );
            if (e.isError()) {
                throw new RuntimeException(e.excuse());
            }

            d.cr("main", "nSecondString", "rowSize", jdbt.rowSize());
        }

        for (var i = 0; i < 3; i++) {
            var row = jdbt.rowGet(i).value();
            d.cr("main", "for", i, row.get(0), row.get(1), row.get(2));
        }
    }
}
