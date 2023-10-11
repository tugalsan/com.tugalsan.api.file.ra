package com.tugalsan.api.file.ra.server.indexed;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.optional.client.TGS_Optional;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class TS_FileRaIndexedFile extends TS_FileRaIndexedBase {

    /**
     * Hashtable which holds the in-memory index. For efficiency, the entire
     * index is cached in memory. The hashtable maps a key of type String to a
     * RecordHeader.
     */
    protected Hashtable memIndex;

    public static TGS_Optional<TS_FileRaIndexedFile> of(Path dbPath) {
        return TGS_UnSafe.call(() -> TS_FileUtils.isExistFile(dbPath) ? TGS_Optional.of(new TS_FileRaIndexedFile(dbPath, "rw")) : TGS_Optional.of(new TS_FileRaIndexedFile(dbPath, 64)), e -> TGS_Optional.ofEmpty(e.getClass().getSimpleName() + ":" + e.getMessage()));
    }

    /**
     * Creates a new database file. The initialSize parameter determines the
     * amount of space which is allocated for the index. The index can grow
     * dynamically, but the parameter is provide to increase efficiency.
     */
    private TS_FileRaIndexedFile(Path dbPath, int initialSize) throws IOException, TS_FileRaIndexedException {
        super(dbPath, initialSize);
        memIndex = new Hashtable(initialSize);
    }

    /**
     * Opens an existing database and initializes the in-memory index.
     */
    private TS_FileRaIndexedFile(Path dbPath, String accessFlags) throws IOException, TS_FileRaIndexedException {
        super(dbPath, accessFlags);
        var numRecords = readNumRecordsHeader();
        memIndex = new Hashtable(numRecords);
        for (int i = 0; i < numRecords; i++) {
            String key = readKeyFromIndex(i);
            TS_FileRaIndexedHeader header = readRecordHeaderFromIndex(i);
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
    protected TS_FileRaIndexedHeader keyToRecordHeader(String key) throws TS_FileRaIndexedException {
        var h = (TS_FileRaIndexedHeader) memIndex.get(key);
        if (h == null) {
            throw new TS_FileRaIndexedException("Key not found: " + key);
        }
        return h;
    }

    /**
     * This method searches the file for free space and then returns a
     * RecordHeader which uses the space. (O(n) memory accesses)
     */
    protected TS_FileRaIndexedHeader allocateRecord(String key, int dataLength) throws TS_FileRaIndexedException, IOException {
        // search for empty space
        TS_FileRaIndexedHeader newRecord = null;
        var e = memIndex.elements();
        while (e.hasMoreElements()) {
            var next = (TS_FileRaIndexedHeader) e.nextElement();
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
            newRecord = new TS_FileRaIndexedHeader(fp, dataLength);
        }
        return newRecord;
    }

    /**
     * Returns the record to which the target file pointer belongs - meaning the
     * specified location in the file is part of the record data of the
     * RecordHeader which is returned. Returns null if the location is not part
     * of a record. (O(n) mem accesses)
     */
    protected TS_FileRaIndexedHeader getRecordAt(long targetFp) throws TS_FileRaIndexedException {
        var e = memIndex.elements();
        while (e.hasMoreElements()) {
            var next = (TS_FileRaIndexedHeader) e.nextElement();
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
    public synchronized void close() throws IOException, TS_FileRaIndexedException {
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
    protected void addEntryToIndex(String key, TS_FileRaIndexedHeader newRecord, int currentNumRecords) throws IOException, TS_FileRaIndexedException {
        super.addEntryToIndex(key, newRecord, currentNumRecords);
        memIndex.put(key, newRecord);
    }

    /**
     * Removes the record from the index. Replaces the target with the entry at
     * the end of the index.
     */
    protected void deleteEntryFromIndex(String key, TS_FileRaIndexedHeader header, int currentNumRecords) throws IOException, TS_FileRaIndexedException {
        super.deleteEntryFromIndex(key, header, currentNumRecords);
        var deleted = (TS_FileRaIndexedHeader) memIndex.remove(key);
    }
}
