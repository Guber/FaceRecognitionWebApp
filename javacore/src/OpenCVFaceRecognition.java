import java.util.HashMap;
import java.util.Map;

import static org.bytedeco.javacpp.opencv_face.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class OpenCVFaceRecognition {
    public static void main(String[] args) {

        //recognition for test image, path to test image must be given in argument
        Mat image = imread(args[0], CV_LOAD_IMAGE_GRAYSCALE);
        final int IMG_WIDTH = 800;
        final int IMG_HEIGHT = 640;

        Mat testImage = new Mat();
        Size sz = new Size(800, 640);
        resize(image, testImage, sz);

        //loading trained recognizers
        FaceRecognizer faceRecognizer1 = createFisherFaceRecognizer();
        FaceRecognizer faceRecognizer2 = createEigenFaceRecognizer();
        FaceRecognizer faceRecognizer3 = createLBPHFaceRecognizer();

        faceRecognizer1.load("resources/facerecognizerFisherFace.xml");
        faceRecognizer2.load("resources/facerecognizerEigenFace.xml");
        faceRecognizer3.load("resources/facerecognizerLBPHFace.xml");

        int n[] = new int[1];
        double p[] = new double[1];

        int label;
        double confidence;

        //names hardcoded for our dataset to be changed or removed if new training photos are used
        Map<Integer, String> names = new HashMap<Integer, String>();
        names.put(1, "EG");
        names.put(2, "GG");
        names.put(3, "GB");
        names.put(4, "HJ");
        names.put(5, "IM");
        names.put(6, "IJ");
        names.put(7, "JM");
        names.put(8, "LM");
        names.put(9, "LS");
        names.put(10, "MK");
        names.put(11, "MJ");
        names.put(12, "PK");
        names.put(13, "SE");
        names.put(14, "AD");


        /*
        //Without names

        faceRecognizer1.predict(testImage, n, p);
        label = n[0];
        //confidence = (200 - p[0]) / 200;
        confidence = p[0];
        System.out.println("Predicted label for FisherFace: " + label + " confidence: " + String.format("%.2f", confidence) + "<br>");

        faceRecognizer2.predict(testImage, n, p);
        label = n[0];
        //confidence = (200 - p[0]) / 200;
        confidence = p[0];
        System.out.println("Predicted label for EigenFace: " + label + " confidence: " + String.format("%.2f", confidence) + "<br>");

        faceRecognizer3.predict(testImage, n, p);
        label = n[0];
        //confidence = (200 - p[0]) / 200;
        confidence = p[0];
        System.out.println("Predicted label for LBPHFace: " + label + " confidence: " + String.format("%.2f", confidence) + "<br>");
        */

        faceRecognizer1.predict(testImage, n, p);
        label = n[0];
        //confidence = (200 - p[0]) / 200;
        confidence = p[0];
        System.out.println("Predicted label for FisherFace: " + label + " name: " + names.get(label) +  " confidence: " + String.format("%.2f", confidence) + "<br>");

        faceRecognizer2.predict(testImage, n, p);
        label = n[0];
        //confidence = (200 - p[0]) / 200;
        confidence = p[0];
        System.out.println("Predicted label for EigenFace: " + label + " name: " + names.get(label) +  " confidence: " + String.format("%.2f", confidence) + "<br>");

        faceRecognizer3.predict(testImage, n, p);
        label = n[0];
        //confidence = (200 - p[0]) / 200;
        confidence = p[0];
        System.out.println("Predicted label for LBPHFace: " + label + " name: " + names.get(label) +  " confidence: " + String.format("%.2f", confidence) + "<br>");
    }
}