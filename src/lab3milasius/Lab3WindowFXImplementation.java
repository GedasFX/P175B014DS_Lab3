package lab3milasius;

import javafx.stage.Stage;
import laborai.demo.AutoGamyba;
import laborai.demo.Automobilis;
import laborai.demo.GreitaveikosTyrimas;
import laborai.gui.MyException;
import laborai.gui.fx.KsFX;
import laborai.gui.fx.Lab3WindowFX;
import laborai.studijosktu.AvlSetKTUx;
import laborai.studijosktu.BstSetKTUx;
import laborai.studijosktu.SortedSetADTx;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Lab3WindowFXImplementation extends Lab3WindowFX {
    private int sizeOfInitialSubSet;
    private int sizeOfGenSet;
    private int sizeOfLeftSubSet;

    private static SortedSetADTx<Automobilis> autoSet;

    public Lab3WindowFXImplementation(Stage stage) {
        super(stage);
    }

    @Override
    protected void treeGeneration(String filePath) throws MyException {
            // Nuskaitomi parametrai
            readTreeParameters();
            // Sukuriamas aibės objektas, priklausomai nuo medžio pasirinkimo
            // cmbTreeType objekte
            createTree();

            Automobilis[] autoArray;
            // Jei failas nenurodytas - generuojama
            if (filePath == null) {
                autoArray = AutoGamyba.generuotiIrIsmaisyti(sizeOfGenSet, sizeOfInitialSubSet, coef);
                paneParam1.getTfOfTable().get(2).setText(String.valueOf(sizeOfLeftSubSet));
            } else { // Skaitoma is failo
                autoSet.load(filePath);
                autoArray = new Automobilis[autoSet.size()];
                int i = 0;
                for (Object o : autoSet.toArray()) {
                    autoArray[i++] = (Automobilis) o;
                }
                // Skaitant iš failo išmaišoma standartiniu Collections.shuffle metodu.
                Collections.shuffle(Arrays.asList(autoArray), new Random());
            }

            // Išmaišyto masyvo elementai surašomi i aibę
            autoSet.clear();
            for (Automobilis a : autoArray) {
                autoSet.add(a);
            }
            // Išvedami rezultatai
            // Nustatoma, kad eilutės pradžioje neskaičiuotų išvedamų eilučių skaičiaus
            KsFX.setFormatStartOfLine(true);
            KsFX.oun(taOutput, autoSet.toVisualizedString(tfDelimiter.getText()),
                    MESSAGES.getString("msg5"));
            // Nustatoma, kad eilutės pradžioje skaičiuotų išvedamų eilučių skaičių
            KsFX.setFormatStartOfLine(false);
            disableButtons(false);
        }

    @Override
    protected void treeAdd() throws MyException {
        KsFX.setFormatStartOfLine(true);
        Automobilis auto = AutoGamyba.gautiIsBazes();
        autoSet.add(auto);
        paneParam1.getTfOfTable().get(2).setText(String.valueOf(--sizeOfLeftSubSet));
        KsFX.oun(taOutput, auto, MESSAGES.getString("msg7"));
        KsFX.oun(taOutput, autoSet.toVisualizedString(tfDelimiter.getText()));
        KsFX.setFormatStartOfLine(false);
    }

    @Override
    protected void treeRemove() {
        KsFX.setFormatStartOfLine(true);
        if (autoSet.isEmpty()) {
            KsFX.ounerr(taOutput, MESSAGES.getString("msg4"));
            KsFX.oun(taOutput, autoSet.toVisualizedString(tfDelimiter.getText()));
        } else {
            int nr = new Random().nextInt(autoSet.size());
            Automobilis auto = (Automobilis) autoSet.toArray()[nr];
            autoSet.remove(auto);
            KsFX.oun(taOutput, auto, MESSAGES.getString("msg6"));
            KsFX.oun(taOutput, autoSet.toVisualizedString(tfDelimiter.getText()));
        }
        KsFX.setFormatStartOfLine(false);
    }

    @Override
    protected void treeIteration() {
        KsFX.setFormatStartOfLine(true);
        if (autoSet.isEmpty()) {
            KsFX.ounerr(taOutput, MESSAGES.getString("msg4"));
        } else {
            KsFX.oun(taOutput, autoSet, MESSAGES.getString("msg8"));
        }
        KsFX.setFormatStartOfLine(false);
    }

    @Override
    protected void treeEfficiency() {
        KsFX.setFormatStartOfLine(true);
        KsFX.oun(taOutput, "", MESSAGES.getString("msg2"));
        paneBottom.setDisable(true);
        menuFX.setDisable(true);

        GreitaveikosTyrimas gt = new GreitaveikosTyrimas();

        // Si gija paima rezultatus is greitaveikos tyrimo gijos ir isveda
        // juos i taOutput. Gija baigia darbą kai gaunama FINISH_COMMAND
        new Thread(() -> {
            try {
                String result;
                while (!(result = gt.getResultsLogger().take())
                        .equals(GreitaveikosTyrimas.FINISH_COMMAND)) {
                    KsFX.ou(taOutput, result);
                    gt.getSemaphore().release();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            gt.getSemaphore().release();
            paneBottom.setDisable(false);
            menuFX.setDisable(false);
        }, "Greitaveikos_rezultatu_gija").start();

        //Sioje gijoje atliekamas greitaveikos tyrimas
        new Thread(() -> gt.pradetiTyrima(), "Greitaveikos_tyrimo_gija").start();
    }

    @Override
    protected void createTree() throws MyException {
        switch (cmbTreeType.getSelectionModel().getSelectedIndex()) {
            case 0:
                autoSet = new BstSetKTUx(new Automobilis());
                break;
            case 1:
                autoSet = new AvlSetKTUx(new Automobilis());
                break;
            default:
                disableButtons(true);
                throw new MyException(MESSAGES.getString("msg1"));
        }
    }

    private void readTreeParameters() throws MyException {
        // Truputėlis kosmetikos..
        for (int i = 0; i < 2; i++) {
            paneParam1.getTfOfTable().get(i).
                    setStyle("-fx-control-inner-background: white; ");
            paneParam1.getTfOfTable().get(i).applyCss();
        }
        // Nuskaitomos parametrų reiksmės. Jei konvertuojant is String
        // įvyksta klaida, sugeneruojama NumberFormatException situacija. Tam, kad
        // atskirti kuriame JTextfield'e ivyko klaida, panaudojama nuosava
        // situacija MyException
        int i = 0;
        try {
            // Pakeitimas (replace) tam, kad sukelti situaciją esant
            // neigiamam skaičiui
            sizeOfGenSet = Integer.valueOf(paneParam1.getParametersOfTable().get(i).replace("-", "x"));
            sizeOfInitialSubSet = Integer.valueOf(paneParam1.getParametersOfTable().get(++i).replace("-", "x"));
            sizeOfLeftSubSet = sizeOfGenSet - sizeOfInitialSubSet;
            ++i;
            coef = Double.valueOf(paneParam1.getParametersOfTable().get(++i).replace("-", "x"));
        } catch (NumberFormatException e) {
            // Galima ir taip: pagauti exception'ą ir vėl mesti
            throw new MyException(paneParam1.getParametersOfTable().get(i), e, i);
        }
    }
}
