import java.awt.*;
import java.util.List;

public class Missile {

    public static final int X_SPEED = 10;
    public static final int Y_SPEED = 10;

    public static final int WIDTH = 10;

    public static final int HEIGHT = 10;

    private boolean live = true;

    private boolean good;

    private TankClient tc;

    int x, y;

    Tank.Direction dir;

    public Missile(int x, int y, Tank.Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Missile(int x, int y, boolean good, Tank.Direction dir, TankClient tc) {
        this(x, y, dir);
        this.good = good;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (!live) {
            tc.missiles.remove(this);
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(c);

        move();
    }

    private void move() {
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
        }

        if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
            live = false;
        }
    }

//    public boolean isLive() {
//        return live;
//    }

    public Rectangle getRec() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean hitTank(Tank t) {
        if (this.live && this.getRec().intersects(t.getRec()) && t.isLive() && this.good != t.isGood()) {
            if (t.isGood()) {
                t.setLife(t.getLife() - 20);
                if (t.getLife() <= 0) {
                    t.setLive(false);
                }
            } else {
                t.setLive(false);
            }
            this.live = false;
            Explode e = new Explode(x, y, tc);
            tc.explodes.add(e);
            return true;
        }
        return false;
    }

    public boolean hitTanks(List<Tank> tanks) {
        for (int i = 0; i < tanks.size(); i++) {
            if (hitTank(tanks.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean hitWall(Wall w) {
        if (this.live && this.getRec().intersects(w.getRec())) {
            this.live = false;
            return true;
        }
        return false;
    }
}
