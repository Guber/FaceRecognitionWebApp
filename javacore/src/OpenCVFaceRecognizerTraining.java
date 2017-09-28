import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_face;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_face.createEigenFaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.createFisherFaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

/**
 * This class is used to train our recognizer methods and save the training data into xml files.
 */
public class OpenCVFaceRecognizerTraining {
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

        int counter = 0;
        opencv_face.FaceRecognizer faceRecognizer;

        for (File image : imageFiles) {
            opencv_core.Mat orig_img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);

            // images are resized to designed reolution that is also used in the camre input
            // NOTICE test image also needs to be of this resolution
            Mat resizeimage = new Mat();
            Size sz = new Size(800,640);
            Mat img = new Mat();
            resize( orig_img, img, sz );


            int label = Integer.parseInt(image.getName().split("\\-")[0]);
            images.put(counter, img);
            labelsBuf.put(counter, label);

            counter++;
        }

        //training all of our three algorithms
        try {
            faceRecognizer = createFisherFaceRecognizer();
            faceRecognizer.train(images, labels);
            faceRecognizer.save("resources/facerecognizerFisherFace.xml");
            System.out.println("Succesfully completed FisherFace training.");
        } catch (Exception e) {
            System.err.println("Eroor whilst training FisherFace method.");
        }

        try {
            faceRecognizer = createEigenFaceRecognizer();
            faceRecognizer.train(images, labels);
            faceRecognizer.save("resources/facerecognizerEigenFace.xml");
            System.out.println("Succesfully completed EigenFace training.");
        } catch (Exception e) {
            System.err.println("Eroor whilst training EigenFace method.");
        }

        try {
            faceRecognizer = createLBPHFaceRecognizer();
            faceRecognizer.train(images, labels);
            faceRecognizer.save("resources/facerecognizerLBPHFace.xml");
            System.out.println("Succesfully completed LBPHFace training.");
        } catch (Exception e) {
            System.err.println("Eroor whilst training LBPH method.");
        }

    }
}