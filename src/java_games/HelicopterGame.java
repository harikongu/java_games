/**
 * Created by Hariprasad in 2008, while pursuing second year engineering studies.
 * This game was developed after inspired by the popular Helicopter flash game.
 * 
 * Watch how the game look like in youtube:
 * https://www.youtube.com/watch?v=izxtjY1hAE8
 * 
 * Warning:
 * This is not the updated code and this may contain bugs.
 */

package java_games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class HelicopterGame extends Frame implements MouseListener, Runnable,
        WindowListener {

    static int Piller1X = 1024, Piller2X = 1536;
    static int pillerPosition[] = {150, 200, 300, 350, 400, 450, 500};
    static int piller1Location, piller2Location;
    static boolean flag1, flag2, flag3, flag4;
    static int score = 0;

    int heliHight = 150;

    ImageIcon helicopterImg;
    Rectangle heli, piller1, piller2, topLayer, bottomLayer;
    boolean mousePress = true;

    HelicopterGame() {

        helicopterImg = new ImageIcon("images/helicopter.gif");

        addMouseListener(this);
        addWindowListener(this);
        Thread mainThread = new Thread(this);
        mainThread.start();

        Piller pillerThread = new Piller();
        pillerThread.start();
        piller1Location = getPillerPosition();
        piller2Location = getPillerPosition();

        heli = new Rectangle(180, 60);
        piller1 = new Rectangle(40, 270);
        piller2 = new Rectangle(40, 270);
        topLayer = new Rectangle(60, 150);
        bottomLayer = new Rectangle(60, 150);

        topLayer.x = 100;
        bottomLayer.x = 100;
        topLayer.y = 21;
        bottomLayer.y = 618;

    }

    static int getPillerPosition() {
        // To get random piller position
        int randVal = (int) (6 * java.lang.Math.random());
        int val = pillerPosition[randVal];
        return val;
    }

    public static void main(String arg[]) {
        HelicopterGame f = new HelicopterGame();
        f.setSize(1024, 768);
        f.setVisible(true);
    }

    public void run() {
        // NOTE: We Can also change the speed of the box
        // up one speed & down one speed
        try {
            while (flag1 == false && flag2 == false && flag3 == false
                    && flag4 == false) {

                // speed of helicopter
                Thread.sleep(15);

                // lift helicopter
                if (mousePress == true) {

                    if (heliHight < 538)
                        heliHight += 5;
                }

                // down helicopter
                if (mousePress == false) {

                    if (heliHight > 150)
                        heliHight -= 5;
                }

                repaint();

            }
            helicopterImg = new ImageIcon("images/heli_crashed1.gif");
            repaint();
            Thread.sleep(1500);
            helicopterImg = new ImageIcon("images/heli_crashed2.gif");

        } catch (Exception e) {
        }
    }

    void updateFlags() {

        flag1 = heli.intersects(piller1);
        flag2 = heli.intersects(piller2);
        flag3 = heli.intersects(topLayer);
        flag4 = heli.intersects(bottomLayer);

    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {

        Image test = createImage(1024, 768);
        Graphics gc = test.getGraphics();

        helicopterImg.paintIcon(this, gc, 100, heliHight);
        heli.x = 100;
        heli.y = heliHight + 20;

        // 1st piller
        gc.fillRect(Piller1X, piller1Location, 40, 270);
        piller1.x = Piller1X;
        piller1.y = piller1Location;

        // 2nd piller
        gc.fillRect(Piller2X, piller2Location, 40, 270);
        piller2.x = Piller2X;
        piller2.y = piller2Location;

        gc.setColor(new Color(0, 255, 51));

        // top layer
        gc.fillRect(0, 0, 1024, 150);

        // bottom layer
        gc.fillRect(0, 618, 1024, 150);

        gc.setColor(Color.blue);
        gc.drawString("SCORE:" + score, 900, 650);

        g.drawImage(test, 0, 0, this);

        updateFlags();
    }

    public void mousePressed(MouseEvent ev) {
        mousePress = false;
    }

    public void mouseClicked(MouseEvent ev) {
    }

    public void mouseReleased(MouseEvent ev) {
        mousePress = true;
    }

    public void mouseExited(MouseEvent ev) {
    }

    public void mouseEntered(MouseEvent ev) {
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

class Piller extends Thread {

    public void run() {
        try {
            while (HelicopterGame.flag1 == false
                    && HelicopterGame.flag2 == false
                    && HelicopterGame.flag3 == false
                    && HelicopterGame.flag4 == false) {

                Thread.sleep(20);

                HelicopterGame.Piller1X = HelicopterGame.Piller1X - 5;
                HelicopterGame.Piller2X = HelicopterGame.Piller2X - 5;

                if (HelicopterGame.Piller1X < 0) {
                    HelicopterGame.score += 100;
                    HelicopterGame.piller1Location = HelicopterGame
                            .getPillerPosition();
                    HelicopterGame.Piller1X = 1024;
                }

                if (HelicopterGame.Piller2X < 0) {
                    HelicopterGame.score += 100;
                    HelicopterGame.piller2Location = HelicopterGame
                            .getPillerPosition();
                    HelicopterGame.Piller2X = 1024;
                }

            }
        } catch (Exception e) {
        }
    }
}
