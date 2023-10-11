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
        var colId = TS_FileRaTableCellLng.ofEmpty();
        var colName = TS_FileRaTableCellStr.ofEmpty(256);
        var colPrice = TS_FileRaTableCellDbl.ofEmpty();
        var jdbt = TS_FileRaTable.of(dbPath, colId, colName, colPrice);
        colName.set_cropIfNotProper("Ali gel");
        for (var i = 0; i < 10000; i++) {
            var e = jdbt.rowSet(i, colId, colName, colPrice);
            if (e != null) {
                throw new RuntimeException(e);
            }
        }
        d.cr("main", "colConfig.byteSize", jdbt.colConfig.byteSize());
        d.cr("main", "rowSize", jdbt.rowSize());
    }

    public static void table_set_get(Path dbPath) {
        var r = new Random();
        var colId = TS_FileRaTableCellLng.ofEmpty();
        var colName = TS_FileRaTableCellStr.ofEmpty(20);
        var colPrice = TS_FileRaTableCellDbl.ofEmpty();

        var jdbt = TS_FileRaTable.of(dbPath, colId, colName, colPrice);
        d.cr("main", "rowSize", jdbt.rowSize());

        {
            colId.set(r.nextInt());
            colName.set_cropIfNotProper("FirstString");
            colPrice.set(r.nextDouble());
            var e = jdbt.rowSet(0, colId, colName, colPrice);
            if (e != null) {
                throw new RuntimeException(e);
            }
            d.cr("main", "FirstString", "rowSize", jdbt.rowSize());
        }

        {
            colId.set(r.nextInt());
            colName.set_cropIfNotProper("SecondString");
            colPrice.set(r.nextDouble());
            var e = jdbt.rowSet(1, colId, colName, colPrice);
            if (e != null) {
                throw new RuntimeException(e);
            }
            d.cr("main", "SecondString", "rowSize", jdbt.rowSize());
        }

        {
            colId.set(r.nextInt());
            colName.set_cropIfNotProper("ThirdString");
            colPrice.set(r.nextDouble());
            var e = jdbt.rowSet(2, colId, colName, colPrice);
            if (e != null) {
                throw new RuntimeException(e);
            }
            d.cr("main", "ThirdString", "rowSize", jdbt.rowSize());
        }

        {
            colId.set(r.nextInt());
            var str = "nSecondStringlksdjsald jlaskdj laskjd laskdj lkasjd laskdjlaskdj laskdj salkd ";
            colName.set_cropIfNotProper(str);
            colPrice.set(r.nextDouble());
            var e = jdbt.rowSet(1, colId, colName, colPrice);
            if (e != null) {
                throw new RuntimeException(e);
            }
            d.cr("main", "nSecondString", "rowSize", jdbt.rowSize());
        }

        for (int i = 0; i < 3; i++) {
            var row = jdbt.rowGet(i);
            colId = (TS_FileRaTableCellLng) row.get(0);
            colName = (TS_FileRaTableCellStr) row.get(1);
            colPrice = (TS_FileRaTableCellDbl) row.get(2);
            d.cr("main", "for", i, colId.get(), colName.get(), colPrice.get());
        }

    }
}
