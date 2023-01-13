import java.awt.*;

/*
 * move and draw asteroids
 * shrink when hit
 * */

class Asteroid {
    public double x, y; // current position of ‘this’ asteroid
    private double dx, dy; // displacement (delta) dx & dy for next position
    public int size = 20; // initial size 20 pixels

    // constructor
    public Asteroid(double ix, double iy, double idx, double idy) {
        // defining variables as parameter values
        x = ix;
        y = iy;
        dx = idx;
        dy = idy;
    }

    // method to return x position of asteroid
    public double getX() {
        return x;
    }

    // method to return y position of asteroid
    public double getY() {
        return y;
    }

    // method to move asteroid by changing x and y positions by dx and dy
    public void move() {
        x += dx;
        y += dy;
    } // move to next position

    // method to paint asteroid
    public void paint(Graphics g) { // Hey asteroid, paint yourself
        g.setColor(Color.blue); // as a blue circle
        g.fillOval((int) x, (int) y, size, size); // must be int coordinates
    }

    // method to check if the asteroid has been hit
    public void hit() {
        size = size - 4;
    } // been hit, shrink size

    // method to check if the asteroid is near x y coordinate
    public boolean nearTo(double tx, double ty) {
        // use Pythagorean theorem to determine distance between points
        double distance = Math.sqrt((x - tx) * (x - tx) + (y - ty) * (y - ty));
        return distance < 20;
    } // x, y are coordinates of ‘this’
} // tx, ty are ‘other sender’ coords
