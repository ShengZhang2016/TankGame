import java.awt.*;

public class Blood {
    private int x, y, w, h;
    private TankClient tc;

    private int step = 0;

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    private boolean live = true;

    private int[][] pos = {{300,400},{300,420},{300,440},{300,460},{300,480},{300,500},{300,480},
            {300,460},{300,440},{300,420},{300,400}};

    public Blood() {
        x = pos[0][0];
        y = pos[0][1];
        w = h = 15;
    }

    public void draw(Graphics g) {
        if (!this.isLive()) return;

        Color c = g.getColor();
        g.setColor(Color.MAGENTA);
        g.fillRect(x, y, w, h);
        g.setColor(c);

        move();
    }

    private void move() {
        step++;
        if (step == pos.length) {
            step = 0;
        }
        x = pos[step][0];
        y = pos[step][1];
    }

    public Rectangle getRec() {
        return new Rectangle(x, y, w, h);
    }
}
