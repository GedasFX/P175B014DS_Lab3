package laborai.gui.fx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import laborai.gui.MyException;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Lab3 langas su JavaFX
 * <pre>
 *                   Lab3WindowFX (BorderPane)
 *  |-------------------------Top-------------------------|
 *  |                        menuFX                       |
 *  |------------------------Center-----------------------|
 *  |  |-----------------------------------------------|  |
 *  |  |                                               |  |
 *  |  |                                               |  |
 *  |  |                    taOutput                   |  |
 *  |  |                                               |  |
 *  |  |                                               |  |
 *  |  |                                               |  |
 *  |  |                                               |  |
 *  |  |                                               |  |
 *  |  |-----------------------------------------------|  |                                           |
 *  |------------------------Bottom-----------------------|
 *  |  |~~~~~~~~~~~~~~~~~~~paneBottom~~~~~~~~~~~~~~~~~~|  |
 *  |  |                                               |  |
 *  |  |  |-------------||------------||------------|  |  |
 *  |  |  | paneButtons || paneParam1 || paneParam2 |  |  |
 *  |  |  |             ||            ||            |  |  |
 *  |  |  |-------------||------------||------------|  |  |
 *  |  |                                               |  |
 *  |  |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|  |
 *  |-----------------------------------------------------|
 * </pre>
 *
 * @author darius.matulis@ktu.lt
 */
public abstract class Lab3WindowFX extends BorderPane implements EventHandler<ActionEvent> {

    protected static final ResourceBundle MESSAGES = ResourceBundle.getBundle("laborai.gui.messages");

    protected static final int TF_WIDTH = 120;
    protected static final int TF_WIDTH_SMALLER = 70;

    protected static final double SPACING = 5.0;
    protected static final Insets INSETS = new Insets(SPACING);
    protected static final double SPACING_SMALLER = 2.0;
    protected static final Insets INSETS_SMALLER = new Insets(SPACING_SMALLER);

    protected final TextArea taOutput = new TextArea();
    protected final GridPane paneBottom = new GridPane();
    protected final GridPane paneParam2 = new GridPane();
    protected final TextField tfDelimiter = new TextField();
    protected final TextField tfInput = new TextField();
    protected final ComboBox cmbTreeType = new ComboBox();
    protected PanelsFX paneParam1;
    protected PanelsFX paneButtons;
    protected MenuFX menuFX;
    protected final Stage stage;

    protected double coef;
    protected final String[] errors;

    public Lab3WindowFX(Stage stage) {
        this.stage = stage;
        errors = new String[]{
            MESSAGES.getString("error1"),
            MESSAGES.getString("error2"),
            MESSAGES.getString("error3"),
            MESSAGES.getString("error4")
        };
        initComponents();
    }

