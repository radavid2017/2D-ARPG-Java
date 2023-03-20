package features;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Sound {

    Clip clip;
    List<String> soundNames = new ArrayList<>();
    List<File> soundURL = new ArrayList<>();
    FloatControl fc;
    int volumeScale = 0;
    float volume;

    public Sound(String folderPath) {
        File directory = new File(folderPath);
        File[] files = directory.listFiles();
        System.out.println(folderPath + " fisiere: " + files.length);
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
                    fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    checkVolume();
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

    public void checkVolume() {
        switch (volumeScale) {
            case 0 -> {
                volume = -80f;
            }
            case 1 -> {
                volume = -20f;
            }
            case 2 -> {
                volume = -12f;
            }
            case 3 -> {
                volume = -5f;
            }
            case 4 -> {
                volume = 1f;
            }
            case 5 -> {
                volume = 6f;
            }
        }
        fc.setValue(volume);
    }

    public int getVolumeScale() {
        return volumeScale;
    }

    public void setVolumeScale(int volumeScale) {
        this.volumeScale = volumeScale;
    }
}
