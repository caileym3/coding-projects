import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/*
 * creates the station
 * creates a container (an ArrayList) for the collection of asteroids
 * creates another ArrayList container for the rockets
 * creates the listener for system-generated key presses
 * */

public class AsteroidGame extends Frame {
    // frame width and heigth
    private int FrameWidth = 500;
    private int FrameHeight = 400;
    // timer of game
    private Timer timer;
    TimerTask task;
    int seconds;


    // constructor
    public AsteroidGame() {
        setTitle("Asteroid Game"); // title of frame
        setSize(FrameWidth, FrameHeight); // size of frame
        setBackground(Color.black); // background color of frame
        addKeyListener(new keyDown()); // key events
        addWindowListener(new CloseQuit()); // widow events

        // timer
        seconds = 0;
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                seconds++;
            }
        };
    }

    // run asteroid game
    public void run() {
        timer.scheduleAtFixedRate(task, 0, 1000); // starting timer (in seconds)
        while (true) {
            movePieces();
            repaint();
            // end game if hits is more than 500
            if (station.getHits() >= 500) {
                clearGame(); // removes all rockets and asteriods
                repaint(); // repaints
                break; // ends loop to end game
            }
            try {
                // pause 100 milliseconds in order
                // to create animation illusion
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
    }

    // ArrayLists for Asteriod and Rocket objects
    private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
    private ArrayList<Rocket> rockets = new ArrayList<Rocket>();
    // Station position middle of baseline
    private Station station = new Station(FrameWidth / 2, FrameHeight - 20);

    // moving asteriods, rockets, and station together
    private void movePieces() {
        // create a random new asteroid – 30% of the time
        if (Math.random() < 0.3) {
            Asteroid newRock = new Asteroid(
                    FrameWidth * Math.random(), 20,
                    10 * Math.random() - 5, 3 + 3 * Math.random());
            asteroids.add(newRock);
        }
        // then move everything
        int i = 0;
        while (asteroids.size() > i) {
            // remove Asteriod objects in ArrayList that are out of the frame (width or height)
            // else move Asteriod objects (in ArrayList, iterate through)
            Asteroid rock = (Asteroid) asteroids.get(i);
            if (rock.getX() > FrameWidth || rock.getY() > FrameHeight) {
                asteroids.remove(i); // remove Asteroid
            } else {
                rock.move(); // move Asteroid
                station.checkHit(rock); // check if the station hit each Asteriod
            }
            i++;
        }
        int j = 0;
        while (rockets.size() > j) {
            // remove Rocket objects in ArrayList that are out of the frame (width or height)
            // else move Rocket objects (in ArrayList, iterate through)
            Rocket rock = (Rocket) rockets.get(j);
            if (rock.getX() > FrameWidth || rock.getY() > FrameHeight) {
                rockets.remove(j); // remove Rocket
            } else {
                rock.move(asteroids); // move Rocket
            }
            j++;
        }
    }

    // displaying game on frame
    public void paint(Graphics g) {
        station.paint(g); // displaying station
        g.drawString("time: " + seconds + " seconds", 10, FrameHeight - 5); // displaying time
        // displaying all Asteriod objects in ArrayList
        int i = 0; // counter
        while (asteroids.size() > i) {
            Asteroid rock = (Asteroid) asteroids.get(i); // iterate through ArrayList
            rock.paint(g);
            i++; // counter + 1
        }
        // displaying all Rocket objects in ArrayList
        int j = 0; // counter
        while (rockets.size() > j) {
            Rocket rock = (Rocket) rockets.get(j); // iterate through ArrayList
            rock.paint(g);
            j++; // counter + 1
        }
        // if hits are >= 500, then display YOU LOSE in the middle of the frame
        if (station.getHits() >= 500) {
            g.drawString("YOU LOSE", FrameWidth / 2 - 20, FrameHeight / 2);
        }

    }

    // key events
    private class keyDown extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            char key = e.getKeyChar();
            switch (key) {
                // j moves cannon to the left
                case 'j':
                    station.moveLeft();
                    break; // turn left
                // k moves cannon to the right
                case 'k':
                    station.moveRight();
                    break; // turn right
                // space bar fires rockets
                case ' ':
                    station.fire(rockets);
                    break; // space: fire
                // q quits the game by exiting frame
                case 'q':
                    System.exit(0); // q: quit
            }
        }
    }

    // quitting game by closing window
    private class CloseQuit extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

    // method to clear the game of asteriods and rockets
    private void clearGame() {
        asteroids.clear();
        rockets.clear();
    }

    private class gameMover extends Thread {
        // override the run() method of Thread
        public void run() {
            while (true) {
                movePieces();
                repaint();
                try {
                    sleep(100);
                } catch (Exception e) {
                }
            }
        }
    }

    // main
    static public void main(String[] args) {
        AsteroidGame world = new AsteroidGame(); // object to call AsteroidGame class
        world.setVisible(true); // make frame visible
        world.run(); // run game
    }
}