    private void initComponents() {
        //======================================================================
        // Sudaromas rezultatų išvedimo VBox klasės objektas, kuriame
        // talpinamas Label ir TextArea klasės objektai
        //======================================================================        
        VBox vboxTaOutput = new VBox();
        vboxTaOutput.setPadding(INSETS_SMALLER);
        VBox.setVgrow(taOutput, Priority.ALWAYS);
        vboxTaOutput.getChildren().addAll(new Label(MESSAGES.getString("border1")), taOutput);
        //======================================================================
        // Formuojamas mygtukų tinklelis (mėlynas). Naudojama klasė PanelsFX.
        //======================================================================
        paneButtons = new PanelsFX(
                new String[]{
                    MESSAGES.getString("button1"),
                    MESSAGES.getString("button2"),
                    MESSAGES.getString("button3"),
                    MESSAGES.getString("button4"),
                    MESSAGES.getString("button5"),
                    MESSAGES.getString("button6"),
                    MESSAGES.getString("button7")},
                2, 4);
        disableButtons(true);
        //======================================================================
        // Formuojama pirmoji parametrų lentelė (žalia). Naudojama klasė PanelsFX.
        //======================================================================
        paneParam1 = new PanelsFX(
                new String[]{
                    MESSAGES.getString("lblParam11"),
                    MESSAGES.getString("lblParam12"),
                    MESSAGES.getString("lblParam13"),
                    MESSAGES.getString("lblParam14"),
                    MESSAGES.getString("lblParam15")},
                new String[]{
                    MESSAGES.getString("tfParam11"),
                    MESSAGES.getString("tfParam12"),
                    MESSAGES.getString("tfParam13"),
                    MESSAGES.getString("tfParam14"),
                    MESSAGES.getString("tfParam15")},
                TF_WIDTH_SMALLER);
        //======================================================================
        // Formuojama antroji parametrų lentelė (gelsva).
        //======================================================================
        paneParam2.setAlignment(Pos.CENTER);
        paneParam2.setNodeOrientation(NodeOrientation.INHERIT);
        paneParam2.setVgap(SPACING);
        paneParam2.setHgap(SPACING);
        paneParam2.setPadding(INSETS);

        paneParam2.add(new Label(MESSAGES.getString("lblParam21")), 0, 0);
        paneParam2.add(new Label(MESSAGES.getString("lblParam22")), 0, 1);
        paneParam2.add(new Label(MESSAGES.getString("lblParam23")), 0, 2);

        cmbTreeType.setItems(FXCollections.observableArrayList(
            MESSAGES.getString("cmbTreeType1"),
            MESSAGES.getString("cmbTreeType2"),
            MESSAGES.getString("cmbTreeType3")
        ));
        cmbTreeType.setPrefWidth(TF_WIDTH);
        cmbTreeType.getSelectionModel().select(0);
        paneParam2.add(cmbTreeType, 1, 0);

        tfDelimiter.setPrefWidth(TF_WIDTH);
        tfDelimiter.setAlignment(Pos.CENTER);
        paneParam2.add(tfDelimiter, 1, 1);

        // Vėl pirmas stulpelis, tačiau plotis - 2 celės
        tfInput.setEditable(false);
        tfInput.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        paneParam2.add(tfInput, 0, 3, 2, 1);
        //======================================================================
        // Formuojamas bendras parametrų panelis (tamsiai pilkas).
        //======================================================================
        paneBottom.setPadding(INSETS);
        paneBottom.setHgap(SPACING);
        paneBottom.setVgap(SPACING);
        paneBottom.add(paneButtons, 0, 0);
        paneBottom.add(paneParam1, 1, 0);
        paneBottom.add(paneParam2, 2, 0);
        paneBottom.alignmentProperty().bind(new SimpleObjectProperty<>(Pos.CENTER));

        menuFX = new MenuFX() {
            @Override
            public void handle(ActionEvent ae) {
                Object source = ae.getSource();
                if (source.equals(menuFX.getMenus().get(0).getItems().get(0))) {
                    fileChooseMenu();
                } else if (source.equals(menuFX.getMenus().get(0).getItems().get(1))) {
                    KsFX.ounerr(taOutput, MESSAGES.getString("msg1"));
                } else if (source.equals(menuFX.getMenus().get(0).getItems().get(3))) {
                    System.exit(0);
                } else if (source.equals(menuFX.getMenus().get(1).getItems().get(0))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initStyle(StageStyle.UTILITY);
                    alert.setTitle(MESSAGES.getString("menuItem21"));
                    alert.setHeaderText(MESSAGES.getString("author"));
                    alert.showAndWait();
                }
            }
        };

        //======================================================================
        // Formuojamas Lab3 langas
        //======================================================================          
        // ..viršuje, centre ir apačioje talpiname objektus..
        setTop(menuFX);
        setCenter(vboxTaOutput);

        VBox vboxPaneBottom = new VBox();
        VBox.setVgrow(paneBottom, Priority.ALWAYS);
        vboxPaneBottom.getChildren().addAll(new Label(MESSAGES.getString("border2")), paneBottom);
        setBottom(vboxPaneBottom);
        appearance();

        paneButtons.getButtons().forEach((btn) -> btn.setOnAction(this));
        cmbTreeType.setOnAction(this);
    }

