import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_face;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_core.Size;
import static org.bytedeco.javacpp.opencv_face.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

public class OpenCVFaceRecognitionTraining {
    public static void main(String[] args) {

        // Training direcotory is passed through first argument, this should be n absoltue path to the img folder
        String trainingDir = args[0];
        File root = new File(trainingDir);

        // images filter
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
            System.out.println("In folder:" + trainingDir + "there are no images.");
            System.exit(0);
        }

        opencv_core.MatVector images = new opencv_core.MatVector(imageFiles.length);
        opencv_core.Mat labels = new opencv_core.Mat(imageFiles.length, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.getIntBuffer();
        Map<Integer, String> names = new HashMap<Integer, String>();

        int counter = 0;
        opencv_face.FaceRecognizer faceRecognizer0;
        opencv_face.FaceRecognizer faceRecognizer1;
        opencv_face.FaceRecognizer faceRecognizer2;

        for (File image : imageFiles) {
            opencv_core.Mat orig_img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);

            // images are resized to designed reolution that is also used in the camre input
            // NOTICE test image also needs to be of this resolution
            Mat resizeimage = new Mat();
            Size sz = new Size(800, 640);
            Mat img = new Mat();
            resize(orig_img, img, sz);


            int label = Integer.parseInt(image.getName().split("\\-")[0]);
            String name = image.getName().split("\\-")[1];
            names.put(label, name);
            images.put(counter, img);
            labelsBuf.put(counter, label);

            counter++;
        }

        faceRecognizer0 = createFisherFaceRecognizer();
        faceRecognizer0.train(images, labels);
        faceRecognizer1 = createEigenFaceRecognizer();
        faceRecognizer1.train(images, labels);
        faceRecognizer2 = createLBPHFaceRecognizer();
        faceRecognizer2.train(images, labels);

        //recognition for test image, path to test image must be given in argument
        Mat image = imread(args[1], CV_LOAD_IMAGE_GRAYSCALE);

        Mat testImage = new Mat();
        Size sz = new Size(800, 640);
        resize(image, testImage, sz);


        int n[] = new int[1];
        double p[] = new double[1];

        int label;
        double confidence;

        faceRecognizer0.predict(testImage, n, p);
        label = n[0];
        //confidence = (200 - p[0]) / 200;
        confidence = p[0];
        System.out.println("Predicted label for FisherFace: " + label + " name: " + names.get(label) +  " confidence: " + String.format("%.2f", confidence) + "<br>");

        faceRecognizer1.predict(testImage, n, p);
        label = n[0];
        //confidence = (200 - p[0]) / 200;
        confidence = p[0];
        System.out.println("Predicted label for EigenFace: " + label + " name: " + names.get(label) +  " confidence: " + String.format("%.2f", confidence) + "<br>");

        faceRecognizer2.predict(testImage, n, p);
        label = n[0];
        //confidence = (200 - p[0]) / 200;
        confidence = p[0];
        System.out.println("Predicted label for LBPHFace: " + label + " name: " + names.get(label) +  " confidence: " + String.format("%.2f", confidence) + "<br>");

    }
}