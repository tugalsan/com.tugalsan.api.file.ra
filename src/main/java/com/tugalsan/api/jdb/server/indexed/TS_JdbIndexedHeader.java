package com.tugalsan.api.jdb.server.indexed;

import java.io.*;

public class TS_JdbIndexedHeader {

    /**
     * File pointer to the first byte of record data (8 bytes).
     */
    protected long dataPointer;
    /**
     * Actual number of bytes of data held in this record (4 bytes).
     */
    protected int dataCount;
    /**
     * Number of bytes of data that this record can hold (4 bytes).
     */
    protected int dataCapacity;
    /**
     * Indicates this header's position in the file index.
     */
    protected int indexPosition;

    protected TS_JdbIndexedHeader() {
    }

    protected TS_JdbIndexedHeader(long dataPointer, int dataCapacity) {
        if (dataCapacity < 1) {
            throw new IllegalArgumentException("Bad record size: " + dataCapacity);
        }
        this.dataPointer = dataPointer;
        this.dataCapacity = dataCapacity;
        this.dataCount = 0;
    }

    protected int getIndexPosition() {
        return indexPosition;
    }

    protected void setIndexPosition(int indexPosition) {
        this.indexPosition = indexPosition;
    }

    protected int getDataCapacity() {
        return dataCapacity;
    }

    protected int getFreeSpace() {
        return dataCapacity - dataCount;
    }

    protected void read(DataInput in) throws IOException {
        dataPointer = in.readLong();
        dataCapacity = in.readInt();
        dataCount = in.readInt();
    }

    protected void write(DataOutput out) throws IOException {
        out.writeLong(dataPointer);
        out.writeInt(dataCapacity);
        out.writeInt(dataCount);
    }

    protected static TS_JdbIndexedHeader readHeader(DataInput in) throws IOException {
        var r = new TS_JdbIndexedHeader();
        r.read(in);
        return r;
    }

    /**
     * Returns a new record header which occupies the free space of this record.
     * Shrinks this record size by the size of its free space.
     */
    protected TS_JdbIndexedHeader split() throws TS_JdbIndexedException {
        var newFp = dataPointer + (long) dataCount;
        var newRecord = new TS_JdbIndexedHeader(newFp, getFreeSpace());
        dataCapacity = dataCount;
        return newRecord;
    }
}