    private void appearance() {
        paneButtons.setBackground(new Background(new BackgroundFill(Color.rgb(112, 162, 255)/* Blyškiai mėlyna */, CornerRadii.EMPTY, Insets.EMPTY)));
        paneParam1.setBackground(new Background(new BackgroundFill(Color.rgb(204, 255, 204)/* Šviesiai žalia */, CornerRadii.EMPTY, Insets.EMPTY)));
        paneParam1.getTfOfTable().get(2).setEditable(false);
        paneParam1.getTfOfTable().get(2).setStyle("-fx-text-fill: red");
        paneParam1.getTfOfTable().get(4).setEditable(false);
        paneParam1.getTfOfTable().get(4).setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        paneParam2.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 153)/* Gelsva */, CornerRadii.EMPTY, Insets.EMPTY)));
        paneBottom.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        taOutput.setFont(Font.font("Monospaced", 12));
        taOutput.setStyle("-fx-text-fill: black;");
        taOutput.setEditable(false);
    }

    @Override
    public void handle(ActionEvent ae) {
        try {
            System.gc();
            System.gc();
            System.gc();
            Region region = (Region) taOutput.lookup(".content");
            region.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

            Object source = ae.getSource();
            if (source instanceof Button) {
                handleButtons(source);
            } else if (source instanceof ComboBox && source.equals(cmbTreeType)) {
                disableButtons(true);
            }
        } catch (MyException e) {
            if (e.getCode() >= 0 && e.getCode() <= 3) {
                KsFX.ounerr(taOutput, errors[e.getCode()] + ": " + e.getMessage());
                if (e.getCode() == 2) {
                    paneParam1.getTfOfTable().get(0).setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    paneParam1.getTfOfTable().get(1).setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            } else if (e.getCode() == 4) {
                KsFX.ounerr(taOutput, MESSAGES.getString("msg3"));
            } else {
                KsFX.ounerr(taOutput, e.getMessage());
            }
        } catch (java.lang.UnsupportedOperationException e) {
            KsFX.ounerr(taOutput, e.getLocalizedMessage());
        } catch (Exception e) {
            KsFX.ounerr(taOutput, MESSAGES.getString("error5"));
            e.printStackTrace(System.out);
        }
    }

    private void handleButtons(Object source) throws MyException {
        if (source.equals(paneButtons.getButtons().get(0))) {
            treeGeneration(null);
        } else if (source.equals(paneButtons.getButtons().get(1))) {
            treeIteration();
        } else if (source.equals(paneButtons.getButtons().get(2))) {
            treeAdd();
        } else if (source.equals(paneButtons.getButtons().get(3))) {
            treeEfficiency();
        } else if (source.equals(paneButtons.getButtons().get(4))) {
            treeRemove();
        } else if (source.equals(paneButtons.getButtons().get(5))
                || source.equals(paneButtons.getButtons().get(6))) {
            KsFX.setFormatStartOfLine(true);
            KsFX.ounerr(taOutput, MESSAGES.getString("msg1"));
            KsFX.setFormatStartOfLine(false);
        }
    }

    protected abstract void treeGeneration(String filePath);

    protected abstract void treeAdd();

    protected abstract void treeRemove();

    protected abstract void treeIteration();

    protected abstract void treeEfficiency();

    protected abstract void createTree();

    protected void disableButtons(boolean disable) {
        for (int i : new int[]{1, 2, 4, 5, 6}) {
            if (i < paneButtons.getButtons().size() && paneButtons.getButtons().get(i) != null) {
                paneButtons.getButtons().get(i).setDisable(disable);
            }
        }
    }

    private void fileChooseMenu() throws MyException {
        FileChooser fc = new FileChooser();
        // Papildoma mūsų sukurtais filtrais
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("txt", "*.txt")
        );
        fc.setTitle((MESSAGES.getString("menuItem11")));
        fc.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            treeGeneration(file.getAbsolutePath());
        }
    }

    public static void createAndShowFXGUI(Stage stage, Lab3WindowFX implementation) {
        Locale.setDefault(Locale.US); // Suvienodiname skaičių formatus
        stage.setScene(new Scene(implementation, 1100, 650));
        stage.setTitle(MESSAGES.getString("title"));
        stage.getIcons().add(new Image("file:" + MESSAGES.getString("icon")));
        stage.show();
    }
}
