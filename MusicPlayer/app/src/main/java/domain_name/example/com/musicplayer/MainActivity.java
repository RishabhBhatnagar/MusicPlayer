package domain_name.example.com.musicplayer;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity{

    //XML components(widgets).
    private SeekBar seekbar;
    private ListView listView;
    private TextView textView;
    private NavigationView sideNavigationMenu;

    private String[] fileNames = new String[100];
    private ArrayAdapter adapter;
    private MediaPlayer mp;
    private final int resId[] = {R.raw.callertune,R.raw.chanda_1463333735,
            R.raw.deewani_1463333479,R.raw.ekadantaya_1460230358,
            R.raw.exclusive_1463331861,R.raw.first,R.raw.g_1463333685,R.raw.hanuman_1463334898,R.raw.hanuman_1463334898,R.raw.i1460228566,
            R.raw.iddarammayilatho_1460228389,R.raw.iddarammayilatho_1460230577,R.raw.iddarammayilatho_1460230648,
            R.raw.ilahi_1463332103,R.raw.jabra_1463333798,R.raw.jeena_1463333563, R.raw.jesus_1463334280,R.raw.ls_1463333851, R.raw.lyrical_1463332586, R.raw.mast_1463332259,R.raw.pinga_1463332673,R.raw.second
    };
    private Field[] fields;
    private String[] namesOfFiles;


    private int whichTrackSelected = -99;
    private int progress=0;
    private static final int maxProgress = 100;
    private int currentPosition=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Initializing all xml components*/
        listView = findViewById(R.id.listView);
        seekbar = findViewById(R.id.seekBar);
        textView = findViewById(R.id.textView);
        sideNavigationMenu = findViewById(R.id.sideNaviigationMenu);

        namesOfFiles = new String[1000];


        //Seekbar settings:
        seekbar.setMax(maxProgress);
        seekbar.setProgress(progress);

        fields = R.raw.class.getFields();

        String[] nameOfSongs = new String[fields.length];
        for(int i=0;i<fields.length;i++){
            nameOfSongs[i] = fields[i].getName();
        }


        //Setting names of files to listView via an adapter.
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, nameOfSongs);
        listView.setAdapter(adapter);

        //Playing song on clicked.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                play(resId[(int) l]);
            }
        });


        /*Code to implement if user changes the seek position.*/
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if(mp != null && isFromUser){
                    mp.seekTo(progress);
                    textView.setText("C to : "+String.valueOf(progress));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });//end

        // Playing first three songs on respective click of navMenu items (for testing).
        sideNavigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch(itemId){
                    case R.id.newAccount : play(resId[0]);break;
                    case R.id.newSettings : play(resId[1]);break;
                    case R.id.three : play(resId[2]);break;
                }
                return true;
            }
        });

    }


    @Override
    protected void onRestart() {
      //Set back where last stopped.
        mp.start();
        mp.seekTo(currentPosition);
        super.onRestart();
    }

    @Override
    protected void onPause() {
        //store the current position and pause the media.
        currentPosition = mp.getCurrentPosition();
        mp.pause();
        super.onPause();
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void play(int resId){
        if(whichTrackSelected!=resId || !mp.isPlaying()) {                            //When selected track is not equal to already playing track.
            if(mp != null){                                        //When media player was already plaaying a track and another one was selected.
                mp.reset();
                mp.release();
            }

            whichTrackSelected = (int) resId;
            mp = MediaPlayer.create(this, resId);
            mp.start();
            textView.setText(String.valueOf(mp.getDuration()));


    //    Update the seekbar at every fraction of the total duration of the progress of playing track.
            final Handler handler = new Handler();


            MainActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    seekbar.setMax(mp.getDuration());
                    if(mp != null){
                        int mCurrentPosition = mp.getCurrentPosition();
                        currentPosition = mCurrentPosition;
                        seekbar.setProgress(mCurrentPosition);
                        textView.setText(String.valueOf(mCurrentPosition));
                        if(mCurrentPosition>=seekbar.getMax()){
                            textView.setText(String.valueOf(mCurrentPosition)+" Max reached");
                        }
                    }
                    handler.postDelayed(this, 1);
                }
            });

        }
    }
}