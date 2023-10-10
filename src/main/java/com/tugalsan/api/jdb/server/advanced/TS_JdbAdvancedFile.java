package com.tugalsan.api.jdb.server.advanced;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.optional.client.TGS_Optional;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class TS_JdbAdvancedFile extends TS_JdbAdvancedBase {

    /**
     * Hashtable which holds the in-memory index. For efficiency, the entire
     * index is cached in memory. The hashtable maps a key of type String to a
     * RecordHeader.
     */
    protected Hashtable memIndex;

    public static TGS_Optional<TS_JdbAdvancedFile> of(Path dbPath) {
        return TGS_UnSafe.call(() -> TS_FileUtils.isExistFile(dbPath) ? TGS_Optional.of(new TS_JdbAdvancedFile(dbPath, "rw")) : TGS_Optional.of(new TS_JdbAdvancedFile(dbPath, 64)), e -> TGS_Optional.ofEmpty(e.getMessage()));
    }

    /**
     * Creates a new database file. The initialSize parameter determines the
     * amount of space which is allocated for the index. The index can grow
     * dynamically, but the parameter is provide to increase efficiency.
     */
    private TS_JdbAdvancedFile(Path dbPath, int initialSize) throws IOException, TS_JdbAdvancedException {
        super(dbPath, initialSize);
        memIndex = new Hashtable(initialSize);
    }

    /**
     * Opens an existing database and initializes the in-memory index.
     */
    private TS_JdbAdvancedFile(Path dbPath, String accessFlags) throws IOException, TS_JdbAdvancedException {
        super(dbPath, accessFlags);
        var numRecords = readNumRecordsHeader();
        memIndex = new Hashtable(numRecords);
        for (int i = 0; i < numRecords; i++) {
            String key = readKeyFromIndex(i);
            TS_JdbAdvancedHeader header = readRecordHeaderFromIndex(i);
            header.setIndexPosition(i);
            memIndex.put(key, header);
        }
    }

    /**
     * Returns an enumeration of all the keys in the database.
     */
    public synchronized Enumeration enumerateKeys() {
        return memIndex.keys();
    }

    /**
     * Returns the current number of records in the database.
     */
    public synchronized int getNumRecords() {
        return memIndex.size();
    }

    /**
     * Checks if there is a record belonging to the given key.
     */
    public synchronized boolean recordExists(String key) {
        return memIndex.containsKey(key);
    }

    /**
     * Maps a key to a record header by looking it up in the in-memory index.
     */
    protected TS_JdbAdvancedHeader keyToRecordHeader(String key) throws TS_JdbAdvancedException {
        var h = (TS_JdbAdvancedHeader) memIndex.get(key);
        if (h == null) {
            throw new TS_JdbAdvancedException("Key not found: " + key);
        }
        return h;
    }

    /**
     * This method searches the file for free space and then returns a
     * RecordHeader which uses the space. (O(n) memory accesses)
     */
    protected TS_JdbAdvancedHeader allocateRecord(String key, int dataLength) throws TS_JdbAdvancedException, IOException {
        // search for empty space
        TS_JdbAdvancedHeader newRecord = null;
        var e = memIndex.elements();
        while (e.hasMoreElements()) {
            var next = (TS_JdbAdvancedHeader) e.nextElement();
//            var free = next.getFreeSpace();
            if (dataLength <= next.getFreeSpace()) {
                newRecord = next.split();
                writeRecordHeaderToIndex(next);
                break;
            }
        }
        if (newRecord == null) {
            // append record to end of file - grows file to allocate space
            var fp = getFileLength();
            setFileLength(fp + dataLength);
            newRecord = new TS_JdbAdvancedHeader(fp, dataLength);
        }
        return newRecord;
    }

    /**
     * Returns the record to which the target file pointer belongs - meaning the
     * specified location in the file is part of the record data of the
     * RecordHeader which is returned. Returns null if the location is not part
     * of a record. (O(n) mem accesses)
     */
    protected TS_JdbAdvancedHeader getRecordAt(long targetFp) throws TS_JdbAdvancedException {
        var e = memIndex.elements();
        while (e.hasMoreElements()) {
            var next = (TS_JdbAdvancedHeader) e.nextElement();
            if (targetFp >= next.dataPointer
                    && targetFp < next.dataPointer + (long) next.dataCapacity) {
                return next;
            }
        }
        return null;
    }

    /**
     * Closes the database.
     */
    public synchronized void close() throws IOException, TS_JdbAdvancedException {
        try {
            super.close();
        } finally {
            memIndex.clear();
            memIndex = null;
        }
    }

    /**
     * Adds the new record to the in-memory index and calls the super class add
     * the index entry to the file.
     */
    protected void addEntryToIndex(String key, TS_JdbAdvancedHeader newRecord, int currentNumRecords) throws IOException, TS_JdbAdvancedException {
        super.addEntryToIndex(key, newRecord, currentNumRecords);
        memIndex.put(key, newRecord);
    }

    /**
     * Removes the record from the index. Replaces the target with the entry at
     * the end of the index.
     */
    protected void deleteEntryFromIndex(String key, TS_JdbAdvancedHeader header, int currentNumRecords) throws IOException, TS_JdbAdvancedException {
        super.deleteEntryFromIndex(key, header, currentNumRecords);
        var deleted = (TS_JdbAdvancedHeader) memIndex.remove(key);
    }
}
