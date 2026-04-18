package audio;

import javax.sound.sampled.*;
import java.io.File;

public class MusicPlayer {

    private static Clip clip;
    private static boolean isPlaying = false;

    public static void playLoop(String path) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }

            AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(new File(path));

            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

            isPlaying = true;

        } catch (Exception e) {
            System.out.println("Music error: " + e.getMessage());
        }
    }

    public static void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
        isPlaying = false;
    }

    public static void toggle(String path) {
        if (isPlaying) {
            stop();
        } else {
            playLoop(path);
        }
    }

    public static boolean isPlaying() {
        return isPlaying;
    }
}