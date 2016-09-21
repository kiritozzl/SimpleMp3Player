package com.example.kirito.simplemp3player;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.kirito.simplemp3player.adapter.ListAdapter;
import com.example.kirito.simplemp3player.entity.Mp3Item;
import com.example.kirito.simplemp3player.support.LoadFiles;
import com.example.kirito.simplemp3player.support.OpenMp3File;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton ib_last;
    private ImageButton ib_play;
    private ImageButton ib_next;
    private SeekBar sb;
    private ListView lv;

    private String path;
    private List<Mp3Item> items;
    private ListAdapter adapter;

    private MediaPlayer player;
    private String ph;
    private boolean isPlay = false;
    private int last,next,now;
    private android.os.Handler mHandler = new android.os.Handler();
    private String music_title;

    @BindView(R.id.tv_total)
    TextView tv_total;
    @BindView(R.id.tv_current)
    TextView tv_current;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setView();
        //path = Environment.getExternalStorageDirectory().getAbsolutePath();
        //Log.e(TAG, "onCreate: external path ---"+ Environment.getExternalStorageDirectory().getAbsolutePath());
        path = "/storage/sdcard1";
        loadData();
    }

    private void loadData(){
        LoadFiles load = new LoadFiles(this);
        load.setCallBack(new LoadFiles.CallBack() {
            @Override
            public void setListItem(List<Mp3Item> listItem) {
                adapter = new ListAdapter(MainActivity.this,listItem);
                items = listItem;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                last = position - 1;
                                next = position + 1;
                                Mp3Item item = (Mp3Item) parent.getItemAtPosition(position);
                                ph = item.getPath();
                                music_title = item.getName();
                                if (player != null){
                                    stopPlayer();
                                }
                                playMp3(ph,music_title);

                                if (isPlay){
                                    ib_play.setImageResource(R.drawable.stop);
                                }
                            }
                        });
                    }
                });
            }
        });
        load.execute(path);
    }

    private void playMp3(String path,String m_name){
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(MainActivity.this, Uri.parse(path));
            player.prepare();
            player.start();
            setMusicTitle(m_name);
            updateSeekBar();
            isPlay = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //ib_play.setImageResource(R.drawable.play);

                playNext();
            }
        });
    }

    private void setMusicTitle(String s){
        setTitle(s);
    }

    private void updateSeekBar(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (player != null){
                    int mCurrentPosition = player.getCurrentPosition() / 1000;
                    int total = player.getDuration() / 1000;
                    sb.setProgress(mCurrentPosition);
                    sb.setMax(total);
                    tv_total.setText(calculateTime(total));
                    tv_current.setText(calculateTime(mCurrentPosition));
                }
                mHandler.postDelayed(this,1000);
            }
        });
    }

    private String calculateTime(int time){
        int minute;
        int second;
        if (time >= 60){
            minute = time / 60;
            second = time % 60;
            return minute + ":" +second;
        }else if (time < 60){
            second = time;
            return "0:" + second;
        }
        return null;
    }

    private void setView(){
        ib_last = (ImageButton) findViewById(R.id.ib_last);
        ib_play = (ImageButton) findViewById(R.id.ib_play);
        ib_next = (ImageButton) findViewById(R.id.ib_next);
        ib_play.setOnClickListener(this);
        ib_last.setOnClickListener(this);
        ib_next.setOnClickListener(this);
        sb = (SeekBar) findViewById(R.id.sb);
        lv = (ListView) findViewById(R.id.lv);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player != null && fromUser){
                    player.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_play){
            if (!isPlay && path != null && player!= null){
                player.start();
                isPlay = true;
                ib_play.setImageResource(R.drawable.stop);
            }else if(isPlay){
                player.pause();
                isPlay = false;
                ib_play.setImageResource(R.drawable.play);
            }
        }else if (v.getId() == R.id.ib_last){
            playLast();
        }else if (v.getId() == R.id.ib_next){
            playNext();
        }
    }

    private void playNext(){
        if (next <= items.size()){
            stopPlayer();
            Mp3Item next_item = items.get(next);
            playMp3(next_item.getPath(),next_item.getName());
            now = next;
            last = now - 1;
            next = now + 1;
        }
    }

    private void playLast(){
        if (last >= 0){
            stopPlayer();
            Mp3Item last_item = items.get(last);
            playMp3(last_item.getPath(),last_item.getName());
            now = last;
            last = now - 1;
            next = now + 1;
        }
    }

    private void stopPlayer(){
        isPlay = false;
        player.stop();
        player.release();
    }
}
