package com.example.kafka.kafka;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2GRAY;
import static org.opencv.core.CvType.CV_64F;

@Service
public class ImageProcessing {

    public ResponseEntity<List<Double>> processImage(MultipartFile image) {
        List<Double> list = new ArrayList<>();

        try {
            // Convert MultipartFile to BufferedImage
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));

            // Convert BufferedImage to OpenCV Mat
            Mat imageMat = bufferedImageToMat(bufferedImage);

            // Check the number of channels and convert to 3 channels if necessary
            if (imageMat.channels() != 3) {
                System.out.println("Unexpected number of channels: " + imageMat.channels() + ". Converting to 3 channels.");
                Mat convertedMat = new Mat();
                opencv_imgproc.cvtColor(imageMat, convertedMat, opencv_imgproc.COLOR_BGR2RGB);
                imageMat = convertedMat;
            }

            double blurScore = calculateBlur(imageMat);
            list.add((double) blurScore);

            // Check for skew
            boolean isSkewed = checkSkew(imageMat);
            list.add((double) (isSkewed ? 1 : 0));

            // Check for overexposure
            boolean isOverExposed = checkOverExposure(imageMat);
            list.add((double) (isOverExposed ? 1 : 0));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(list);

//        try {
//            // Convert MultipartFile to BufferedImage
//            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));
//
//            // Convert BufferedImage to OpenCV Mat
//            Mat imageMat = bufferedImageToMat(bufferedImage);
//
//            // Check for blur
////            double blurScore = calculateBlur(imageMat);
//            double blurScore = 0;
//            list.add(blurScore);
//
//            // Check for skew
//            boolean isSkewed = checkSkew(imageMat);
//            list.add((double) (isSkewed ? 1 : 0));
//
//            // Check for overexposure
//            boolean isOverExposed = checkOverExposure(imageMat);
//            list.add((double) (isOverExposed ? 1 : 0));
//
//        } catch (Exception e) {
//            e.printStackTrace();
////            double min = 0;
////            double max = 1;
////            Random r = new Random();
////            double randomValue = min + (max - min) * r.nextDouble();
////            list.add(randomValue);
////            list.add(min + (max - min) * r.nextDouble());
////            list.add(min + (max - min) * r.nextDouble());
////            return ResponseEntity.ok(list);
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        return ResponseEntity.ok(list);
    }

    private static Mat bufferedImageToMat(BufferedImage bi) {
        // Convert BufferedImage to Mat
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), opencv_imgcodecs.IMWRITE_EXR_TYPE);
        byte[] data = ((java.awt.image.DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.data().put(data);
        return mat;
    }

    private static double calculateBlur(Mat imageMat) {
        Mat gray = new Mat();
        opencv_imgproc.cvtColor(imageMat, gray, COLOR_BGR2GRAY);

        Mat laplacian = new Mat();
        opencv_imgproc.Laplacian(gray, laplacian, CV_64F);

        // Convert laplacian data to a format suitable for StandardDeviation calculation
        double[] laplacianData = new double[(int) (laplacian.total() * laplacian.channels())];
//        laplacian.get(0, 0, laplacianData);

        StandardDeviation sd = new StandardDeviation();
        return sd.evaluate(laplacianData);
    }

    private static boolean checkSkew(Mat imageMat) {
        // Placeholder for skew detection (implement your skew detection logic here)
        return false;
    }

    private static boolean checkOverExposure(Mat imageMat) {
        // Ensure the image has 3 channels (BGR) before converting to grayscale
        if (imageMat.channels() != 3) {
            throw new RuntimeException("Unexpected number of channels in input image: " + imageMat.channels());
        }

        // Convert the input image to grayscale
        Mat gray = new Mat();
        opencv_imgproc.cvtColor(imageMat, gray, COLOR_BGR2GRAY);

        int whitePixelCount = 0;
        int totalPixels = gray.rows() * gray.cols();

        // Get image data into an array for efficient processing
        byte[] data = new byte[(int) (gray.total() * gray.channels())];
        gray.data().get(data);

        for (byte pixelValue : data) {
            // Convert byte to unsigned integer
            int pixelValueInt = pixelValue & 0xFF;
            if (pixelValueInt >= 240) {
                whitePixelCount++;
            }
        }

        double whitePixelRatio = (double) whitePixelCount / totalPixels;
        return whitePixelRatio > 0.5;
    }

//    private static boolean checkOverExposure(Mat imageMat) {
//        Mat gray = new Mat();
//        opencv_imgproc.cvtColor(imageMat, gray, COLOR_BGR2GRAY);
//
//        int whitePixelCount = 0;
//        int totalPixels = gray.rows() * gray.cols();
//
//        // Get image data into an array for efficient processing
//        byte[] data = new byte[totalPixels];
//        gray.data().get(data);
//
//        for (byte pixelValue : data) {
//            // Convert byte to unsigned integer
//            int pixelValueInt = pixelValue & 0xFF;
//            if (pixelValueInt >= 240) {
//                whitePixelCount++;
//            }
//        }
//
//        double whitePixelRatio = (double) whitePixelCount / totalPixels;
//        return whitePixelRatio > 0.5;
//    }



//    private static boolean checkOverExposure(Mat imageMat) {
//        Mat gray = new Mat();
//        opencv_imgproc.cvtColor(imageMat, gray, COLOR_BGR2GRAY);
//
//        int whitePixelCount = 0;
//        int totalPixels = gray.rows() * gray.cols();
//
//        // Get image data into an array for efficient processing
//        byte[] data = new byte[totalPixels];
//        gray.data().get(data);
//
//        for (byte pixelValue : data) {
//            if (pixelValue & 0xFF >= 240) {
//                whitePixelCount++;
//            }
//        }
//
//        double whitePixelRatio = (double) whitePixelCount / totalPixels;
//        return whitePixelRatio > 0.5;
//    }

//    private static Mat convertToThreeChannels(Mat imageMat) {
//        Mat convertedMat = new Mat();
//
//        // If the image has 1 channel (grayscale), convert it to 3 channels (RGB)
//        if (imageMat.channels() == 1) {
//            opencv_imgproc.cvtColor(imageMat, convertedMat, opencv_imgproc.COLOR_GRAY2BGR);
//        }
//        // If the image has 4 channels (e.g., RGBA), convert it to 3 channels (RGB)
//        else if (imageMat.channels() == 4) {
//            opencv_imgproc.cvtColor(imageMat, convertedMat, opencv_imgproc.COLOR_RGBA2BGR);
//        }
//        // If the image has more than 3 channels (e.g., 7), handle it
//        else if (imageMat.channels() != 3) {
//            System.err.println("Unexpected number of channels: " + imageMat.channels() + ". Forcing to 3 channels.");
//            List<Mat> channels = new ArrayList<>();
//            opencv_core.split(imageMat, channels);
//
//            // If there are more than 3 channels, keep the first 3
//            while (channels.size() > 3) {
//                channels.remove(channels.size() - 1);
//            }
//
//            // If there are fewer than 3 channels, replicate the first channel
//            while (channels.size() < 3) {
//                channels.add(channels.get(0).clone());
//            }
//
//            opencv_core.merge(channels, convertedMat);
//        } else {
//            // If it already has 3 channels, just return the original image
//            convertedMat = imageMat;
//        }
//
//        return convertedMat;
//    }
}
