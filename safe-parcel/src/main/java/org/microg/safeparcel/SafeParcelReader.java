/*
 * Copyright (C) 2013-2017 microG Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.microg.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

@SuppressWarnings("MagicNumber")
public final class SafeParcelReader {

    private SafeParcelReader() {
    }

    public static int halfOf(int i) {
        return i & 0xFFFF;
    }

    public static int readSingleInt(Parcel parcel) {
        return parcel.readInt();
    }

    private static int readStart(Parcel parcel, int first) {
        if ((first & 0xFFFF0000) != -65536)
            return first >> 16 & 0xFFFF;
        return parcel.readInt();
    }

    private static void readStart(Parcel parcel, int position, int length) {
        int i = readStart(parcel, position);
        if (i != length)
            throw new ReadException("Expected size " + length + " got " + i + " (0x" + Integer.toHexString(i) + ")", parcel);
    }

    public static int readStart(Parcel parcel) {
        int first = readSingleInt(parcel);
        int length = readStart(parcel, first);
        int start = parcel.dataPosition();
        if (halfOf(first) != SafeParcelable.SAFE_PARCEL_MAGIC)
            throw new ReadException("Expected object header. Got 0x" + Integer.toHexString(first), parcel);
        int end = start + length;
        if ((end < start) || (end > parcel.dataSize()))
            throw new ReadException("Size read is invalid start=" + start + " end=" + end, parcel);
        return end;
    }

    public static int readInt(Parcel parcel, int position) {
        readStart(parcel, position, 4);
        return parcel.readInt();
    }

    public static byte readByte(Parcel parcel, int position) {
        readStart(parcel, position, 4);
        return (byte) parcel.readInt();
    }

    public static short readShort(Parcel parcel, int position) {
        readStart(parcel, position, 4);
        return (short) parcel.readInt();
    }

    public static boolean readBool(Parcel parcel, int position) {
        readStart(parcel, position, 4);
        return parcel.readInt() != 0;
    }

    public static long readLong(Parcel parcel, int position) {
        readStart(parcel, position, 8);
        return parcel.readLong();
    }

    public static float readFloat(Parcel parcel, int position) {
        readStart(parcel, position, 4);
        return parcel.readFloat();
    }

    public static double readDouble(Parcel parcel, int position) {
        readStart(parcel, position, 8);
        return parcel.readDouble();
    }

    public static String readString(Parcel parcel, int position) {
        int length = readStart(parcel, position);
        int start = parcel.dataPosition();
        if (length == 0)
            return null;
        String string = parcel.readString();
        parcel.setDataPosition(start + length);
        return string;
    }

    public static IBinder readBinder(Parcel parcel, int position) {
        int length = readStart(parcel, position);
        int start = parcel.dataPosition();
        if (length == 0)
            return null;
        IBinder binder = parcel.readStrongBinder();
        parcel.setDataPosition(start + length);
        return binder;
    }

    public static <T extends Parcelable> T readParcelable(Parcel parcel, int position, Parcelable.Creator<T> creator) {
        int length = readStart(parcel, position);
        int start = parcel.dataPosition();
        if (length == 0)
            return null;
        T t = creator.createFromParcel(parcel);
        parcel.setDataPosition(start + length);
        return t;
    }

    public static ArrayList readList(Parcel parcel, int position, ClassLoader classLoader) {
        int length = readStart(parcel, position);
        int start = parcel.dataPosition();
        if (length == 0)
            return null;
        ArrayList list = parcel.readArrayList(classLoader);
        parcel.setDataPosition(start + length);
        return list;
    }

    public static <T extends Parcelable> ArrayList<T> readParcelableList(Parcel parcel, int position, Parcelable.Creator<T> creator) {
        int length = readStart(parcel, position);
        int start = parcel.dataPosition();
        if (length == 0)
            return null;
        ArrayList<T> list = parcel.createTypedArrayList(creator);
        parcel.setDataPosition(start + length);
        return list;
    }

    public static ArrayList<String> readStringList(Parcel parcel, int position) {
        int length = readStart(parcel, position);
        int start = parcel.dataPosition();
        if (length == 0)
            return null;
        ArrayList<String> list = parcel.createStringArrayList();
        parcel.setDataPosition(start + length);
        return list;
    }

    public static <T extends Parcelable> T[] readParcelableArray(Parcel parcel, int position, Parcelable.Creator<T> creator) {
        int length = readStart(parcel, position);
        int start = parcel.dataPosition();
        if (length == 0)
            return null;
        T[] arr = parcel.createTypedArray(creator);
        parcel.setDataPosition(start + length);
        return arr;
    }

    public static String[] readStringArray(Parcel parcel, int position) {
        int length = readStart(parcel, position);
        int start = parcel.dataPosition();
        if (length == 0)
            return null;
        String[] arr = parcel.createStringArray();
        parcel.setDataPosition(start + length);
        return arr;
    }

    public static byte[] readByteArray(Parcel parcel, int position) {
        int length = readStart(parcel, position);
        int start = parcel.dataPosition();
        if (length == 0)
            return null;
        byte[] arr = parcel.createByteArray();
        parcel.setDataPosition(start + length);
        return arr;
    }

    public static int[] readIntArray(Parcel parcel, int position) {
        int length = readStart(parcel, position);
        int start = parcel.dataPosition();
        if (length == 0)
            return null;
        int[] arr = parcel.createIntArray();
        parcel.setDataPosition(start + length);
        return arr;
    }

    public static Bundle readBundle(Parcel parcel, int position, ClassLoader classLoader) {
        int length = readStart(parcel, position);
        int start = parcel.dataPosition();
        if (length == 0)
            return null;
        Bundle bundle = parcel.readBundle(classLoader);
        parcel.setDataPosition(start + length);
        return bundle;
    }

    public static void skip(Parcel parcel, int position) {
        int i = readStart(parcel, position);
        parcel.setDataPosition(parcel.dataPosition() + i);
    }

    public static class ReadException extends RuntimeException {
        public ReadException(String message, Parcel parcel) {
            super(message);
        }
    }
}
