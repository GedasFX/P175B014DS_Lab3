package laborai.demo;

import laborai.studijosktu.KTUable;
import laborai.studijosktu.Ks;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * @author EK
 */
public final class Automobilis implements KTUable<Automobilis> {

    // bendri duomenys visiems automobiliams (visai klasei)
    private static final int priimtinųMetųRiba = 1990;
    private static final int esamiMetai = LocalDate.now().getYear();
    private static final double minKaina = 100.0;
    private static final double maxKaina = 333000.0;
    private static final String idCode = "TA";   //  ***** nauja
    private static int serNr = 100;               //  ***** nauja
    private final String autoRegNr;
    private String markė = "";
    private String modelis = "";
    private int gamMetai = -1;
    private int rida = -1;
    private double kaina = -1.0;

    public Automobilis() {
        autoRegNr = idCode + (serNr++);    // suteikiamas originalus autoRegNr
    }

    public Automobilis(String markė, String modelis,
            int gamMetai, int rida, double kaina) {
        autoRegNr = idCode + (serNr++);    // suteikiamas originalus autoRegNr
        this.markė = markė;
        this.modelis = modelis;
        this.gamMetai = gamMetai;
        this.rida = rida;
        this.kaina = kaina;
        validate();
    }

    public Automobilis(String dataString) {
        autoRegNr = idCode + (serNr++);    // suteikiamas originalus autoRegNr
        this.parse(dataString);
    }

    public Automobilis(Builder builder) {
        autoRegNr = idCode + (serNr++);    // suteikiamas originalus autoRegNr
        this.markė = builder.markė;
        this.modelis = builder.modelis;
        this.gamMetai = builder.gamMetai;
        this.rida = builder.rida;
        this.kaina = builder.kaina;
        validate();
    }

    @Override
    public Automobilis create(String dataString) {
        return new Automobilis(dataString);
    }

    @Override
    public String validate() {
        String klaidosTipas = "";
        if (gamMetai < priimtinųMetųRiba || gamMetai > esamiMetai) {
            klaidosTipas = "Netinkami gamybos metai, turi būti ["
                    + priimtinųMetųRiba + ":" + esamiMetai + "]";
        }
        if (kaina < minKaina || kaina > maxKaina) {
            klaidosTipas += " Kaina už leistinų ribų [" + minKaina
                    + ":" + maxKaina + "]";
        }
        return klaidosTipas;
    }

    @Override
    public void parse(String dataString) {
        try {   // ed - tai elementarūs duomenys, atskirti tarpais
            Scanner ed = new Scanner(dataString);
            markė = ed.next();
            modelis = ed.next();
            gamMetai = ed.nextInt();
            setRida(ed.nextInt());
            setKaina(ed.nextDouble());
            validate();
        } catch (InputMismatchException e) {
            Ks.ern("Blogas duomenų formatas apie auto -> " + dataString);
        } catch (NoSuchElementException e) {
            Ks.ern("Trūksta duomenų apie auto -> " + dataString);
        }
    }

    @Override
    public String toString() {  // papildyta su autoRegNr
        return getAutoRegNr() + "=" + markė + "_" + modelis + ":" + gamMetai + " " + getRida() + " " + String.format("%4.1f", kaina);
    }

    public String getMarkė() {
        return markė;
    }

    public String getModelis() {
        return modelis;
    }

    public int getGamMetai() {
        return gamMetai;
    }

    public int getRida() {
        return rida;
    }

    public void setRida(int rida) {
        this.rida = rida;
    }

    public double getKaina() {
        return kaina;
    }

    public void setKaina(double kaina) {
        this.kaina = kaina;
    }

    public String getAutoRegNr() {  //** nauja.
        return autoRegNr;
    }

    @Override
    public int compareTo(Automobilis a) {
        return getAutoRegNr().compareTo(a.getAutoRegNr());
    }

    public static Comparator<Automobilis> pagalMarke = (Automobilis a1, Automobilis a2) -> a1.markė.compareTo(a2.markė); // pradžioje pagal markes, o po to pagal modelius

    public static Comparator<Automobilis> pagalKaina = (Automobilis a1, Automobilis a2) -> {
        // didėjanti tvarka, pradedant nuo mažiausios
        if (a1.kaina < a2.kaina) {
            return -1;
        }
        if (a1.kaina > a2.kaina) {
            return +1;
        }
        return 0;
    };

    public static Comparator<Automobilis> pagalMetusKainą = (Automobilis a1, Automobilis a2) -> {
        // metai mažėjančia tvarka, esant vienodiems lyginama kaina
        if (a1.gamMetai > a2.gamMetai) {
            return +1;
        }
        if (a1.gamMetai < a2.gamMetai) {
            return -1;
        }
        if (a1.kaina > a2.kaina) {
            return +1;
        }
        if (a1.kaina < a2.kaina) {
            return -1;
        }
        return 0;
    };

    // Automobilis klases objektų gamintojas
    public static class Builder {

        private final static Random RANDOM = new Random(1949);  // Atsitiktinių generatorius
        private final static String[][] MODELIAI = { // galimų automobilių markių ir jų modelių masyvas
            {"Mazda", "121", "323", "626", "MX6"},
            {"Ford", "Fiesta", "Escort", "Focus", "Sierra", "Mondeo"},
            {"Saab", "92", "96"},
            {"Honda", "Accord", "Civic", "Jazz"},
            {"Renault", "Laguna", "Megane", "Twingo", "Scenic"},
            {"Peugeot", "206", "207", "307"}
        };

        private String markė = "";
        private String modelis = "";
        private int gamMetai = -1;
        private int rida = -1;
        private double kaina = -1.0;

        public Automobilis build() {
            return new Automobilis(this);
        }

        public Automobilis buildRandom() {
            int ma = RANDOM.nextInt(MODELIAI.length);        // markės indeksas  0..
            int mo = RANDOM.nextInt(MODELIAI[ma].length - 1) + 1;// modelio indeksas 1..              
            return new Automobilis(MODELIAI[ma][0],
                    MODELIAI[ma][mo],
                    1990 + RANDOM.nextInt(20),// metai tarp 1990 ir 2009
                    6000 + RANDOM.nextInt(222000),// rida tarp 6000 ir 228000
                    800 + RANDOM.nextDouble() * 88000);// kaina tarp 800 ir 88800
        }

        public Builder gamMetai(int gamMetai) {
            this.gamMetai = gamMetai;
            return this;
        }

        public Builder markė(String markė) {
            this.markė = markė;
            return this;
        }

        public Builder modelis(String modelis) {
            this.modelis = modelis;
            return this;
        }

        public Builder rida(int rida) {
            this.rida = rida;
            return this;
        }

        public Builder kaina(double kaina) {
            this.kaina = kaina;
            return this;
        }
    }
}
