package laborai.demo;

import java.util.Locale;
import javafx.application.Application;
import javafx.stage.Stage;
import laborai.gui.fx.Lab3WindowFX;
import lab3milasius.Lab3WindowFXImplementation;

/*
 * Darbo atlikimo tvarka - čia yra JavaFX pradinė klasė.
 */
public class VykdymoModulisFX extends Application {

    public static void main(String [] args) {
        VykdymoModulisFX.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(Locale.US); // Suvienodiname skaičių formatus 
        AutoTestai.aibėsTestas();
        Lab3WindowFX.createAndShowFXGUI(primaryStage, new Lab3WindowFXImplementation(primaryStage));
    }
}