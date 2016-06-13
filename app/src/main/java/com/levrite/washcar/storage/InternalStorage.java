package com.levrite.washcar.storage;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Michael Zaytsev on 25.05.2016.
 */
public class InternalStorage {

    public static void writeObject(Context context, String key, Object object) throws IOException{

        FileOutputStream fileOutputStream = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        fileOutputStream.close();

    }

    public static Object readObject(Context context, String key) throws IOException, ClassNotFoundException {

        FileInputStream fileInputStream = context.openFileInput(key);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object object = objectInputStream.readObject();
        return object;

    }

}
