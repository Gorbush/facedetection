package ua.test.opencv;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Console version of face detection.
 */
public class ConsoleFaceDetector {
    private void runFaceRecognition() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        System.out.println("\nRunning ConsoleFaceDetector");

        CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("/haarcascade_frontalface_alt.xml").getPath());
        Mat image = Imgcodecs.imread("/home/winter/Desktop/FACE 1.JPG");

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }

        String filename = "/home/winter/Desktop/result1.png";
        System.out.println(String.format("Writing %s", filename));
        Imgcodecs.imwrite(filename, image);
    }

    public static void main(String[] args) {
        ConsoleFaceDetector consoleFaceDetector = new ConsoleFaceDetector();

        consoleFaceDetector.runFaceRecognition();
    }
}
