package com.example.kafka;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestImage {

    public static void main(String[] args) {
        int width = 300;
        int height = 300;

        // Create a BufferedImage with a white background
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // Fill the image with white color
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Draw a red rectangle
        g2d.setColor(Color.RED);
        g2d.fillRect(50, 50, 200, 200);

        g2d.dispose();

        // Save the image to a file
        try {
            ImageIO.write(bufferedImage, "jpg", new File("test_image.jpg"));
            System.out.println("Test image created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
