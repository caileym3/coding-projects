import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Queue;
import java.util.*;

/*
 * Caterpillar Game...
 * creates universe of objects
 * repaints window
 * moves caterpillars
 * */

public class CaterpillarGame extends Frame {
    // board dimensions
    final static int BoardWidth = 60;
    final static int BoardHeight = 40;
    // size of segment
    final static int SegmentSize = 10;

    // two Caterpillars in their starting positions
    private Caterpillar playerOne = new Caterpillar(Color.blue, new Point(20, 10));
    private Caterpillar playerTwo = new Caterpillar(Color.red, new Point(20, 30));

    // array to hold all possible number square positions on board
    private Point[] numberPos = new Point[BoardWidth * BoardHeight];
    private Point currSquare; // current number square position
    private int sqCount; // number of number squares

    private int playerOneScore; // score of player one
    private int playerTwoScore; // score of player two

    // countdown timers for player 1 and player 2
    private Timer timer1;
    TimerTask task1;
    int counter1;

    private Timer timer2;
    TimerTask task2;
    int counter2;

    // constructor
    public CaterpillarGame() {
        setSize((BoardWidth + 1) * SegmentSize, (BoardHeight * SegmentSize) + 30); // size of frame
        setTitle("Caterpillar Game"); // title of frame
        addKeyListener(new KeyReader()); // key events
        addWindowListener(new CloseQuit()); // window events

        // numberPos holds the position of the number square and its value
        int counter = 0; // array index starts at 0
        for (int i = 1; i < BoardWidth; i++) { // 0 < x < BoardWidth and 0 < y < BoardHeight
            for (int j = 1; j < BoardHeight; j++) { // are reachable values for caterpillar
                numberPos[counter] = new Point(i, j);
                counter++;
            }
        }

        currSquare = newNumberSquare(); // holds Point returned by newNumberSquare()
        sqCount = 1; // number of number squares caterpillars have caught

        // initialize player scores to 0
        playerOneScore = 0;
        playerTwoScore = 0;

        // setting up countdown timers
        counter1 = 20;
        timer1 = new Timer();
        task1 = new TimerTask() {
            public void run() {
                counter1--;
            }
        };

        counter2 = 20;
        timer2 = new Timer();
        task2 = new TimerTask() {
            public void run() {
                counter2--;
            }
        };
    }

    public void movePieces() {
        // move player one and player two caterpillars
        playerOne.move(this);
        playerTwo.move(this);

        // check if caterpillar is in number square
        if (playerOne.inPosition(currSquare)) { // if player one is in number square position, then...
            currSquare = newNumberSquare(); // change number square position
            sqCount++; // add one to number square count
            playerOneScore++; // add one to player one score
            counter1 = 20; // reset countdown timer for p1
            playerOne.addSegment(); // add segment to body of p1 caterpillar
        }
        if (playerTwo.inPosition(currSquare)) { // if player two is in number square position, then...
            currSquare = newNumberSquare(); // change number square position
            sqCount++; // add one to number square count
            playerTwoScore++; // add one to player two score
            counter2 = 20; // reset countdown timer for p1
            playerTwo.addSegment(); // add segment to body of p1 caterpillar
        }

        // check if counters are 0
        if (counter1 <= 0) { // if counter for p1 is <= 0, then...
            counter1 = 20; // resent countdown
            playerOne.removeSegment(); // remove segment from body of p1 caterpillar
        }
        if (counter2 <= 0) { // if counter for p2 is <= 0, then...
            counter2 = 20; // resent countdown
            playerTwo.removeSegment(); // remove segment from body of p1 caterpillar
        }
    }

    // apply boundaries to where caterpillars can move
    // hits edge of board and other caterpillar
    public boolean canMove(Point np) {
        // get x, y coordinate
        int x = np.x;
        int y = np.y;
        // test playing board boundaries
        if ((x <= 0) || (y <= 0)) return false;
        if ((x >= BoardWidth) || (y >= BoardHeight)) return false;
        // test caterpillars: can’t move through self or other Caterpillar
        if (playerOne.inPosition(np)) return false;
        if (playerTwo.inPosition(np)) return false;
        // ok, safe square
        return true;
    }

