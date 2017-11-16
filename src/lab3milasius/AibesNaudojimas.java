package lab3milasius;

import javafx.application.Application;
import javafx.stage.Stage;
import laborai.gui.fx.Lab3WindowFX;

public class AibesNaudojimas extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Lab3WindowFX.createAndShowFXGUI(primaryStage, new Lab3WindowFXImplementation(primaryStage));
    }
}
