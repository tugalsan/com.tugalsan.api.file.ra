module com.tugalsan.api.jdb {
    requires com.tugalsan.api.time;
    requires com.tugalsan.api.file;
    requires com.tugalsan.api.optional;
    requires com.tugalsan.api.unsafe;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.callable;
    requires com.tugalsan.api.runnable;
    exports com.tugalsan.api.jdb.server.advanced;
    exports com.tugalsan.api.jdb.server.simple;
}