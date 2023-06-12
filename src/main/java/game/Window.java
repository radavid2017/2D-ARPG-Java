package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class Window {

    public void run() {
//        GraphicsEnvironment graphics =
//                GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice device = graphics.getDefaultScreenDevice();

        // crearea ferestrei
        JFrame window = new JFrame();
        // inchide fereastra la butonul de iesire 'x'
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // adaptarea rezolutiei in functie de ecran
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int width = (int) (screenSize.width / 1.25f);
//        int height = (int) (screenSize.height / 1.25f);
//        window.setBounds(0,0, width, height);
        // titlul ferestrei
        window.setTitle("Huntily Poke");
        setIcon(window);

        // panou de joc - instantiere
        GamePanel gamePanel = new GamePanel();

        // adaugarea panoului de joc in fereastra
        window.add(gamePanel);

        gamePanel.config.loadConfig();

        window.pack();


        // Setari fullscreen
//        window.setUndecorated(true);
//        window.setResizable(false);
//        device.setFullScreenWindow(window);

        // afisarea ferestrei in centrul ecranului
        window.setLocationRelativeTo(null);

        // fereastra devine vizibila
        window.setVisible(true);

//        gamePanel.setupGame();
        gamePanel.startGameThread();


    }

    public void setIcon(JFrame window) {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(ImageIO.read(new FileInputStream("res/item/equipable/weapon/sword/sword_normal.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        window.setIconImage(icon.getImage());
    }
}
