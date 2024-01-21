import java.util.Arrays;
import java.util.stream.IntStream;

public class Array2d {
    public static void main(String[] args) {
        System.out.println(isMagicSquare(new int[][]{{4,9,2},{3,5,7},{8,1,6}}));
    }

    public static boolean isMagicSquare(int[][] s) {
        int size = s.length;
        if (s[0].length != size || Arrays.stream(s).anyMatch(row -> row.length != size)) {
            return false;
        }
        int diag1 = 0;
        int diag2 = 0;
        for (int i = 0; i < s.length; i++) {
            diag1 += s[i][i];
            diag2 += s[i][s.length - 1 - i];
        }
        if (diag1 != diag2) {
            return false;
        }
        final int d1 =diag1;
        return Arrays.stream(s).allMatch(row -> Arrays.stream(row).sum() == d1) && IntStream.range(0, size).allMatch(i -> Arrays.stream(s).mapToInt(row -> row[i]).sum() == d1);
    }


} 