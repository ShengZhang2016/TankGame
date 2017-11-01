import java.awt.*;

public class TankClient extends Frame {

    public void launchFrame() {
        this.setLocation(300, 400);
        this.setSize(800, 600);
        setVisible(true);
    }

    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.launchFrame();
    }
}
