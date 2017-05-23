package ua.test.opencv;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * JFrame wrapper for {@link ConsoleFaceDetector}.
 */
public class FaceDetectionFrame extends JFrame {

    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 700;

    public FaceDetectionFrame() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JButton sourceImageButton = new JButton("Open");

        JLabel sourceImageIconLabel = new JLabel();
        JLabel resultImageIconLabel = new JLabel();

        JTextField cascadeClassifierTextField = new JTextField(20);

        sourceImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            int returnVal = fileChooser.showOpenDialog(FaceDetectionFrame.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File sourceFile = fileChooser.getSelectedFile();
                String absoluteFilePath = sourceFile.getAbsolutePath();

                System.out.println("\nRunning FaceDetector.");

                CascadeClassifier faceDetector = new CascadeClassifier(cascadeClassifierTextField.getText());

                Mat image = Imgcodecs.imread(sourceFile.getAbsolutePath());

                MatOfRect faceDetections = new MatOfRect();
                faceDetector.detectMultiScale(image, faceDetections);

                int facesDetectedCount = faceDetections.toArray().length;

                System.out.println(String.format("Detected %s faces", facesDetectedCount));

                for (Rect rect : faceDetections.toArray()) {
                    Imgproc.rectangle(image, new org.opencv.core.Point(rect.x, rect.y), new org.opencv.core.Point(rect.x + rect.width, rect.y + rect.height),
                            new Scalar(0, 0, 255));
                }

                String filePath = absoluteFilePath.substring(0, absoluteFilePath.lastIndexOf(File.separator));

                String resultFilePath = filePath + File.separator + sourceFile.getName() + "_facesDetectedCount_" + facesDetectedCount;

                System.out.println(String.format("Writing %s", resultFilePath));

                Imgcodecs.imwrite(resultFilePath, image);

                try {
                    ImageIcon sourceImageIcon = new ImageIcon();
                    ImageIcon resultImageIcon = new ImageIcon();

                    sourceImageIcon.setImage(loadImage(sourceFile.getAbsolutePath(), sourceImageIconLabel.getWidth(), sourceImageIconLabel.getHeight()));
                    resultImageIcon.setImage(loadImage(resultFilePath, resultImageIconLabel.getWidth(), sourceImageIconLabel.getHeight()));

                    sourceImageIconLabel.setIcon(sourceImageIcon);
                    resultImageIconLabel.setIcon(resultImageIcon);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        JPanel bottomPanel = new JPanel();

        bottomPanel.add(new JLabel("Open image"));
        bottomPanel.add(sourceImageButton);
        bottomPanel.add(new JLabel("Path to cascade classifier file"));
        bottomPanel.add(cascadeClassifierTextField);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sourceImageIconLabel, resultImageIconLabel);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setDividerLocation(FRAME_WIDTH / 2);

        mainPanel.add(mainSplitPane, BorderLayout.CENTER);

        this.add(mainPanel);
    }

    /**
     * Load image to {@link BufferedImage}.
     *
     * @param imagePath -   path to image
     *
     * @throws IOException -
     *
     * @return BufferedImage
     */
    private Image loadImage(String imagePath, int width, int height) throws IOException {
         return ImageIO.read(new File(imagePath)).getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    /**
     * Application input point.
     *
     * @param args  - command line arguments
     *
     * @throws Exception    -
     */
    public static void main(String[] args) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        FaceDetectionFrame faceDetectionFrame = new FaceDetectionFrame();

        faceDetectionFrame.setTitle("Face detection");
        faceDetectionFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        faceDetectionFrame.setVisible(true);
        faceDetectionFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        faceDetectionFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });
    }
}