    // creates a new, randomly distributed number value
    // ensures number is not placed in a square already occupied by a caterpillar
    public Point newNumberSquare() {
        int max = (BoardWidth - 1) * (BoardHeight - 1); // max index in numberPos array
        int min = 1; // min index in numberPos array
        // generates random int value between max and min
        int n = (int) Math.floor(Math.random() * (max - min + 1)) + min;
        // check if caterpillar already in randomly chosen point numberPos[n]
        // if so, retry newNumberSquare()
        if (!playerOne.inPosition(numberPos[n]) && !playerTwo.inPosition(numberPos[n])) {
            return numberPos[n];
        } else {
            return newNumberSquare();
        }
    }

    // draw game
    public void paint(Graphics g) {
        // drawing 60 x 40 graph of board with segments of 10 pixels
        g.setColor(Color.GREEN); // set color of lines to green
        // 59 horizontal lines
        for (int i = 0; i < 59; i++) {
            g.drawLine((i * SegmentSize) + (2 * SegmentSize), // shift graph to the right by 2 segments
                    3 * SegmentSize,
                    (i * SegmentSize) + (2 * SegmentSize), // shift graph to the right by 2 segments
                    (BoardHeight * SegmentSize) + SegmentSize); // 59 pts
        }

        // 40 horizontal lines
        for (int i = 0; i < 41; i++) {
            g.drawLine(2 * SegmentSize,
                    (i * SegmentSize) + SegmentSize, // shift graph down by 1 segment
                    (BoardWidth * SegmentSize), // 39 pts
                    (i * SegmentSize) + SegmentSize); // shift graph down by 1 segment
        }

        // draw both caterpillars
        playerOne.paint(g);
        playerTwo.paint(g);

        // convert x and y point coordinate to coordinates on frame
        int sqX = (currSquare.x * SegmentSize) + SegmentSize; // x coordinate is 1 segment to the right
        int sqY = (currSquare.y * SegmentSize) + (3 * SegmentSize); // y coordinate is 3 segments down

        // draw number at random point
        g.setColor(Color.BLACK); // set color of number to black
        g.drawString(Integer.toString(sqCount), sqX - 5, sqY - 5); // draw number and position AT point

        // draw scores
        // player one score is blue and at bottom left
        g.setColor(Color.BLUE);
        g.drawString(("Player One Score: " + playerOneScore),
                20,
                (BoardHeight * SegmentSize) + 30 - 5);
        // player one score is red and at bottom right
        g.setColor(Color.RED);
        g.drawString(("Player Two Score: " + playerTwoScore),
                (BoardWidth + 1) * SegmentSize - 135,
                (BoardHeight * SegmentSize) + 30 - 5);

        // draw countdowns
        g.setColor(Color.BLUE);
        g.drawString(("Step Countdown: " + counter1),
                175,
                (BoardHeight * SegmentSize) + 30 - 5);
        g.setColor(Color.RED);
        g.drawString(("Step Countdown: " + counter2),
                315,
                (BoardHeight * SegmentSize) + 30 - 5);
    }

