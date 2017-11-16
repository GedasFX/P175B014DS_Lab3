package laborai.demo;

import laborai.gui.MyException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

public class AutoGamyba {

    private static Automobilis[] automobiliai;
    private static int pradinisIndeksas = 0, galinisIndeksas = 0;
    private static boolean arPradzia = true;

    public static Automobilis[] generuoti(int kiekis) {
        automobiliai = new Automobilis[kiekis];
        for (int i = 0; i < kiekis; i++) {
            automobiliai[i] = new Automobilis.Builder().buildRandom();
        }
        return automobiliai;
    }

    public static Automobilis[] generuotiIrIsmaisyti(int aibesDydis,
            double isbarstymoKoeficientas) throws MyException {
        return generuotiIrIsmaisyti(aibesDydis, aibesDydis, isbarstymoKoeficientas);
    }

    /**
     *
     * @param aibesDydis
     * @param aibesImtis
     * @param isbarstymoKoeficientas
     * @return Gražinamas aibesImtis ilgio masyvas
     * @throws MyException
     */
    public static Automobilis[] generuotiIrIsmaisyti(int aibesDydis,
            int aibesImtis, double isbarstymoKoeficientas) throws MyException {
        automobiliai = generuoti(aibesDydis);
        return ismaisyti(automobiliai, aibesImtis, isbarstymoKoeficientas);
    }

    // Galima paduoti masyvą išmaišymui iš išorės
    public static Automobilis[] ismaisyti(Automobilis[] autoBaze,
            int kiekis, double isbarstKoef) throws MyException {
        if (autoBaze == null) {
            throw new IllegalArgumentException("AutoBaze yra null");
        }
        if (kiekis <= 0) {
            throw new MyException(String.valueOf(kiekis), 1);
        }
        if (autoBaze.length < kiekis) {
            throw new MyException(autoBaze.length + " >= " + kiekis, 2);
        }
        if ((isbarstKoef < 0) || (isbarstKoef > 1)) {
            throw new MyException(String.valueOf(isbarstKoef), 3);
        }

        int likusiuKiekis = autoBaze.length - kiekis;
        int pradziosIndeksas = (int) (likusiuKiekis * isbarstKoef / 2);

        Automobilis[] pradineAutomobiliuImtis = Arrays.copyOfRange(autoBaze, pradziosIndeksas, pradziosIndeksas + kiekis);
        Automobilis[] likusiAutomobiliuImtis = Stream
                .concat(Arrays.stream(Arrays.copyOfRange(autoBaze, 0, pradziosIndeksas)),
                        Arrays.stream(Arrays.copyOfRange(autoBaze, pradziosIndeksas + kiekis, autoBaze.length)))
                .toArray(Automobilis[]::new);

        Collections.shuffle(Arrays.asList(pradineAutomobiliuImtis)
                .subList(0, (int) (pradineAutomobiliuImtis.length * isbarstKoef)));
        Collections.shuffle(Arrays.asList(likusiAutomobiliuImtis)
                .subList(0, (int) (likusiAutomobiliuImtis.length * isbarstKoef)));

        AutoGamyba.pradinisIndeksas = 0;
        galinisIndeksas = likusiAutomobiliuImtis.length - 1;
        AutoGamyba.automobiliai = likusiAutomobiliuImtis;
        return pradineAutomobiliuImtis;
    }

    public static Automobilis gautiIsBazes() throws MyException {
        if ((galinisIndeksas - pradinisIndeksas) < 0) {
            throw new MyException(String.valueOf(galinisIndeksas - pradinisIndeksas), 4);
        }
        // Vieną kartą Automobilis imamas iš masyvo pradžios, kitą kartą - iš galo.     
        arPradzia = !arPradzia;
        return automobiliai[arPradzia ? pradinisIndeksas++ : galinisIndeksas--];
    }
}
