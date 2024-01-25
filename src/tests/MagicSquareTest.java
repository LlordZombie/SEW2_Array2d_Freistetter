package tests;

import arr2d.Array2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;

class MagicSquareTest {
    private static int bigMagicSqare[][];

    @BeforeAll
    static void generateBigMagicSquare() {
        System.out.println("Erzeugen eine wirklich riesigen Magischen Quadrats: 10_001 x 10_001.\n" +
                "Das braucht wirklich lang!");
        long t0 = System.currentTimeMillis();
        bigMagicSqare=generateMagicSquare(10_001, false);
        long t1 = System.currentTimeMillis();
        System.out.println("Fertig mit dem Erzeugen nach " + (t1-t0) + "ms");
    }

    /**
     * Erzeugt ein magisches Quadrat.
     * Die Kantenlänge muss ungerade sein!
     *
     * Sourcecode-Quelle:https://stackoverflow.com/questions/36040292/creating-a-magic-square-in-java
     *
     * @param s Kantenlänge
     * @param printMagicSquare gibt an, ob das Magische Quadrat auch ausgeruckt werden soll.
     * @return das Magische Quadrat
     * @throws IllegalArgumentException falls die Kantenlänge nicht ungerade und >= 1 ist.
     */
    public static int[][] generateMagicSquare(int s, boolean printMagicSquare) throws IllegalArgumentException {
        if (s % 2 == 0) {
            throw new IllegalArgumentException(
                    "Die Kantenlänge muss für diesen Algorithmus ungerade sein, war aber: " + s);
        }
        if (s <= 0) {
            throw new IllegalArgumentException("Die Kantenlänge muss größer gleich 1 sein, war aber: " + s);
        }
        int magicSqr[][] = new int[s][s];

        int r = s / 2;
        int c = s - 1;

        for (int no = 1; no <= s * s;) {
            if (r == -1 && c == s) {
                c = s - 2;
                r = 0;
            } else {
                if (c == s) {
                    c = 0;
                }

                if (r < 0) {
                    r = s - 1;
                }
            }

            if (magicSqr[r][c] != 0) {
                c = c - 2;
                r = r + 1;
                continue;
            } else {
                magicSqr[r][c] = no;
                no = no + 1;
            }

            c = c + 1;
            r = r - 1;
        }

        if (printMagicSquare) {
            System.out.println("The Magic Square for " + s + ": \n");
            System.out.println("Sum of each column or row " + s * (s * s + 1) / 2 + ": \n");

            for (r = 0; r < s; r++) {
                System.out.println(Arrays.toString(
                        magicSqr[r]).replace('[', '{').replace(']', '}') + ',');
            }
        }

        return magicSqr;
    }

    @Test
    void testMagicSquare00() {
        String msg;
        int[][] magic = {{4, 9, 2}, {3, 5, 7}, {8, 1, 6}};

        msg = "Testet die Methode isMagic() auf richtiges Ergebnis";
        Assertions.assertTrue(Array2d.isMagicSquare(magic), msg);
    }

    @Test
    void testMagicSquare01() {
        String msg;
        int[][] noMagic = {{0, 9, 2}, {3, 5, 7}, {8, 1, 6}};

        msg = "Testet die Methode isMagic() auf richtiges Ergebnis."
                + " Hier passt die Berechnung nicht.";
        Assertions.assertFalse(Array2d.isMagicSquare(noMagic), msg);
    }

    @Test
    void testMagicSquare02() {
        String msg;
        int[][] noSquare = {{0, 9, 2}, {3, 5, 7}};

        msg = "Testet die Methode isMagic() auf richtiges Ergebnis."
                + " Hier wird überprüft, ob die Methode testet, ob das"
                + " 2D-Array quadratisch ist.";
        Assertions.assertFalse(Array2d.isMagicSquare(noSquare), msg);
    }

