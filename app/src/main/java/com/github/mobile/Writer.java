package com.github.mobile;


import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

/**
 * Created by mapang on 2/6/17.
 */

public interface Writer {
    <v> v write(v request);

   // void setAccess(RandomAccessFile access);
   // void setOutputStream(ObjectOutputStream stream);
}
