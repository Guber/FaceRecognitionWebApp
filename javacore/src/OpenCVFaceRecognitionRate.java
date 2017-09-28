import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_face;

import java.io.File;
import java.io.FilenameFilter;
import static org.bytedeco.javacpp.opencv_face.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;


public class OpenCVFaceRecognitionRate {
    public static void main(String[] args) {

        //loading the recognizers
        //opencv_face.FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
        //opencv_face.FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
        opencv_face.FaceRecognizer faceRecognizer = createLBPHFaceRecognizer();

        //faceRecognizer.load("./resources/facerecognizerFisherFace.xml");
        //faceRecognizer.load("./resources/facerecognizerEigenFace.xml");
        faceRecognizer.load("./resources/facerecognizerLBPHFace.xml");

        int n[] = new int[1];
        double p[] = new double[1];

        int label;
        int label_predicted;
        double confidence;

        //direcotry of test images
        String testDir = args[0];
        File root = new File(testDir);

        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
            }
        };

        File[] imageFiles = root.listFiles(imgFilter);

        try {
            opencv_core.MatVector images = new opencv_core.MatVector(imageFiles.length);
        } catch (NullPointerException e) {
            System.out.println("In folder:" + testDir + "there are no iamges.");
            System.exit(0);

        }

        opencv_core.MatVector images = new opencv_core.MatVector(imageFiles.length);

        Mat testimage, test;
        Size sz;
        int counter = 0, correct = 0;

        for (File image : imageFiles) {
            opencv_core.Mat origimg = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
            testimage = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
            sz = new Size(800, 640);
            test = new Mat();
            resize(testimage, test, sz);

            faceRecognizer.predict(test, n, p);
            label_predicted = n[0];
            //confidence = (200 - p[0]) / 200;
            confidence = p[0];

            label = Integer.parseInt(image.getName().split("\\-")[0]);

            if (label_predicted == label) {
                correct++;
            }
            System.out.println(label + " - " + label_predicted + "   " + correct);
            counter++;
        }

        System.out.println((float) correct / counter);

    }
}