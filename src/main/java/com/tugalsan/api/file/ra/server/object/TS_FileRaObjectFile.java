package com.tugalsan.api.file.ra.server.object;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TS_FileRaObjectFile extends TS_FileRaObjectBase {

    /**
     * Hashtable which holds the in-memory index. For efficiency, the entire
     * index is cached in memory. The hashtable maps a key of type String to a
     * RecordHeader.
     */
    protected ConcurrentHashMap memIndex;

    public static TGS_UnionExcuse<TS_FileRaObjectFile> of(Path dbPath) {
        try {
            if (TS_FileUtils.isExistFile(dbPath)) {
                return TGS_UnionExcuse.of(new TS_FileRaObjectFile(dbPath, "rw"));
            }
            return TGS_UnionExcuse.of(new TS_FileRaObjectFile(dbPath, 64));
        } catch (IOException | TS_FileRaObjectException ex) {
            return TGS_UnionExcuse.ofExcuse(ex);
        }
    }

    /**
     * Creates a new database file. The initialSize parameter determines the
     * amount of space which is allocated for the index. The index can grow
     * dynamically, but the parameter is provide to increase efficiency.
     */
    private TS_FileRaObjectFile(Path dbPath, int initialSize) throws IOException, TS_FileRaObjectException {
        super(dbPath, initialSize);
        memIndex = new ConcurrentHashMap(initialSize);
    }

    /**
     * Opens an existing database and initializes the in-memory index.
     */
    private TS_FileRaObjectFile(Path dbPath, String accessFlags) throws IOException, TS_FileRaObjectException {
        super(dbPath, accessFlags);
        var numRecords = readNumRecordsHeader();
        memIndex = new ConcurrentHashMap(numRecords);
        for (var i = 0; i < numRecords; i++) {
            var key = readKeyFromIndex(i);
            var header = readRecordHeaderFromIndex(i);
            header.setIndexPosition(i);
            memIndex.put(key, header);
        }
    }

    /**
     * Returns an enumeration of all the keys in the database.
     */
    @Override
    public synchronized Enumeration enumerateKeys() {
        return memIndex.keys();
    }

    /**
     * Returns the current number of records in the database.
     */
    @Override
    public synchronized int getNumRecords() {
        return memIndex.size();
    }

    /**
     * Checks if there is a record belonging to the given key.
     */
    @Override
    public synchronized boolean recordExists(String key) {
        return memIndex.containsKey(key);
    }

    /**
     * Maps a key to a record header by looking it up in the in-memory index.
     */
    @Override
    protected TS_FileRaObjectHeader keyToRecordHeader(String key) throws TS_FileRaObjectException {
        var h = (TS_FileRaObjectHeader) memIndex.get(key);
        if (h == null) {
            throw new TS_FileRaObjectException("Key not found: " + key);
        }
        return h;
    }

    /**
     * This method searches the file for free space and then returns a
     * RecordHeader which uses the space. (O(n) memory accesses)
     */
    @Override
    protected TS_FileRaObjectHeader allocateRecord(String key, int dataLength) throws TS_FileRaObjectException, IOException {
        // search for empty space
        TS_FileRaObjectHeader newRecord = null;
        var e = memIndex.elements();
        while (e.hasMoreElements()) {
            var next = (TS_FileRaObjectHeader) e.nextElement();
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
            newRecord = new TS_FileRaObjectHeader(fp, dataLength);
        }
        return newRecord;
    }

    /**
     * Returns the record to which the target file pointer belongs - meaning the
     * specified location in the file is part of the record data of the
     * RecordHeader which is returned. Returns null if the location is not part
     * of a record. (O(n) mem accesses)
     */
    @Override
    protected TS_FileRaObjectHeader getRecordAt(long targetFp) throws TS_FileRaObjectException {
        var e = memIndex.elements();
        while (e.hasMoreElements()) {
            var next = (TS_FileRaObjectHeader) e.nextElement();
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
    @Override
    public synchronized void close() throws IOException, TS_FileRaObjectException {
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
    @Override
    protected void addEntryToIndex(String key, TS_FileRaObjectHeader newRecord, int currentNumRecords) throws IOException, TS_FileRaObjectException {
        super.addEntryToIndex(key, newRecord, currentNumRecords);
        memIndex.put(key, newRecord);
    }

    /**
     * Removes the record from the index. Replaces the target with the entry at
     * the end of the index.
     */
    @Override
    protected void deleteEntryFromIndex(String key, TS_FileRaObjectHeader header, int currentNumRecords) throws IOException, TS_FileRaObjectException {
        super.deleteEntryFromIndex(key, header, currentNumRecords);
        var deleted = (TS_FileRaObjectHeader) memIndex.remove(key);
    }
}
