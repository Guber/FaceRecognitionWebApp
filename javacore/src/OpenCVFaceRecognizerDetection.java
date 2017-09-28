import org.bytedeco.javacpp.opencv_objdetect;

import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_face.*;
import static org.bytedeco.javacpp.opencv_highgui.cvShowImage;
import static org.bytedeco.javacpp.opencv_highgui.cvWaitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;

public class OpenCVFaceRecognizerDetection {
    public static void main(String[] args) {

        //Mat testImage = imread(args[1], CV_LOAD_IMAGE_GRAYSCALE);
        IplImage testImage = cvLoadImage(args[0], CV_LOAD_IMAGE_GRAYSCALE);

        final String XML_FILE = "resources/haarcascade_frontalface_default.xml";
        final int IMG_WIDTH = 800;
        final int IMG_HEIGHT = 640;

        FaceRecognizer faceRecognizer1 = createFisherFaceRecognizer();
        FaceRecognizer faceRecognizer2 = createEigenFaceRecognizer();
        FaceRecognizer faceRecognizer3 = createLBPHFaceRecognizer();

        faceRecognizer1.load("resources/facerecognizerFisherFace.xml");
        faceRecognizer2.load("resources/facerecognizerEigenFace.xml");
        faceRecognizer3.load("resources/facerecognizerLBPHFace.xml");

        int n[] = new int[1];
        double p[] = new double[1];

        /* EG */
        //Define classifier
        opencv_objdetect.CvHaarClassifierCascade cascade = new opencv_objdetect.CvHaarClassifierCascade(cvLoad(XML_FILE));
        CvMemStorage storage = CvMemStorage.create();

        //Detect objects
        CvSeq sign = cvHaarDetectObjects(
                testImage,
                cascade,
                storage,
                1.5,
                3,
                CV_HAAR_DO_CANNY_PRUNING);

        cvClearMemStorage(storage);
        int total_Faces = sign.total();

        if (total_Faces == 0) {
            System.out.println("No matching person found.");
            System.exit(0);
        }

        //Draw rectangles around detected objects
        for (int i = 0; i < total_Faces; i++) {
            CvRect r = new CvRect(cvGetSeqElem(sign, i));
            cvRectangle(
                    testImage,
                    cvPoint(r.x(), r.y()),
                    cvPoint(r.width() + r.x(), r.height() + r.y()),
                    CvScalar.RED,
                    2,
                    CV_AA,
                    0);

            cvSetImageROI(testImage, r);

            IplImage resizedImage = IplImage.create(IMG_WIDTH, IMG_HEIGHT, testImage.depth(), testImage.nChannels());

            //cvSmooth(origImg, origImg);
            cvResize(testImage, resizedImage);

            //IplImage cropped = cvCreateImage(cvGetSize(testImage), testImage.depth(), testImage.nChannels());

            Mat test = cvarrToMat(resizedImage);
            //Mat test = imread(args[1], CV_LOAD_IMAGE_GRAYSCALE);

            int label;
            double confidence;

            System.out.println("Pearson: " + (i + 1) + "<br>");

            faceRecognizer1.predict(test, n, p);
            label = n[0];
            //confidence = (200 - p[0]) / 200;
            confidence = p[0];
            System.out.println("Predicted label for FisherFace: " + label + " confidence: " + String.format("%.2f", confidence) + "<br>");

            faceRecognizer2.predict(test, n, p);
            label = n[0];
            //confidence = (200 - p[0]) / 200;
            confidence = p[0];
            System.out.println("Predicted label for EigenFace: " + label + " confidence: " + String.format("%.2f", confidence) + "<br>");

            faceRecognizer3.predict(test, n, p);
            label = n[0];
            //confidence = (200 - p[0]) / 200;
            confidence = p[0];
            System.out.println("Predicted label for LBPHFace: " + label + " confidence: " + String.format("%.2f", confidence) + "<br>");

            cvShowImage("Result", resizedImage);
            cvWaitKey(0);

            System.out.println("<br<br" + "<hr>" + "<br><br>");
        }


    }
}