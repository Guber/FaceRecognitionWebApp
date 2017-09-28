import org.bytedeco.javacpp.opencv_core.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.cvShowImage;
import static org.bytedeco.javacpp.opencv_highgui.cvWaitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_AA;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangle;
import static org.bytedeco.javacpp.opencv_imgproc.cvResize;
import static org.bytedeco.javacpp.opencv_objdetect.*;
import java.io.File;

public class OpenCVFaceDetector{

    //Load haar classifier XML file
    public static final String XML_FILE = "resources/haarcascade_frontalface_default.xml";


    public static void main(String[] args){
        File[] files = new File(args[0]).listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
            } else {
                //Load image
                IplImage img = cvLoadImage(file.getAbsolutePath());
                detect(img, file.getAbsolutePath());
            }
        }
    }

    //Detect for face using classifier XML file
    public static void detect(IplImage src, String path){
        final int IMG_WIDTH = 800;
        final int IMG_HEIGHT = 640;

        IplImage resizedImage = src;
        //Define classifier
        CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad(XML_FILE));

        CvMemStorage storage = CvMemStorage.create();

        //Detect objects
        CvSeq sign = cvHaarDetectObjects(
                src,
                cascade,
                storage,
                1.5,
                3,
                CV_HAAR_DO_CANNY_PRUNING);

        cvClearMemStorage(storage);

        int total_Faces = sign.total();
        if (total_Faces > 1) {
            total_Faces = 1;
        }
        //Draw rectangles around detected objects
        for(int i = 0; i < total_Faces; i++){
            CvRect r = new CvRect(cvGetSeqElem(sign, i));
            cvRectangle (
                    src,
                    cvPoint(r.x(), r.y()),
                    cvPoint(r.width() + r.x(), r.height() + r.y()),
                    CvScalar.RED,
                    2,
                    CV_AA,
                    0);

            cvSetImageROI(src, r);
            IplImage cropped = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
            // Copy original image (only ROI) to the cropped image
            cvCopy(src, cropped);


            resizedImage = IplImage.create(IMG_WIDTH, IMG_HEIGHT, src.depth(), src.nChannels());

            //cvSmooth(origImg, origImg);
            cvResize(src, resizedImage);

            cvSaveImage(path,  resizedImage);
        }

        //Display result
        //cvShowImage("Result",  resizedImage);
        //cvWaitKey(0);

    }
}