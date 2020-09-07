package com.example.audioplayer;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private TextView artist_name;
    private ImageView album_image;
    private TextView song_name;
    private SeekBar timeline;
    private Button play;
    private Button left;
    private Button right;
    private MediaPlayer song;
    private ImageView header;

    private int[] id_songs = {
            R.raw.eurus,
            R.raw.soldier_poet_king,
            R.raw.el_capitan,
            R.raw.tiro
    };

    private int[] id_images_albums = {
            R.drawable.thehellos,
            R.drawable.hellos2,
            R.drawable.capitain,
            R.drawable.scatolove,
    };

    private int[] colors = {
            R.color.eurus,
            R.color.hallos2,
            R.color.capitain,
            R.color.scatolove,

    };

    private String[][] albums = {
            {"The Oh Hellos", "Eurus"},
            {"The Oh Hellos","Soldier, Poet and King"},
            {"Colony house","El Capitain"},
            {"Scatolove","O Tiro"},
    };

    private final int FIRST_POSITION = 0;
    private final int LATEST_POSITION = id_songs.length  - 1;
    private int CURRENT_POSITION;

    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAllVariables();

    }



    public void play(View view) {
        if (!song.isPlaying()){
            song.start();
        } else {
            song.pause();
        }

        TimelineSong();
    }


    private void TimelineSong(){
        //Make sure you update Seekbar on UI thread
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(song != null){
                    int mCurrentPosition = song.getCurrentPosition() / 1000;
                    timeline.setProgress(mCurrentPosition);


                    // change the icon if is playing or not
                    if(song.isPlaying()){
                        play.setBackgroundResource(R.drawable.ic_play);

                    } else {
                        play.setBackgroundResource(R.drawable.ic_pause);

                        // if the song is finish
                        if(timeline.getProgress() >= timeline.getMax()  - 1){
                            next();
                        }
                    }
                }
                mHandler.postDelayed(this, 1000);
            }
        });

    }


    public void left(View view) {
        back();
    }

    public void right(View view) {
        next();
    }


    private void setSong(){
        song = MediaPlayer.create(getApplicationContext(), id_songs[CURRENT_POSITION]);
        album_image.setImageResource(id_images_albums[CURRENT_POSITION]);
        artist_name.setText(albums[CURRENT_POSITION][0]);
        song_name.setText(albums[CURRENT_POSITION][1]);
        header.setColorFilter(ContextCompat.getColor(getApplicationContext(),colors[CURRENT_POSITION]), android.graphics.PorterDuff.Mode.SRC_IN);
        timeline.getProgressDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), colors[CURRENT_POSITION]), PorterDuff.Mode.SRC_ATOP);
        timeline.getThumb().setColorFilter(ContextCompat.getColor(getApplicationContext(), colors[CURRENT_POSITION]), PorterDuff.Mode.SRC_ATOP);
        setTopColor();
        changeOpacityDirectionButton(left, FIRST_POSITION);
        changeOpacityDirectionButton(right, LATEST_POSITION);
        timeline.setMax(song.getDuration()/1000);
    }


    private void setTopColor(){

        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), colors[CURRENT_POSITION]));
    }


    private void changeOpacityDirectionButton(Button direction, int position){
        if(CURRENT_POSITION == position){
            direction.getBackground().setAlpha(76);
        }else{
            direction.getBackground().setAlpha(255);
        }
    }



    private void setAllVariables(){
        album_image = findViewById(R.id.album_image);
        artist_name = findViewById(R.id.album_name);
        song_name = findViewById(R.id.song_name);
        timeline = findViewById(R.id.timeline);
        play = findViewById(R.id.play);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        header = findViewById(R.id.imageView);
        CURRENT_POSITION = FIRST_POSITION;

        setSong();
        timeline.setMax(song.getDuration()/1000);
        timeline.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(song != null && fromUser){
                    song.seekTo(progress * 1000);
                }

            }


        });


    }

    private  void next(){
        CURRENT_POSITION++;
        if (CURRENT_POSITION <= LATEST_POSITION) {
            song.reset();
            setSong();
            song.start();
        } else {
            CURRENT_POSITION = LATEST_POSITION;
        }
    }

    private void back(){
        CURRENT_POSITION--;

        if (CURRENT_POSITION >= FIRST_POSITION) {
            song.reset();
            setSong();
            song.start();
        }else {
            CURRENT_POSITION = 0;
        }
    }
}
