package fr.unice.reneviergonin.demomulti;


import android.content.res.AssetFileDescriptor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.test.espresso.ViewAction;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import fr.unice.reneviergonin.demomulti.images.MyPolygon;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static fr.unice.reneviergonin.demomulti.MyViewAction.pamatreziedSingleTouchAt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class TestHorizontale {

    @Rule
    public ActivityTestRule<DemoMulti> mActivityRule = new ActivityTestRule<>(DemoMulti.class);

    // adb -e pull /sdcard/Android/data/fr.unice.reneviergonin.demomulti/files/2018-01-24-11-51-24.csv E:\.
    // ou adb.exe -e pull /storage/emulated/0/Android/data/fr.unice.reneviergonin.demomulti/files/2019-02-05-17-17-41.csv E:\.
    // *.csv : cela ne fonctionne pas...
    // mais ceci prend tout le dossier : adb.exe -e pull /storage/emulated/0/Android/data/fr.unice.reneviergonin.demomulti/files E:\.
    // source : https://stackoverflow.com/questions/10050925/how-do-i-adb-pull-all-files-of-a-folder-present-in-sd-card

    int [] files = {  R.raw.f3, R.raw.spirale2 , R.raw.spirale, R.raw.horizontale1, R.raw.horizontale2, R.raw.vague, R.raw.diagonale, R.raw.verticale, R.raw.petit};
    boolean [] estHorinzontal = { false, false, false, true, true, false, false, false, false};

    @Before
    public void startTest() {


        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }


    class TouchInfo {
        private long dateCreation;
        private int action;
        private int meta;
        private long dateDown;
        private float x;
        private float y;
        // 8360273;8360320;2;570.34375;584.46826;0

        TouchInfo(String d1, String d2, String action, String x, String y, String meta) {
            this.dateDown = Long.parseLong(d1);
            this.dateCreation = Long.parseLong(d2);
            this.action = Integer.parseInt(action);
            this.x = Float.parseFloat(x);
            this.y = Float.parseFloat(y);
            this.meta = Integer.parseInt(meta);
        }

        ViewAction getViewAction() {
            return pamatreziedSingleTouchAt(x, y, action, dateDown, dateCreation, meta);
        }
    }


    /**
     * On part du principe : on appuie, on déplace et on relève le doit
     * ici avec une erreur : le premier point (down) et le dernier point (up) ne sont pas mémorisés
     */
    public void drawAndTest(TouchInfo [] events, boolean estHorizontal) {
        // on dessine

        DemoMulti demo = mActivityRule.getActivity();
        MyView myView = (MyView) demo.findViewById(R.id.draw);
        int limite = events.length;
        int x = 0;


        for(TouchInfo t : events) {
            x++;
            onView(withId(R.id.draw)).perform(t.getViewAction());
            try {
                TimeUnit.MILLISECONDS.sleep(100); // pour avoir le temps de voir
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if ( x < limite) {
                // après le dernier point, il y a un "up", donc plus de current... mais un last
                assertEquals("taille de la liste de point", x, myView.current[0].getNbPoints());
            }
            else if (x == limite) {
                MyPolygon[] last = myView.last; // modif pour P
                assertEquals("taille de la liste de point", x, last[0].getNbPoints());
            }

        }


        MyPolygon[] last = myView.last; // modif pour P
        float  [] recordedPoints = last[0].getPoints();
        assertEquals("taille des points enregistrés", limite*2, recordedPoints.length);
        for(int i = 0; i < recordedPoints.length; i = i+2) {
            assertEquals("coord x des points enregistrés",events[i/2].x,recordedPoints[i],0);
            assertEquals("coord y des points enregistrés",events[i/2].y,recordedPoints[i+1],0);
        }

        assertEquals("test d'horizontal", estHorizontal, myView.estHorizontal(0));

    }




    @Test
    public void loadAndTest() {
        for(int i = 0; i < files.length; i++) {
            int id = files[i];
            try {
            // InputStream input = mActivityRule.getActivity().getResources().openRawResource(id);
            AssetFileDescriptor afd = mActivityRule.getActivity().getResources().openRawResourceFd(id);
            InputStream is = afd.createInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line = "";
            ArrayList<TouchInfo> list = new ArrayList<TouchInfo>();

                while ((line = reader.readLine()) != null) {
                    String[]event = line.split(";");
                    if ((event != null) && (event.length == 6 )) {
                        TouchInfo t = new TouchInfo(event[0], event[1], event[2], event[3], event[4], event[5]);
                        list.add(t);
                    }
                }


                TouchInfo [] events = new TouchInfo[list.size()];
                events = list.toArray(events);


                drawAndTest(events, estHorinzontal[i]);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}