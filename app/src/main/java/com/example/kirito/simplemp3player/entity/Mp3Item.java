package com.example.kirito.simplemp3player.entity;

/**
 * Created by kirito on 2016/9/20.
 */
public class Mp3Item {
    private String path;
    private String name;
    private String size;
    private String last_modify_date;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLast_modify_date() {
        return last_modify_date;
    }

    public void setLast_modify_date(String last_modify_date) {
        this.last_modify_date = last_modify_date;
    }
}
