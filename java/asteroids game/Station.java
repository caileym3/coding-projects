import java.awt.*;
import java.util.ArrayList;

/*
 * move the cannon left or right
 * fire the cannon
 * draw station
 * check to see whether an asteroid hit the station
 * */

class Station {
    // width and height of frame
    private int FrameWidth = 500;
    private int FrameHeight = 400;

    // constructor
    public Station(double ix, double iy) {
        // defining variables as parameter values
        x = ix;
        y = iy;
    }

    private double angle = Math.PI / 2.0; // public static final double PI 3.141592653589793d
    private int hits = 0; // So, angle is initialized to 90 degrees
    private final double x; // x position of canon
    private final double y; // y position of canon

    // method to move cannon to the left
    public void moveLeft() {
        angle = angle + 0.1;
    }

    // method to move cannon to the right
    public void moveRight() {
        angle = angle - 0.1;
    }

    // method to fire cannon
    public void fire(ArrayList rockets) {
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);
        // rocket goes same direction as gun is pointing
        // length of Rocket Launcher is 20; size of rocket is 5; (20 – 5 = 15)
        Rocket r = new Rocket(x + 15 * cosAngle, y - 15 * sinAngle,
                5 * cosAngle, -5 * sinAngle);
        rockets.add(r);
    }

    // check if asteroid (rock) hit me (station) then hits is incremented by asteroid size
    public void checkHit(Asteroid rock) {
        if (rock.nearTo((double) x, (double) y))
            hits += rock.size;
    }

    public int getHits() {
        return hits;
    }

    // paint station and canon and score
    public void paint(Graphics g) {
        // paint rocket launcher (length 20 pixels)
        g.setColor(Color.red);
        double lv = 20 * Math.sin(angle); // launcher tip vertical coordinate
        double lh = 20 * Math.cos(angle); // launcher tip horizontal coordinate
        // (x, y) is launcher base, (x+lh, y-lv) is tip of launcher
        g.drawLine((int) x, (int) y, (int) (x + lh), (int) (y - lv)); // cannon
        g.fillOval((int) x - 10, (int) y, 20, 20); // station
        // display updated score
        g.drawString("hits: " + hits, 10, FrameHeight - 20);
    }
}
