import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame {

    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;


    Tank myTank = new Tank(50, 50, true, Tank.Direction.STOP, this);

    List<Missile> missiles = new ArrayList<>();
    List<Explode> explodes = new ArrayList<>();
    List<Tank> tanks = new ArrayList<>();

    Wall w1 = new Wall(100, 200, 20, 150, this);
    Wall w2 = new Wall(300, 100, 300, 20, this);

    Image offScreenImage = null;


    @Override
    public void paint(Graphics g) {

        g.drawString("Missiles' count " + missiles.size(),60,60);
        g.drawString("Explode count " + explodes.size(), 60, 80);
        g.drawString("Tanks count " + tanks.size(), 60, 100);
        g.drawString("Tank Life " + myTank.getLife(), 60, 120);

        for (int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);
            t.collidesWithWall(w1);
            t.collidesWithWall(w2);
            t.collidesWithTanks(tanks);
            t.draw(g);
        }
        for (int i = 0; i < missiles.size(); i++) {
            Missile m = missiles.get(i);
            m.hitTanks(tanks);
            m.hitTank(myTank);
            m.hitWall(w1);
            m.hitWall(w2);
            m.draw(g);
        }
        for (int i = 0; i < explodes.size(); i++) {
            Explode e = explodes.get(i);
            e.draw(g);
        }

        myTank.draw(g);
        w1.draw(g);
        w2.draw(g);
    }

    // Double buffer, in case of flash.
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = g.getColor();
        gOffScreen.setColor(Color.GRAY);
        gOffScreen.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage,0,0,null);
    }

    public void launchFrame() {
        for (int i = 0; i < 10; i++) {
            tanks.add(new Tank(50 + 40 *(i + 1), 50, false, Tank.Direction.D, this));
        }
        this.setLocation(300, 400);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setTitle("Tank Game Version 0.1");
        this.setBackground(Color.GRAY);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setResizable(false);

        this.addKeyListener(new keyBoardMonitor());

        setVisible(true);

        new Thread(new PaintThread()).start();
    }

    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.launchFrame();
    }

    private class PaintThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class keyBoardMonitor extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            myTank.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }
    }
}
