package fr.unice.reneviergonin.demomulti;



import android.view.MotionEvent;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import fr.unice.reneviergonin.demomulti.images.MyPolygon;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static fr.unice.reneviergonin.demomulti.MyViewAction.singleTouchAt;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class TestPointsEnregistrés {

    static boolean PAUSE = false;

    @Rule
    public ActivityTestRule<DemoMulti> mActivityRule = new ActivityTestRule<>(DemoMulti.class);


    @Before
    public void startTest() {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }


    /**
     * illustration d'ouverture de menu
    @Test
    public void menu() {
        openActionBarOverflowOrOptionsMenu(mActivityRule.getActivity());

        try {
            if (PAUSE) TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    */

    /**
     * On part du principe : on appuie, on déplace et on relève le doit
     * ici avec une erreur avant version2 dans MyView : le premier point (down) et le dernier point (up) ne sont pas mémorisés
     */
    @Test
    public void DiagonaleDescendanteSansVérification() {
        // on dessine

        DemoMulti demo = mActivityRule.getActivity();
        MyView myView = (MyView) demo.findViewById(R.id.draw);
        int limite = 150;


        int x = 0;
        onView(withId(R.id.draw)).perform(singleTouchAt(x, x, MotionEvent.ACTION_DOWN));

        for(x = 1; x < limite; x++) {
            onView(withId(R.id.draw)).perform(singleTouchAt(x, x, MotionEvent.ACTION_MOVE));

            try {
                if (PAUSE) TimeUnit.MILLISECONDS.sleep(50); // pour avoir le temps de voir...
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        onView(withId(R.id.draw)).perform(singleTouchAt(x, x, MotionEvent.ACTION_UP));


    }




    @Test
    public void DiagonaleDescendanteAvecVérificationJUnit() {
        // on verifie le nombre de point

        DemoMulti demo = mActivityRule.getActivity();
        MyView myView = (MyView) demo.findViewById(R.id.draw);
        int limite = 150;


        int x = 0;
        onView(withId(R.id.draw)).perform(singleTouchAt(x, x, MotionEvent.ACTION_DOWN));
        assertEquals("taille de la liste de point", x + 1, myView.current[0].getNbPoints());

        for(x = 1; x < limite; x++) {
            onView(withId(R.id.draw)).perform(singleTouchAt(x, x, MotionEvent.ACTION_MOVE));
            assertEquals("taille de la liste de point", x+1, myView.current[0].getNbPoints());
        }
        onView(withId(R.id.draw)).perform(singleTouchAt(x, x, MotionEvent.ACTION_UP));
        MyPolygon[] last = myView.last; // modif pour P
        assertEquals("taille de la liste de point", limite + 1, last[0].getNbPoints());



    }




    @Test
    public void DiagonaleDescendanteAvecVérificationDesPointsEnregistrés() {
        // on verifie en plus la valeur des points
        DemoMulti demo = mActivityRule.getActivity();
        MyView myView = (MyView) demo.findViewById(R.id.draw);
        int limite = 150;


        int x = 0;
        onView(withId(R.id.draw)).perform(singleTouchAt(x, x, MotionEvent.ACTION_DOWN));
        assertEquals("taille de la liste de point", x + 1, myView.current[0].getNbPoints());

        for(x = 1; x < limite; x++) {
            onView(withId(R.id.draw)).perform(singleTouchAt(x, x, MotionEvent.ACTION_MOVE));
            assertEquals("taille de la liste de point", x+1, myView.current[0].getNbPoints());
        }
        onView(withId(R.id.draw)).perform(singleTouchAt(x, x, MotionEvent.ACTION_UP));
        MyPolygon[] last = myView.last; // modif pour P
        assertEquals("taille de la liste de point", limite + 1, last[0].getNbPoints());


        float  [] recordedPoints = last[0].getPoints();
        assertEquals("taille des points enregistrés", (limite+1)*2, recordedPoints.length);
        for(int i = 0; i < recordedPoints.length; i = i+2) {
            assertEquals("coord x des points enregistrés",i/2,recordedPoints[i],0);
            assertEquals("coord y des points enregistrés",i/2,recordedPoints[i+1],0);
        }

    }
    /* */
}