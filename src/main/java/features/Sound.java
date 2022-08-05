package features;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Sound {

    Clip clip;
    List<String> soundNames = new ArrayList<>();
    List<File> soundURL = new ArrayList<>();

    public Sound(String folderPath) {
        File directory = new File(folderPath);
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                String filePath = folderPath + "/" + files[i].getName();
                soundURL.add(new File(filePath));
                soundNames.add(files[i].getName());
                System.out.println("Nume sunet: " + soundNames.get(i));
            }
        }
    }

    public void setFile(String soundName) {
        try {
            for (int i = 0; i < soundNames.size(); i++) {
                if (soundNames.get(i).equals(soundName)) {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL.get(i));
                    clip = AudioSystem.getClip();
                    clip.open(ais);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}
