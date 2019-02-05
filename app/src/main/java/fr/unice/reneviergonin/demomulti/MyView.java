package fr.unice.reneviergonin.demomulti;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import fr.unice.reneviergonin.demomulti.images.MyPolygon;

/**
 * Created by renevier-gonin on 10/10/2014.
 */
public class MyView extends View {

    MyPolygon []  current = new MyPolygon[10]; // jusqu'à 10 points
    // ArrayList [] last ; // change pour P
    MyPolygon[] last ;

    // ici dans la même application, mais de préférence aurait du être fait ailleurs...
    // pour la version 3
    ArrayList<MotionEvent> event = new ArrayList<MotionEvent> () ;
    public ArrayList<MotionEvent> getEvents() {
        ArrayList<MotionEvent> copy = event;
        event = new ArrayList<MotionEvent> () ;
        return copy;
    }


    Paint paint = new Paint();
    {
        paint.setStrokeWidth(3f);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }
    ArrayList<Integer> knownIds ;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyView(Context context) {
        super(context);
    }


    public void reset() {
        event = new ArrayList<MotionEvent> () ;
        current = new MyPolygon[10];
        last = null;
    }

    // VERSION 1
//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//
//        // * interpretation... * //
//        // ***** le polygone en cours de dessin ***** //
//        if (e.getActionMasked() == MotionEvent.ACTION_MOVE) {
//
//            for(int count = 0 ; count < e.getPointerCount(); count++) {
//                int id = e.getPointerId(count);
//
//                if (current[id] == null) {
//                    current[id] = new MyPolygon();
//                    knownIds = new ArrayList<Integer>();
//                }
//
//
//                MotionEvent.PointerCoords point = new MotionEvent.PointerCoords();
//                e.getPointerCoords(count, point);
//                current[id].addPoint(point.x, point.y);
//                knownIds.add(id);
//            }
//
//        }
//        else  if ((e.getActionMasked() == MotionEvent.ACTION_UP) && (e.getPointerCount() == 1) && (current != null)) { // le dernier doigt est levé
//
//                if (knownIds.size() > 1) {
//                    last = current;
//                    for(MyPolygon p : last) if (p!=null) p.color = Color.GRAY;
//                }
//                current = new MyPolygon[current.length];
//                knownIds = new ArrayList<Integer>();
//            }
//
//        invalidate();
//        return true;
//
//    }


/*
     // VERSION 2
     // il manquait le 1er et le dernier point
    @Override
    public boolean onTouchEvent(MotionEvent e) {

        // * interpretation... * //
        // ***** le polygone en cours de dessin ***** //
        if ((e.getActionMasked() == MotionEvent.ACTION_DOWN) || (e.getActionMasked() == MotionEvent.ACTION_MOVE) || (e.getActionMasked() == MotionEvent.ACTION_UP)) {

            for(int count = 0 ; count < e.getPointerCount(); count++) {
                int id = e.getPointerId(count);

                if (current[id] == null) {
                    current[id] = new MyPolygon();
                    knownIds = new ArrayList<Integer>();
                }


                MotionEvent.PointerCoords point = new MotionEvent.PointerCoords();
                e.getPointerCoords(count, point);
                current[id].addPoint(point.x, point.y);
                knownIds.add(id);
            }

        }

        if ((e.getActionMasked() == MotionEvent.ACTION_UP) && (e.getPointerCount() == 1) && (current != null)) { // le dernier doigt est levé

            if (knownIds.size() > 1) {
                last = current;
                for(MyPolygon p : last) if (p!=null) p.color = Color.GRAY;
            }
            current = new MyPolygon[current.length];
            knownIds = new ArrayList<Integer>();
        }

        invalidate();
        return true;

    }/**/






// VERSION 3
// on enregistre les MotionEvent (par duplication car sinon recyclé... et effet de bord
    @Override
    public boolean onTouchEvent(MotionEvent e) {

        event.add(MotionEvent.obtain(e));
        // * interpretation... * //
        // ***** le polygone en cours de dessin ***** //
        if ((e.getActionMasked() == MotionEvent.ACTION_DOWN) || (e.getActionMasked() == MotionEvent.ACTION_MOVE) || (e.getActionMasked() == MotionEvent.ACTION_UP)) {

            for(int count = 0 ; count < e.getPointerCount(); count++) {
                int id = e.getPointerId(count);

                if (current[id] == null) {
                    current[id] = new MyPolygon();
                    knownIds = new ArrayList<Integer>();
                }


                MotionEvent.PointerCoords point = new MotionEvent.PointerCoords();
                e.getPointerCoords(count, point);
                current[id].addPoint(point.x, point.y);
                knownIds.add(id);
            }

        }

        if ((e.getActionMasked() == MotionEvent.ACTION_UP) && (e.getPointerCount() == 1) && (current != null)) { // le dernier doigt est levé

            if (knownIds.size() > 1) {
                // last = current;  // change pour P
                if (last == null) {
                    last = new MyPolygon[10];
                }

                for(MyPolygon p : current) if (p!=null) p.color = Color.DKGRAY;

                last = current;

                // for(MyPolygon p : last) if (p!=null) p.color = Color.GRAY; // change pour P
            }
            current = new MyPolygon[current.length];
            knownIds = new ArrayList<Integer>();
        }

        invalidate();
        return true;

    }


/**/

