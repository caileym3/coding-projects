import java.awt.*;
import java.util.ArrayList;

/*
 * draw and move rocket
 * check if the rocket hit an asteroid
 * move rocket
 * */

class Rocket {
    public double x, y; // current position coordinates
    private double dx, dy; // idx & idy computed from canon direction

    // constructor
    public Rocket(double ix, double iy, double idx, double idy) {
        // defining variables as parameter values
        x = ix;
        y = iy;
        dx = idx;
        dy = idy;
    }

    // method to return x position of rocket
    public double getX() {
        return x;
    }

    // method to return y position of rocket
    public double getY() {
        return y;
    }

    // method to move rocket by changing x and y position by dx and dy
    public void move(ArrayList asteroids) {
        x += dx;
        y += dy; // move ‘this’ rocket
        int i = 0; // counter to iterate through asteroid ArrayList
        // check if new position of rocket is near each asteroid in asteroid Arraylist
        // if so, run hit() in Asteroid class which will shrink asteroid
        while (asteroids.size() > i) {
            Asteroid rock = (Asteroid) asteroids.get(i); // must (cast)
            if (rock.nearTo(x, y)) {
                rock.hit(); // hit if too close
            }
            i++; // counter + 1
        }
    }

    // method to paint rocket
    public void paint(Graphics g) {
        g.setColor(Color.red); // draw self: red circle inside
        g.fillOval((int) x, (int) y, 5, 5); // a 5 by 5 bounding rectangle
    } // (x, y) is the upper left corner
}
