package features;

import game.GamePanel;

import java.io.*;

public class Config {
    GamePanel gp;

    public Config(GamePanel gp) {
        this.gp = gp;
    }

    public void saveConfig() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config"));

            // Full screen
            if (gp.fullScreenOn) {
                bw.write("On");
            }
            else {
                bw.write("Off");
            }
            bw.newLine();

            // Music volume
            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();

            // SE volume
            bw.write(String.valueOf(gp.soundEffect.volumeScale));
            bw.newLine();

            bw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadConfig() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("config"));

            String s = br.readLine();

            // Full screen
            gp.fullScreenOn = s.equals("On");

            // Music volume
            s = br.readLine();
            gp.music.volumeScale = Integer.parseInt(s);

            // SE volume
            s = br.readLine();
            gp.soundEffect.volumeScale = Integer.parseInt(s);

            br.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
