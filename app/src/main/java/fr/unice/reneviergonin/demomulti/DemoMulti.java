package fr.unice.reneviergonin.demomulti;

import android.app.Activity;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DemoMulti extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_multi);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.demo_multi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean consomme = false;

        switch (item.getItemId()) {
            case R.id.save:

                MyView v = (MyView) findViewById(R.id.draw);


                ArrayList<MotionEvent> list = v.getEvents();

                // ouverture du fichier avec pour nom une date
                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                File fileLog = new File(getExternalFilesDir(null), format.format(now)+".csv");


                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileLog, true)));
                    for(MotionEvent e : list) {
                        String line =""+e.getDownTime()+";"+e.getEventTime()+";"+e.getAction()+";"+e.getX()+";"+e.getY()+";"+e.getMetaState();
                        out.println(line);
                    }

                    out.flush();
                    out.close();

                    // pour que le fichier soit visible de l'extérieur plus rapidement
                    scanFile(fileLog.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }


                consomme = true;
                v.reset();
                v.invalidate();
                break;
        }

        return consomme;
    }




    // pour faire apparaitre le fichier plus vite sur les ordinateurs connectés
    private void scanFile(String path) {

        MediaScannerConnection.scanFile(this,
                new String[]{path},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
    }

    }
