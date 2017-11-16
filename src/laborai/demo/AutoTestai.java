package laborai.demo;

import laborai.studijosktu.Ks;
import laborai.studijosktu.AvlSetKTUx;
import laborai.studijosktu.SortedSetADTx;
import laborai.studijosktu.SetADT;
import laborai.studijosktu.BstSetKTUx;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

/*
 * Aibės testavimas be Swing'o
 *
 */
public class AutoTestai {

    static Automobilis[] autoBaze;
    static SortedSetADTx<Automobilis> aSerija = new BstSetKTUx(new Automobilis(), Automobilis.pagalKaina);

    public static void main(String[] args) throws CloneNotSupportedException {
        Locale.setDefault(Locale.US); // Suvienodiname skaičių formatus
        aibėsTestas();
    }

    static SortedSetADTx generuotiAibe(int kiekis, int generN) {
        autoBaze = new Automobilis[generN];
        for (int i = 0; i < generN; i++) {
            autoBaze[i] = new Automobilis.Builder().buildRandom();
        }
        Collections.shuffle(Arrays.asList(autoBaze));
        aSerija.clear();
        for (int i = 0; i < kiekis; i++) {
            aSerija.add(autoBaze[i]);
        }
        return aSerija;
    }

    public static void aibėsTestas() throws CloneNotSupportedException {
        Automobilis a1 = new Automobilis("Renault", "Laguna", 1997, 50000, 1700);
        Automobilis a2 = new Automobilis.Builder()
                .markė("Renault")
                .modelis("Megane")
                .gamMetai(2001)
                .rida(20000)
                .kaina(3500)
                .build();
        Automobilis a3 = new Automobilis.Builder().buildRandom();
        Automobilis a4 = new Automobilis("Renault Laguna 2001 115900 700");
        Automobilis a5 = new Automobilis("Renault Megane 1946 365100 9500");
        Automobilis a6 = new Automobilis("Honda   Civic  2001  36400 80.3");
        Automobilis a7 = new Automobilis("Renault Laguna 2001 115900 7500");
        Automobilis a8 = new Automobilis("Renault Megane 1946 365100 950");
        Automobilis a9 = new Automobilis("Honda   Civic  2007  36400 850.3");

        Automobilis[] autoMasyvas = {a9, a7, a8, a5, a1, a6};

        Ks.oun("Auto Aibė:");
        SortedSetADTx<Automobilis> autoAibe = new BstSetKTUx(new Automobilis());

        for (Automobilis a : autoMasyvas) {
            autoAibe.add(a);
            Ks.oun("Aibė papildoma: " + a + ". Jos dydis: " + autoAibe.size());
        }
        Ks.oun("");
        Ks.oun(autoAibe.toVisualizedString(""));

        SortedSetADTx<Automobilis> autoAibeKopija
                = (SortedSetADTx<Automobilis>) autoAibe.clone();

        autoAibeKopija.add(a2);
        autoAibeKopija.add(a3);
        autoAibeKopija.add(a4);
        Ks.oun("Papildyta autoaibės kopija:");
        Ks.oun(autoAibeKopija.toVisualizedString(""));

        a9.setRida(10000);

        Ks.oun("Originalas:");
        Ks.ounn(autoAibe.toVisualizedString(""));

        Ks.oun("Ar elementai egzistuoja aibėje?");
        for (Automobilis a : autoMasyvas) {
            Ks.oun(a + ": " + autoAibe.contains(a));
        }
        Ks.oun(a2 + ": " + autoAibe.contains(a2));
        Ks.oun(a3 + ": " + autoAibe.contains(a3));
        Ks.oun(a4 + ": " + autoAibe.contains(a4));
        Ks.oun("");

        Ks.oun("Ar elementai egzistuoja aibės kopijoje?");
        for (Automobilis a : autoMasyvas) {
            Ks.oun(a + ": " + autoAibeKopija.contains(a));
        }
        Ks.oun(a2 + ": " + autoAibeKopija.contains(a2));
        Ks.oun(a3 + ": " + autoAibeKopija.contains(a3));
        Ks.oun(a4 + ": " + autoAibeKopija.contains(a4));
        Ks.oun("");

        Ks.oun("Elementų šalinimas iš kopijos. Aibės dydis prieš šalinimą:  " + autoAibeKopija.size());
        for (Automobilis a : new Automobilis[]{a2, a1, a9, a8, a5, a3, a4, a2, a7, a6, a7, a9}) {
            autoAibeKopija.remove(a);
            Ks.oun("Iš autoaibės kopijos pašalinama: " + a + ". Jos dydis: " + autoAibeKopija.size());
        }
        Ks.oun("");

        Ks.oun("Automobilių aibė su iteratoriumi:");
        Ks.oun("");
        for (Automobilis a : autoAibe) {
            Ks.oun(a);
        }
        Ks.oun("");
        Ks.oun("Automobilių aibė AVL-medyje:");
        SortedSetADTx<Automobilis> autoAibe3 = new AvlSetKTUx(new Automobilis());
        for (Automobilis a : autoMasyvas) {
            autoAibe3.add(a);
        }
        Ks.ounn(autoAibe3.toVisualizedString(""));

        Ks.oun("Automobilių aibė su iteratoriumi:");
        Ks.oun("");
        for (Automobilis a : autoAibe3) {
            Ks.oun(a);
        }

        Ks.oun("");
        Ks.oun("Automobilių aibė su atvirkštiniu iteratoriumi:");
        Ks.oun("");
        Iterator iter = autoAibe3.descendingIterator();
        while (iter.hasNext()) {
            Ks.oun(iter.next());
        }

        Ks.oun("");
        Ks.oun("Automobilių aibės toString() metodas:");
        Ks.ounn(autoAibe3);

        // Išvalome ir suformuojame aibes skaitydami iš failo
        autoAibe.clear();
        autoAibe3.clear();

        Ks.oun("");
        Ks.oun("Automobilių aibė DP-medyje:");
        autoAibe.load("Duomenys\\ban.txt");
        Ks.ounn(autoAibe.toVisualizedString(""));
        Ks.oun("Išsiaiškinkite, kodėl medis augo tik į vieną pusę.");

        Ks.oun("");
        Ks.oun("Automobilių aibė AVL-medyje:");
        autoAibe3.load("Duomenys\\ban.txt");
        Ks.ounn(autoAibe3.toVisualizedString(""));

        SetADT<String> autoAibe4 = AutoApskaita.automobiliuMarkes(autoMasyvas);
        Ks.oun("Pasikartojančios automobilių markės:\n" + autoAibe4.toString());
    }
}
