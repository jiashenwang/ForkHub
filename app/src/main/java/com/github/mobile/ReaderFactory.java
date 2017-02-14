package com.github.mobile;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.util.zip.GZIPInputStream;

/**
 * Created by mapang on 2/13/17.
 */

public class ReaderFactory {
    public static Reader getReader(String criteria, File file){
        final int FORMAT_VERSION = 4;
        if(criteria.equals("RequestReader")){
            try {
                RandomAccessFile dir = new RandomAccessFile(file, "rw");
                FileInputStream inputStream = new FileInputStream(dir.getFD());
                GZIPInputStream gZipInput = new GZIPInputStream(inputStream, 8192 * 8);
                ObjectInputStream input = new ObjectInputStream(gZipInput);
                return new RequestReader(file, FORMAT_VERSION, dir, input).read();
            }
            catch (IOException e){
                Log.d( "ReaderFactory","Exception writing cache " + file.getName(), e);
            }



        }
        return null;

    }
}
