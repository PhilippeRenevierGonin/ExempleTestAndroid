package fr.unice.reneviergonin.demomulti;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import org.hamcrest.Matcher;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;

/**
 * Created by Philippe on 09/03/2016.
 */
public class MyViewAction {


    public static ViewAction singleTouchAt(final int x, final int y, final int action) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                long date = SystemClock.uptimeMillis();
                MotionEvent me = MotionEvent.obtain(date, date, action, x, y, 0);
                view.onTouchEvent(me);
            }

            @Override
            public String getDescription() {
                return "touch ";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(View.class);
            }
        };
    }


    public static ViewAction pamatreziedSingleTouchAt( final float x, final float y, final int action, final long d1, final long d2, final int meta) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                MotionEvent me = MotionEvent.obtain(d1, d2, action, x, y, meta);
                view.onTouchEvent(me);
            }

            @Override
            public String getDescription() {
                return "touch ";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(View.class);
            }
        };
    }
}

