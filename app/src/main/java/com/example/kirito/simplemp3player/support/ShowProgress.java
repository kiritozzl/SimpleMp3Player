package com.example.kirito.simplemp3player.support;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by kirito on 2016/9/10.
 */
public class ShowProgress {
    private ProgressDialog dialog;

    public ShowProgress(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setTitle("加载中...");
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
