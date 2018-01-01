
package domain_name.example.com.musicplayer;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity{

    private ListView listView;
    private String[] fileNames = new String[100];
    private ArrayAdapter adapter;
    private MediaPlayer mp;
    private int currentPosition=0;
    private final int resId[] = {R.raw.first, R.raw.second};
    private Field[] fields;
    private int whichTrackSelected = -99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        fields = R.raw.class.getFields();
        for(int i=0;i<fields.length;i++){
            fileNames[i]=fields[i].getName();
        }

        String[] str = new String[fields.length];
        for(int i=0;i<fields.length;i++){
            str[i]=fields[i].getName();
        }
        String na[]=new String[fields.length];
        for(int i=0;i<fields.length;i++){
            na[i]=str[i];
        }
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, na);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String whichView = String.valueOf(l);

                play(resId[(int) l]);
            }
        });
    }
    @Override
    protected void onRestart() {


        mp.start();
        mp.seekTo(currentPosition);
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        //counter = 78;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentPosition = mp.getCurrentPosition();
        mp.pause();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void play(int resId){
        if(whichTrackSelected!=resId) {
            if(mp != null){
                mp.reset();
                mp.release();
            }
            whichTrackSelected = (int) resId;
            mp = MediaPlayer.create(this, resId);
            mp.start();
        }
    }
}