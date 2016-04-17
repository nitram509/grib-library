package org.meteogroup.griblibrary.util;

import java.io.IOException;
import java.io.RandomAccessFile;

public class IoUtils {
    public static void closeSilent(RandomAccessFile randomAccessFile) {
        try {
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        } catch (IOException ignore) {
            // ignore
        }
    }
}
