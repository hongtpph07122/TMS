package com.tms.api.utils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URL;

public class AudioUtils {
    private static final Logger logger = LoggerFactory.getLogger(AudioUtils.class);
    public static double getAudioDuration(String specificPath) {
        AudioInputStream audioInputStream;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new URL(specificPath));
            AudioFormat audioFormat = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            double result  = (frames) / audioFormat.getFrameRate();
            logger.info("Get Duration time: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Get Duration error: {} -> {}", e.getMessage(), e);
            return 0;
        }
    }
}
