module com.tugalsan.api.file.ra {
    requires com.tugalsan.api.bytes;
    requires com.tugalsan.api.file;
    requires com.tugalsan.api.union;
    requires com.tugalsan.api.stream;
    requires com.tugalsan.api.unsafe;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.callable;
    exports com.tugalsan.api.file.ra.server.object;
    exports com.tugalsan.api.file.ra.server.simple;
    exports com.tugalsan.api.file.ra.server.table;
}