    // assigning keys to players and direction of caterpillars
    private class KeyReader extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            char c = e.getKeyChar();
            switch (c) {
                // player One keys
                case 'q':
                    playerOne.setDirection('Z');
                    break;
                case 'a':
                    playerOne.setDirection('W');
                    break;
                case 'd':
                    playerOne.setDirection('E');
                    break;
                case 'w':
                    playerOne.setDirection('N');
                    break;
                case 's':
                    playerOne.setDirection('S');
                    break;
                // player Two keys
                case 'p':
                    playerTwo.setDirection('Z');
                    break;
                case 'j':
                    playerTwo.setDirection('W');
                    break;
                case 'l':
                    playerTwo.setDirection('E');
                    break;
                case 'i':
                    playerTwo.setDirection('N');
                    break;
                case 'k':
                    playerTwo.setDirection('S');
                    break;
                // ignore all other keys
            }  // end switch
        }  // end keyPressed
    }  // end KeyReader private inner (nested) class

    // quitting game by closing window
    private class CloseQuit extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

    public void run() {
        // now start the game
        timer1.scheduleAtFixedRate(task1, 0, 1000);
        timer2.scheduleAtFixedRate(task2, 0, 1000);
        while (true) {
            movePieces();
            repaint();
            // check if either caterpillar has no body
            if (playerOne.getSize() == 0 || playerTwo.getSize() == 0) {
                // print game over and which player won depending on score
                if (playerOneScore > playerTwoScore)
                    System.out.println("THE GAME IS OVER. PLAYER 1 WON.");
                else if (playerOneScore < playerTwoScore)
                    System.out.println("THE GAME IS OVER. PLAYER 2 WON.");
                else if (playerOneScore == playerTwoScore)
                    System.out.println("THE GAME IS OVER. TIE.");
                break; // break while loop to stop game
            }
            try {
                Thread.sleep(100); // create  animation illusion
            } catch (Exception e) {
            } // must be in try-catch
        }
    }

    // main
    public static void main(String[] args) {
        CaterpillarGame world = new CaterpillarGame(); // object to call CaterpillarGame() class
        world.setVisible(true); // make frame visible
        world.run(); // run game
    }
}  // public class CaterpillarGame

// --------------------------------------------------------------------------------------------------------------------

/*
 * Caterpillar...
 * draws self
 * moves
 * */

class Caterpillar {
    private Color color;
    private Point position;
    private char direction = 'E';
    private Vector<Point> body = new Vector<>();
    private Queue<Character> commands = new LinkedList<>();

    // constructor
    public Caterpillar(Color c, Point sp) {
        color = c; // color of caterpillar
        for (int i = 0; i < 10; i++) { // each caterpillar is 10 pieces (circles)
            position = new Point(sp.x + i, sp.y); // nth piece (circle) is shifted n from the starting point sp
            body.add(position); // added to body
        }
    }

    // set direction of caterpillar
    public void setDirection(char d) {
        commands.add(d); // add to queue
    }

    // move caterpillar
    public void move(CaterpillarGame game) {
        // first see if we should change direction
        if (commands.size() > 0) {
            Character c = commands.peek(); // just peek
            commands.remove();
            direction = c.charValue();    // Character wrapper to char
            if (direction == 'Z') return;
        }
        // then find new position
        Point np = newPosition();
        if (game.canMove(np)) {
            // erase one segment, add another
            body.remove(0);
            body.add(np);
            position = np;
        }
    }

    // determine new position
    private Point newPosition() {
        int x = position.x;
        int y = position.y;
        if (direction == 'E') x++; // left
        else if (direction == 'W') x--; // right
        else if (direction == 'N') y--; // up
        else if (direction == 'S') y++; // down
        return new Point(x, y);
    }

    // determine if caterpillar's position is a point
    public boolean inPosition(Point np) {
        Enumeration e = body.elements();
        while (e.hasMoreElements()) {
            Point location = (Point) e.nextElement();
            //System.out.println(location);
            if (np.equals(location)) return true;
        }
        return false;
    }

    // add a segment to caterpillar
    public void addSegment() {
        body.add(position);
    }

    // remove segment from caterpillar
    public void removeSegment() {
        body.remove(0);
    }

    // return size of caterpillar
    public int getSize() {
        return body.size();
    }

    // draw caterpillar
    public void paint(Graphics g) {
        g.setColor(color);
        Enumeration e = body.elements();
        // iterator stuff
        while (e.hasMoreElements()) {
            Point p = (Point) e.nextElement();
            g.fillOval(5 + (CaterpillarGame.SegmentSize * p.x),
                    15 + (CaterpillarGame.SegmentSize * p.y),
                    CaterpillarGame.SegmentSize,
                    CaterpillarGame.SegmentSize);
        }
    }
}
