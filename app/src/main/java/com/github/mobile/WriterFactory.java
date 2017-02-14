package com.github.mobile;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.zip.GZIPOutputStream;

/**
 * Created by mapang on 2/13/17.
 */

public class WriterFactory {
    public static Writer getWriter(String criteria, File file){
        final int FORMAT_VERSION = 4;

        if(criteria.equals("RequestWriter")){
            try {
                RandomAccessFile dir = new RandomAccessFile(file,"rw");
                ObjectOutputStream output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(dir.getFD()), 8192));
                new RequestWriter(file, FORMAT_VERSION, dir, output);
            }
            catch (IOException e){
                Log.d( "ReaderFactory","Exception writing cache " + file.getName(), e);
            }



        }
        return null;

    }
}
