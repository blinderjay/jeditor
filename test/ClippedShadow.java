import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.*;
//import org.scenicview.ScenicView;

// Java 8 code
public class ClippedShadow extends Application {

    private static final int shadowSize = 50;

    @Override public void start(final Stage stage) {
      
        stage.initStyle(StageStyle.TRANSPARENT);
        
        StackPane stackPane = new StackPane(createShadowPane());
        SplitPane splitpane = new SplitPane();
        

        stackPane.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.5);" 
+
        "-fx-background-insets: " + shadowSize + ";"
        );



        Scene scene = new Scene(stackPane, 450, 450);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    // Create a shadow effect as a halo around the pane and not within
    // the pane's content area.
    private Pane createShadowPane() {
        Pane shadowPane = new Pane();
        // a "real" app would do this in a CSS stylesheet.
        shadowPane.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.5);" +
                "-fx-effect: dropshadow(gaussian, red, " + shadowSize + ", 0, 0, 0);" +
                "-fx-background-insets: " + shadowSize + ";"
        );

        Rectangle innerRect = new Rectangle();
        Rectangle outerRect = new Rectangle();
        shadowPane.layoutBoundsProperty().addListener(
                (observable, oldBounds, newBounds) -> {
                    innerRect.relocate(
                            newBounds.getMinX() + shadowSize,
                            newBounds.getMinY() + shadowSize
                    );
                    innerRect.setWidth(newBounds.getWidth() - shadowSize * 2);
                    innerRect.setHeight(newBounds.getHeight() - shadowSize * 2);

                    outerRect.setWidth(newBounds.getWidth());
                    outerRect.setHeight(newBounds.getHeight());

                    Shape clip = Shape.subtract(outerRect, innerRect);
                    shadowPane.setClip(clip);
                }
        );

        return shadowPane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}