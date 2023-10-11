module com.tugalsan.api.file.ra {
    requires com.tugalsan.api.bytes;
    requires com.tugalsan.api.file;
    requires com.tugalsan.api.optional;
    requires com.tugalsan.api.unsafe;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.callable;
    requires com.tugalsan.api.runnable;
    exports com.tugalsan.api.file.ra.server.indexed;
    exports com.tugalsan.api.file.ra.server.simple;
    exports com.tugalsan.api.file.ra.server.table;
}