    @Test
    void testMagicSquare03() {
        String msg;
        int[][] magicFour = {
            {16, 3, 2, 13},
            {5, 10, 11, 8},
            {9, 6, 7, 12},
            {4, 15, 14, 1}
        };
        msg = "Testet die Methode isMagic() auf richtiges Ergebnis.";
        Assertions.assertTrue(Array2d.isMagicSquare(magicFour), msg);
    }

    @Test
    void testMagicSquare04() {
        String msg;
        int[][] noMagicFour = {
            {16, 3, 2, 13},
            {5, 10, 11, 8},
            {9, 6, 7, 12},
            {4, 15, 14}
        };
        msg = "Testet die Methode isMagic() auf richtiges Ergebnis.";
        Assertions.assertFalse(Array2d.isMagicSquare(noMagicFour), msg);
    }

    @Test
    void testMagicSquare05() {
        String msg;
        int[][] magicSeven = {
            {30, 39, 48, 1, 10, 19, 28},
            {38, 47, 7, 9, 18, 27, 29},
            {46, 6, 8, 17, 26, 35, 37},
            {5, 14, 16, 25, 34, 36, 45},
            {13, 15, 24, 33, 42, 44, 4},
            {21, 23, 32, 41, 43, 3, 12},
            {22, 31, 40, 49, 2, 11, 20},
        };
        msg = "Testet die Methode isMagic() auf richtiges Ergebnis.";
        Assertions.assertTrue(Array2d.isMagicSquare(magicSeven), msg);
    }

    @Test
    void testMagicSquare06() {
        String msg;
        int[][] notMagicSeven = {
            {30, 39, 48, 1, 10, 19, 28},
            {38, 47, 7, 9, 18, 27, 29},
            {46, 6, 8, 17, 26, 35, 37},
            {5, 14, 16, 50, 34, 36, 45},
            {13, 15, 24, 33, 42, 44, 4},
            {21, 23, 32, 41, 43, 3, 12},
            {22, 31, 40, 49, 2, 11, 20},
        };
        msg = "Testet die Methode isMagic() auf richtiges Ergebnis.";
        Assertions.assertFalse(Array2d.isMagicSquare(notMagicSeven), msg);
    }

    @Test
    void testMagicSquare07() {
        int[][] notMagic = {{5, 10, 3}, {4, 6, 8}, {9, 2, 7}};

        String msg = "Das Quadrat hat zwar überall die gleichen Summen, jedoch die Zahlen von 2-10";
        Assertions.assertFalse(Array2d.isMagicSquare(notMagic), msg);
    }

    @Test
    void testMagicSquare08() {
        int[][] notMagic = {};

        String msg = "Ein leeres zweidimensionales Array ist kein Magisches Quadrat, da es keine Zahlen enthält.";
        Assertions.assertFalse(Array2d.isMagicSquare(notMagic), msg);
    }

    @Test
    void testMagicSquare09() {
        int[][] isMagic = {{1}};

        String msg = "Das ist das kleinste magische Quadrat, Dimension 1x1, Inhalt 1";
        Assertions.assertTrue(Array2d.isMagicSquare(isMagic), msg);
    }

    @Test
    void testMagicSquare10() {
        int[][] notMagic = {{99}};

        String msg = "Da das Quadrat keinen Einser enthält, ist es nicht magisch";
        Assertions.assertFalse(Array2d.isMagicSquare(notMagic), msg);
    }

    @Test
    void testMagicSquare11() {
        String msg;
        int[][] notMagic = {{3, 5, 7}, {4, 9, 2}, {8, 1, 6}};

        msg = "Die Zeilen- und Spaltensummen sind ok, aber die Diagonalen-Summen sind falsch";
        Assertions.assertFalse(Array2d.isMagicSquare(notMagic), msg);
    }

    @Test
    @Timeout(5)
    void testMagicSquare12_TimeOut() {
        String msg = "Da das Quadrat hat zwar die Dimension 10.000 x 10.000, ist aber magisch, " +
                "der Test darf aber nicht länger als 2 Sekunden dauern.";
        Assertions.assertTrue(Array2d.isMagicSquare(bigMagicSqare), msg);
    }
}
