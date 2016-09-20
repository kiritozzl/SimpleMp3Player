package com.example.kirito.simplemp3player.support;

import android.util.Log;

import com.example.kirito.simplemp3player.entity.Mp3Item;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kirito on 2016/9/20.
 */
public class OpenMp3File {
    private List<Mp3Item> items;
    private static final String TAG = "OpenMp3File";

    public OpenMp3File() {
        items = new ArrayList<>();
    }

    public List<Mp3Item> getMp3Paths(File f){
        File fs[] = f.listFiles();

        for (File file : fs){
            if (!file.isDirectory()){
                String name = file.getName();
                if (name.endsWith(".mp3")){
                    //Log.e(TAG, "getMp3Paths: name---"+name );
                    Mp3Item item = new Mp3Item();
                    item.setPath(file.getAbsolutePath());
                    item.setName(name);
                    item.setSize(calculateSize(file.length()));
                    item.setLast_modify_date(getLastModifyDate(file));

                    items.add(item);
                }
            }else if (file.isDirectory()){
                getMp3Paths(file);
            }
        }
        return items;
    }

    private String calculateSize(long size){
        String length = "";
        if (size > 1024 && size <= 1024 * 1024){
            length = size / 1024 + "KB";
        }else if (size > 1024 * 1024 && size <= 1024 * 1024 * 1024){
            length = size / 1024 / 1024 + "MB";
        }
        return length;
    }

    private String getLastModifyDate(File f){
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(f.lastModified()));
        return date;
    }
}
