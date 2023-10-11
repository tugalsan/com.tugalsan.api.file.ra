package com.tugalsan.api.file.ra.server.table;

import com.tugalsan.api.file.server.TS_PathUtils;
import com.tugalsan.api.log.server.TS_Log;
import java.nio.file.Path;
import java.util.Random;

public class TS_FileRaTableTest {

    final private static TS_Log d = TS_Log.of(false, TS_FileRaTableTest.class);

    public static void main(String... s) {
        var dbPath = TS_PathUtils.getPathCurrent_nio(TS_FileRaTableTest.class.getName() + ".jdb");
        d.cr("main", "dbPath", dbPath);
        table_size(dbPath);
    }

    public static void table_size(Path dbPath) {
        var empty0Id = TS_FileRaTableCellLng.ofEmpty();
        var empty1Name = TS_FileRaTableCellStr.ofEmpty(256);
        var empty2Price = TS_FileRaTableCellDbl.ofEmpty();
        var jdbt = TS_FileRaTable.of(dbPath, empty0Id, empty1Name, empty2Price);
        empty1Name.set_cropIfNotProper("Ali gel");
        for (var i = 0; i < 10000; i++) {
            var e = jdbt.rowSet(i, empty0Id, empty1Name, empty2Price);
            if (e != null) {
                throw new RuntimeException(e);
            }
        }
        d.cr("main", "colConfig.byteSize", jdbt.colConfig.byteSize());
        d.cr("main", "rowSize", jdbt.rowSize());
    }

    public static void table_set_get(Path dbPath) {
        var r = new Random();
        var empty0Id = TS_FileRaTableCellLng.ofEmpty();
        var empty1Name = TS_FileRaTableCellStr.ofEmpty(20);
        var empty2Price = TS_FileRaTableCellDbl.ofEmpty();

        var jdbt = TS_FileRaTable.of(dbPath, empty0Id, empty1Name, empty2Price);
        d.cr("main", "rowSize", jdbt.rowSize());

        {
            empty0Id.set(r.nextInt());
            empty1Name.set_cropIfNotProper("FirstString");
            empty2Price.set(r.nextDouble());
            var e = jdbt.rowSet(0, empty0Id, empty1Name, empty2Price);
            if (e != null) {
                throw new RuntimeException(e);
            }
            d.cr("main", "FirstString", "rowSize", jdbt.rowSize());
        }

        {
            empty0Id.set(r.nextInt());
            empty1Name.set_cropIfNotProper("SecondString");
            empty2Price.set(r.nextDouble());
            var e = jdbt.rowSet(1, empty0Id, empty1Name, empty2Price);
            if (e != null) {
                throw new RuntimeException(e);
            }
            d.cr("main", "SecondString", "rowSize", jdbt.rowSize());
        }

        {
            empty0Id.set(r.nextInt());
            empty1Name.set_cropIfNotProper("ThirdString");
            empty2Price.set(r.nextDouble());
            var e = jdbt.rowSet(2, empty0Id, empty1Name, empty2Price);
            if (e != null) {
                throw new RuntimeException(e);
            }
            d.cr("main", "ThirdString", "rowSize", jdbt.rowSize());
        }

        {
            empty0Id.set(r.nextInt());
            var str = "nSecondStringlksdjsald jlaskdj laskjd laskdj lkasjd laskdjlaskdj laskdj salkd ";
            empty1Name.set_cropIfNotProper(str);
            empty2Price.set(r.nextDouble());
            var e = jdbt.rowSet(1, empty0Id, empty1Name, empty2Price);
            if (e != null) {
                throw new RuntimeException(e);
            }
            d.cr("main", "nSecondString", "rowSize", jdbt.rowSize());
        }

        for (int i = 0; i < 3; i++) {
            var row = jdbt.rowGet(i);
            empty0Id = (TS_FileRaTableCellLng) row.get(0);
            empty1Name = (TS_FileRaTableCellStr) row.get(1);
            empty2Price = (TS_FileRaTableCellDbl) row.get(2);
            d.cr("main", "for", i, empty0Id, empty1Name, empty2Price);
        }
    }
}