    /**
     *
     * seuls deux polygones peuvent être dessinés : celui en cours de dessin (en noir) et
     * le précédent (en gris).
     * si on ne dessine plus, il n'y en a qu'un, en gris
     */
    public void onDraw(Canvas g) {
        super.onDraw(g);
        if (last != null) {

                for(MyPolygon p : last) if (p!=null) {
                    paint.setColor(p.color);
                    float [] pts = p.getPoints();
                    if (pts != null) draw(g, pts, false);
                }


        }
        if (current != null) {
            for(MyPolygon p : current) if (p!=null) {
                paint.setColor(p.color);
                float[] pts = p.getPoints();
                if (pts != null) draw(g, pts, false);
            }
        }
    }



    private void draw(Canvas g, float[] pts, boolean closing)
    {
        for(int i = 0; i<pts.length-3; i=i+2)
        {
            g.drawLine(pts[i], pts[i+1], pts[i+2], pts[i+3], paint);
        } // ligne entre le dernier sommet et le premier somment g.drawLine(pts[pts.length-2], pts[pts.length-1], pts[0], pts[1], paint);
        if (closing) g.drawLine(pts[pts.length-2], pts[pts.length-1], pts[0], pts[1], paint);
    }


    /**/

    // seuil à déterminer, notamment en fonction des tests unitaire
    final static float SEUIL =   12f; // 5f;  //  12f; //

    // en fait c'est est "droit"...
    public boolean estHorizontal(int indice) {
        boolean result = true;
        if ( (indice >= 0) && (indice < last.length) && (last[indice] != null)) {
            float[] points = last[indice].getPoints();
            if (points.length >= 4) {
                float x0 = points[0]; float y0 = points[1];
                float xf =  points[points.length-2]; float yf =  points[points.length-1];

                // ligne suivante à commenter pour avoir une erreur
                result = (Math.abs(yf-y0) < SEUIL); // test horizontal

                if (x0 != xf) {
                    float pente = (yf - y0)/(xf-x0);

                    float max_diff = 0;

                    for(int x = 0; x < last[indice].getNbPoints(); x++) {
                        float y = pente*(points[x*2]-x0)+y0;
                        result = result && (Math.abs(y-points[x*2+1]) < SEUIL) ;

                        if (Math.abs(y-points[x*2+1]) > max_diff) max_diff = Math.abs(y-points[x*2+1]);
                    }

                }
            }

        }
        else result = false;

        return result;
    }

}
