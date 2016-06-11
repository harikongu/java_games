/**
 * Created by Hariprasad in 2008, while pursuing second year engineering studies.
 * This game was developed after inspiring from Pumbaa's bugs collection in Lion King game.
 * Bugs collection video:
 * https://www.youtube.com/watch?v=B4mBnfc27Dk
 * Lion king was one of the games I played in my school days.
 * 
 * Watch the game video in youtube:
 * https://www.youtube.com/watch?v=3wU8HJF6-uA
 * 
 * Warning:
 * This is not the updated code and this may contain bugs.
 * 
 */

package java_games;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EggCollectionGame extends Frame implements Runnable, KeyListener,
        WindowListener {

    static int henX = 0;
    static byte[] keyState = new byte[256];
    static int manX = 100;
    static int manY = 500;
    static int score = 0;
    static int chance = 4;
    static boolean flag1, flag2;

    BufferedImage egg = null;
    EggThread eggthread1, eggthread2;
    HenThread henThread;
    ManThread manThread;
    Thread threadObj;

    int score1, score2;
    Rectangle manRect, eggRect1, eggRect2;
    ImageIcon manLeftImg, manRightImg, manImg, henLeftImg, henRightImg;

    EggCollectionGame() {
        addWindowListener(this);

        manRect = new Rectangle(30, 100);
        eggRect1 = new Rectangle(20, 30);
        eggRect2 = new Rectangle(20, 30);

        manRect.x = 125;
        manRect.y = 525;

        henLeftImg = new ImageIcon("images/hen_left.gif");
        henRightImg = new ImageIcon("images/hen_right.gif");

        manLeftImg = new ImageIcon("images/man_left.gif");
        manRightImg = new ImageIcon("images/man_right.gif");

        manImg = new ImageIcon("images/man_straight.gif");
        henThread = new HenThread();
        henThread.start();

        threadObj = new Thread(this);
        threadObj.start();

        eggthread1 = new EggThread();
        eggthread1.setPriority(Thread.MIN_PRIORITY);
        eggthread1.start();

        eggthread2 = new EggThread();
        eggthread2.setPriority(Thread.MIN_PRIORITY);
        eggthread2.start();

        manThread = new ManThread();
        manThread.start();

        try {
            egg = ImageIO.read(new File("images/egg.gif"));

        } catch (IOException e) {
        }

        addKeyListener(this);

    }

    public static void main(String arg[]) {
        EggCollectionGame f = new EggCollectionGame();
        f.setSize(600, 768);
        f.setVisible(true);

    }

    public void run() {

        try {
            while (true) {
                Thread.sleep(10);

                repaint();

                if (EggCollectionGame.chance == 0) {
                    henThread.stop();
                    eggthread1.stop();
                    eggthread2.stop();
                    manThread.stop();
                }
            }
        } catch (Exception e) {
        }
    }

    void updateScore() {

        eggthread1.bool = manRect.intersects(eggRect1);
        eggthread2.bool = manRect.intersects(eggRect2);

        score1 = (eggthread1.bool == true) ? 1 : 0;
        score2 = (eggthread2.bool == true) ? 1 : 0;
        score = score + score1 + score2;

    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        Image imgObj = createImage(600, 768);
        Graphics gc = imgObj.getGraphics();

        if (keyState[37] == 1 && chance != 0)
            manLeftImg.paintIcon(this, gc, manX, manY);
        if (keyState[39] == 1 && chance != 0)
            manRightImg.paintIcon(this, gc, manX, manY);
        if (keyState[37] == 0 && keyState[39] == 0)
            manImg.paintIcon(this, gc, manX, manY);

        manRect.x = manX + 25;
        manRect.y = manY + 25;

        if (eggthread1.eggY != 150) {

            gc.setColor(new Color(eggthread1.rVal, eggthread1.gVal,
                    eggthread1.bVal));
            gc.fillOval(eggthread1.eggX, eggthread1.eggY, 20, 30);
            eggRect1.x = eggthread1.eggX;
            eggRect1.y = eggthread1.eggY;

        }

        if (eggthread2.eggY != 150) {

            gc.setColor(new Color(eggthread2.rVal, eggthread2.gVal,
                    eggthread2.bVal));
            gc.fillOval(eggthread2.eggX, eggthread2.eggY, 20, 30);
            eggRect2.x = eggthread2.eggX;
            eggRect2.y = eggthread2.eggY;
        }

        if (henThread.moveLeft == true)
            henLeftImg.paintIcon(this, gc, henX, 100);
        else if (henThread.moveLeft == false)
            henRightImg.paintIcon(this, gc, henX, 100);

        gc.drawRect(0, 0, 600, 768);

        gc.drawString("SCORE:" + score, 100, 100);
        chance = 4 + eggthread1.chance + eggthread2.chance;
        gc.drawString("CHANCE:" + chance, 300, 100);
        if (chance == 0)
            gc.drawString("PLEASE TRY AGAIN", 300, 300);

        g.drawImage(imgObj, 0, 0, this);
        updateScore();

    }

    public void keyPressed(KeyEvent ke) {
        keyState[ke.getKeyCode() & 0xFF] = 1;
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void keyReleased(KeyEvent ke) {
        keyState[ke.getKeyCode() & 0xFF] = 0;
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }
}

class HenThread extends Thread {
    boolean moveLeft;
    int sleepTime = 20;

    public void run() {
        try {
            while (true) {
                // for increasing x value
                while (EggCollectionGame.henX < 500) {
                    moveLeft = false;
                    EggCollectionGame.henX += 10;
                    Thread.sleep(sleepTime);
                }

                // for decrease x value
                while (EggCollectionGame.henX > 0) {
                    moveLeft = true;
                    EggCollectionGame.henX -= 10;
                    Thread.sleep(sleepTime);
                }
            }

        } catch (Exception e) {
        }

    }
}

class EggThread extends Thread {

    public int eggX = 0, eggX1, eggY = 150;
    public int score = 0;
    public int chance = 0;
    public int rVal, gVal, bVal;
    boolean bool = false;
    int sleepTime = 10;

    public void run() {

        try {
            while (true) {
                eggX = (int) (50 * java.lang.Math.random());
                eggX = eggX * 10;

                while (true) {
                    if (EggCollectionGame.henX >= eggX - 15
                            && EggCollectionGame.henX <= eggX + 15) {
                        System.out.println("egg entered");
                        break;
                    }
                }

                // RGB values for egg colour
                rVal = (int) (255 * java.lang.Math.random());
                gVal = (int) (255 * java.lang.Math.random());
                bVal = (int) (255 * java.lang.Math.random());

                while (eggY <= 760 && bool == false) {
                    Thread.sleep(sleepTime);

                    eggY = eggY + 10;
                }

                // re-initialize eggY value to make hen ready for next egg
                // laying
                eggY = 150;

            }

        } catch (Exception e) {
        }
    }

}

class ManThread extends Thread {
    int sleepTime = 5;

    public void run() {
        try {
            while (true) {
                // control key speed
                // human running on the floor
                Thread.sleep(sleepTime);

                // Move left
                if (EggCollectionGame.keyState[39] == 1) {
                    if (EggCollectionGame.manX < 500)
                        EggCollectionGame.manX += 10;
                }

                // Move right
                if (EggCollectionGame.keyState[37] == 1) {
                    if (EggCollectionGame.manX > -20)
                        EggCollectionGame.manX -= 10;
                }

            }
        } catch (Exception e) {
        }
    }

}