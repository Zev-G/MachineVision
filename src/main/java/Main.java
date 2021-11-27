import com.github.sarxos.webcam.Webcam;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.awt.image.BufferedImage;

public class Main extends Application {

    private long minTime = 1000 / 60;
    private BufferedImage image;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        final Webcam webcam = Webcam.getDefault();
        webcam.open();


        Canvas canvas = new Canvas();
        canvas.getGraphicsContext2D().setStroke(Color.BLUE);
        Scene scene = new Scene(new StackPane(canvas));

        Thread thread = new Thread(() -> {
            while (true) {
                long start = System.currentTimeMillis();
                image = webcam.getImage();

                double[][] polygon = GroupShape.getShape(image.getWidth() / 2, image.getHeight() / 2, 10, image,100);

                canvas.setWidth(image.getWidth());
                canvas.setHeight(image.getHeight());

                WritableImage drawnImage = new WritableImage(image.getWidth(), image.getHeight());
                PixelWriter writer = drawnImage.getPixelWriter();
                for (int x = 0; x < image.getWidth(); x++) {
                    for (int y = 0; y < image.getHeight(); y++) {
                        int rgb = image.getRGB(x, y);
                        int red = (rgb >> 16) & 0xFF,
                                green = (rgb >> 8) & 0xFF,
                                blue = rgb & 0xFF;
                        writer.setColor(x, y, Color.rgb(red, green, blue));
                    }
                }

                canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                canvas.getGraphicsContext2D().drawImage(drawnImage, 0, 0);
                int[] rgb = Colours.split(image.getRGB(image.getWidth() / 2, image.getHeight() / 2));
                canvas.getGraphicsContext2D().setFill(Color.rgb(rgb[0], rgb[1], rgb[2]));
                canvas.getGraphicsContext2D().fillPolygon(polygon[0], polygon[1], 10);
                canvas.getGraphicsContext2D().strokePolygon(polygon[0], polygon[1], 10);

                long time = System.currentTimeMillis() - start;
                if (time < minTime) {
                    try {
                        Thread.sleep(minTime - time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        stage.setScene(scene);
        stage.show();
    }
}
