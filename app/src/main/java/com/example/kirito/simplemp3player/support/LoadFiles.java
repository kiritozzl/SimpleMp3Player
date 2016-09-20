package com.example.kirito.simplemp3player.support;

import android.content.Context;
import android.os.AsyncTask;


import com.example.kirito.simplemp3player.entity.Mp3Item;

import java.io.File;
import java.util.List;

/**
 * Created by kirito on 2016/9/10.
 */
public class LoadFiles extends AsyncTask<String,Void,Void>{
    private CallBack callBack;
    private ShowProgress dialog;
    private List<Mp3Item> items;

    public LoadFiles(Context context) {
        dialog = new ShowProgress(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        items = new OpenMp3File().getMp3Paths(new File(params[0]));
        if (callBack != null && items != null){
            callBack.setListItem(items);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void mVoid) {
        super.onPostExecute(mVoid);
        dialog.dismiss();
    }

    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }

    public interface CallBack{
        void setListItem(List<Mp3Item> listItem);
    }


}
