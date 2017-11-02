import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {

    private static final int X_SPEED = 5;

    private static final int Y_SPEED = 5;

    private static final int WIDTH = 30;

    private static final int HEIGHT = 30;

    private int x, y;

    private int oldX, oldY;

    private int life = 100;

    private boolean good;

    private boolean live = true;

    private TankClient tc;

    private boolean bL = false, bU = false, bR = false, bD = false;

    private static Random r = new Random();

    private int step = r.nextInt(12) + 3;

    private Direction dir;

    //Define barrel's direction
    private Direction barrelDir = Direction.D;

    private BloodBar bb = new BloodBar();

    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.good = good;
    }

    public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
        this(x, y, good);
        this.dir = dir;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (!this.isLive()) {
            if (!good) {
                tc.tanks.remove(this);
            }
            return;
        }
        Color c = g.getColor();
        if (good) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLUE);
        }
        g.fillOval(x, y,WIDTH,HEIGHT);
        g.setColor(c);

        if (this.isGood()) bb.draw(g);

        // Draw barrel for tank
        switch (barrelDir) {
            case L:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT / 2);
                break;
            case LU:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y);
                break;
            case U:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y);
                break;
            case UR:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y);
                break;
            case R:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT / 2);
                break;
            case RD:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT);
                break;
            case D:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y + Tank.HEIGHT);
                break;
            case DL:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT);
                break;
        }
        if (this.dir != Direction.STOP) {
            this.barrelDir = dir;
        }
        move();
    }

    void move() {
        this.oldX = x;
        this.oldY = y;
        switch (dir) {
            case L:
                x -= X_SPEED;
                break;
            case LU:
                x -= X_SPEED;
                y -= Y_SPEED;
                break;
            case U:
                y -= Y_SPEED;
                break;
            case UR:
                x += X_SPEED;
                y -= Y_SPEED;
                break;
            case R:
                x += X_SPEED;
                break;
            case RD:
                x += X_SPEED;
                y += Y_SPEED;
                break;
            case D:
                y += Y_SPEED;
                break;
            case DL:
                x -= X_SPEED;
                y += Y_SPEED;
                break;
            case STOP:
                break;
        }
        if (x < 0) x = 0;
        if (y < 30) y = 30;
        if (x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
        if (y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;

        if (!good) {
            Direction[] dirs = Direction.values();
            if (step == 0) {
                step = r.nextInt(12) + 3;
                int rn = r.nextInt(dirs.length);
                dir = dirs[rn];
            }
            step--;
            if (r.nextInt(40) > 38) {
                this.fire();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_R :
                if (!this.isLive()) {
                    this.setLive(true);
                    this.setLife(100);
                }
                break;
            case KeyEvent.VK_LEFT :
                bL = true;
                break;
            case KeyEvent.VK_UP :
                bU = true;
                break;
            case KeyEvent.VK_RIGHT :
                bR = true;
                break;
            case KeyEvent.VK_DOWN :
                bD = true;
                break;
        }
        locateDirection();
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_SPACE :
                fire();
                break;
            case KeyEvent.VK_LEFT :
                bL = false;
                break;
            case KeyEvent.VK_UP :
                bU = false;
                break;
            case KeyEvent.VK_RIGHT :
                bR = false;
                break;
            case KeyEvent.VK_DOWN :
                bD = false;
                break;
            case KeyEvent.VK_A:
                superFire();
                break;

        }
        locateDirection();
    }

    /**
     * Read key press from keyboard, set location.
     */
    void locateDirection() {
        if (bL && !bU && !bR && !bD) dir = Direction.L;
        else if (bL && bU && !bR && !bD) dir = Direction.LU;
        else if (!bL && bU && !bR && !bD) dir = Direction.U;
        else if (!bL && bU && bR && !bD) dir = Direction.UR;
        else if (!bL && !bU && bR && !bD) dir = Direction.R;
        else if (!bL && !bU && bR && bD) dir = Direction.RD;
        else if (!bL && !bU && !bR && bD) dir = Direction.D;
        else if (bL && !bU && !bR && bD) dir = Direction.DL;
        else if (!bL && !bU && !bR && !bD) dir = Direction.STOP;
    }

    public void fire() {
        if (!this.isLive()) return;
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile m;
        m = new Missile(x, y, good, barrelDir, tc);
        tc.missiles.add(m);
    }

    public void fire(Direction dir) {
        if (!this.isLive()) return;
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile m = new Missile(x, y, good, dir, tc);
        tc.missiles.add(m);
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLife() {
        return life;
    }

    public boolean isLive() {
        return live;
    }

    public boolean isGood() {
        return good;
    }

    public Rectangle getRec() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    private void stay() {
        x = oldX;
        y = oldY;
    }

    public boolean collidesWithWall(Wall w) {
        if (this.live && this.getRec().intersects(w.getRec())) {
            this.stay();
            return true;
        }
        return false;
    }

    public boolean collidesWithTank(Tank t) {
        if (this.isLive() && t.isLive() && this.getRec().intersects(t.getRec())) {
            this.stay();
            t.stay();
            return true;
        }
        return false;
    }

    public boolean collidesWithTanks(List<Tank> tanks) {
        for (int i = 0; i < tanks.size(); i++) {
            if (this != tanks.get(i) && collidesWithTank(tanks.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void superFire() {
        Direction[] dirs = Direction.values();
        //Use dir.length - 1 here to avoid STOP direction missile.
        for (int i = 0; i < dirs.length - 1; i++) {
            fire(dirs[i]);
        }
    }

    private class BloodBar {
        public void draw(Graphics g) {
            Color c = g.getColor();
            g.setColor(Color.RED);
            g.drawRect(x, y - 10, WIDTH, 9);
            int w = WIDTH * life / 100;
            g.fillRect(x, y - 10, w, 9);
            g.setColor(c);
        }
    }

    public boolean eat(Blood b) {
        if (this.isLive() && b.isLive() && this.getRec().intersects(b.getRec())) {
            this.life = 100;
            b.setLive(false);
            return true;
        }
        return false;
    }
}
