package arr2d;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class Array2d {
    public static void main(String[] args) {
        testSudoku("sudokus.txt", "output.txt");
        System.out.println("isMagicSquare(new int[][]{{4, 9, 2},{3, 5 ,7},{8, 1, 6}}) = " + isMagicSquare(new int[][]{{4, 9, 2}, {3, 5, 7}, {8, 1, 6}}));
        printMonth(2024, 1);
        printYear(2024);
        printSudoku(sudokuFromString("...........5....9...4....1.2....3.5....7.....438...2......9.....1.4...6.........."));
    }

    public static boolean isMagicSquare(int[][] s) {
        int size = s.length;
        if (s.length==0){
            return false;
        }
        if (s[0].length != size || Arrays.stream(s).anyMatch(row -> row.length != size)) return false;
        int diag1 = 0;
        int diag2 = 0;
        for (int i = 0; i < s.length; i++) {
            diag1 += s[i][i];
            diag2 += s[i][s.length - 1 - i];
        }
        int[] rowSums = new int[size];
        int[] colSums = new int[size];

        boolean[] allowedNums = new boolean[s.length*s.length];
        Arrays.fill(allowedNums, false);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int num = s[i][j];
               try {
                   if (allowedNums[num-1]) return false;
               }catch (ArrayIndexOutOfBoundsException e){
                   return false;
               }
                allowedNums[num-1]=true;
                rowSums[i] += num;
                colSums[j] += num;
            }
        }

        int referenceSum = diag1;

        return Arrays.stream(rowSums).allMatch(sum -> sum == referenceSum) && Arrays.stream(colSums).allMatch(sum -> sum == referenceSum)&&diag2==diag1;
    }

    public static int[][] getCalendarOfMonth(int year, int month) {
        LocalDate first = LocalDate.of(year, month, 1);
        int firstInWeek = first.getDayOfWeek().getValue() - 1;
        int len = first.getMonth().length(first.isLeapYear());
        int[][] cal = new int[6][7];
        IntStream.range(firstInWeek, cal.length * cal[0].length).filter(i -> i < len + firstInWeek).forEach(i -> cal[i / 7][i % 7] = i + 1 - firstInWeek);
        return cal;
    }

    public static void printMonth(int year, int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        String headerFormat = "%" + ((21 - (Month.values()[date.getMonth().getValue() - 1].name().length() + 5)) / 2 + Month.values()[date.getMonth().getValue() - 1].name().length() + 5) + "s%n Mo Di Mi Do Fr Sa So%n";
        System.out.printf(headerFormat, Month.values()[date.getMonth().getValue() - 1].name() + " " + year);
        Arrays.stream(getCalendarOfMonth(year, month)).forEach(week -> {
            printWeek(week);
            System.out.println();
        });
    }

    private static void printWeek(int[] week) {
        Arrays.stream(week).mapToObj(day -> day != 0 ? String.format("%3d", day) : "   ").forEach(System.out::print);
    }

    public static void printYear(int year) {
        System.out.println(year);
        IntStream.range(0, 4).forEach(startmonth -> {
            IntStream.range(0, 8).forEach(i -> {
                IntStream.range(0, 3).forEach(month -> {
                    int[][] cal = getCalendarOfMonth(year, (startmonth * 3 + month) % 12 + 1);
                    if (i == 0) {
                        System.out.printf("%20s", Month.values()[(startmonth * 3 + month) % 12].name());
                    } else if (i == 1) {
                        System.out.print("Mo Di Mi Do Fr Sa So  ");
                    } else {
                        printWeek(cal[i - 2]);
                        System.out.print("  ");
                    }
                });
                System.out.println();
            });
            System.out.println();
        });
    }

    public static int[][] sudokuFromString(String s) {
        if (s.length() != 81) {
            throw new IllegalArgumentException(s.length() + " is a wrong length");
        }
        int[][] result = new int[9][9];
        IntStream.range(0, 81).forEach(i -> {
            if (!Character.isDigit(s.charAt(i)) && s.charAt(i) != '.') {
                throw new IllegalArgumentException(s.charAt(i) + " is not valid");
            }
            result[i / 9][i % 9] = s.charAt(i) == '.' ? 0 : Character.getNumericValue(s.charAt(i));
        });
        return result;
    }

    public static void printSudoku(int[][] sudoku) {
        StringBuilder s = new StringBuilder();
        IntStream.range(0, 9).forEach(i -> {
            IntStream.range(0, 9).forEach(j -> s.append(String.format("%-2s", sudoku[i][j] == 0 ? "." : sudoku[i][j])).append(j == 2 || j == 5 ? "| " : ""));
            s.append(i == 2 || i == 5 ? "\n------+-------+------\n" : "\n");
        });
        System.out.println(s);
    }

    public static int[] getLine(int[][] sudoku, int r) {
        return sudoku[r];
    }

    public static int[] getColumn(int[][] sudoku, int c) {
        return IntStream.range(0, 9).map(i -> sudoku[i][c]).toArray();
    }

    public static int[] getBox(int[][] sudoku, int b) {
        return IntStream.range(0, 3).flatMap(i -> IntStream.range(0, 3).map(j -> sudoku[(b / 3) * 3 + i][(b % 3) * 3 + j])).toArray();
    }

    public static int countNum(int num, int[] data) {
        return (int) Arrays.stream(data).filter(i -> i == num).count();
    }

    public static boolean isValid(int[] block) {
        return IntStream.rangeClosed(1, 9).noneMatch(i -> countNum(i, block) > 1);
    }

    public static void testSudoku(String in, String out) {
        try (FileOutputStream oFile = new FileOutputStream(out)) {
            Object[] sudokuObjects = Files.readAllLines(Path.of(in)).toArray();
            StringBuilder judged = new StringBuilder();
            for (Object obj : sudokuObjects) {
                String sudokuString = String.valueOf(obj);
                boolean isValid = isValidSudoku(sudokuFromString(sudokuString));
                judged.append(sudokuString).append(isValid ? ": gültig\n" : ": nicht gültig\n");
            }
            oFile.write(judged.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isValidSudoku(int[][] s) {
        return IntStream.range(0, 9).allMatch(i -> isValid(getBox(s, i)) && isValid(getColumn(s, i)) && isValid(getLine(s, i)));
    }
}
