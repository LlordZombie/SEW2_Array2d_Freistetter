package arr2d;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class Array2d {
    public static void main(String[] args) {
        testSudoku("sudokus.txt","output.txt");
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
        final int d1 = diag1;
        return Arrays.stream(s).allMatch(row -> Arrays.stream(row).sum() == d1) && IntStream.range(0, size).allMatch(i -> Arrays.stream(s).mapToInt(row -> row[i]).sum() == d1);
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
        String headerFormat = "%" + ((21 - (Month.values()[date.getMonth().getValue()].name().length() + 5)) / 2 + Month.values()[date.getMonth().getValue()].name().length() + 5) + "s%n Mo Di Mi Do Fr Sa So%n";
        System.out.printf(headerFormat, Month.values()[date.getMonth().getValue()].name() + " " + year);
        Arrays.stream(getCalendarOfMonth(year, month)).forEach(week -> {
            printWeek(week);
            System.out.println();
        });
    }

    private static void printWeek(int[] week) {
        Arrays.stream(week).mapToObj(day -> day != 0 ? String.format("%3d", day) : "   ").forEach(System.out::print);
    }

    public static void printYear(int year) {
        AtomicReference<String> format = new AtomicReference<>("%" + ((65 - String.valueOf(year).length()) / 2 + String.valueOf(year).length()) + "d");
        System.out.printf(format.get(), year);
        LocalDate[][] months = IntStream.range(0, 4).mapToObj(i -> IntStream.range(0, 3).mapToObj(j -> LocalDate.of(year, (i * 3 + j) % 12 + 1, 1)).toArray(LocalDate[]::new)).toArray(LocalDate[][]::new);
        IntStream.range(0, 4).forEach(i -> {
            System.out.println();
            IntStream.range(0, 3).forEach(j -> {
                int currentLen = (22 - Month.values()[months[i][j].getMonthValue() - 1].name().length()) / 2 + Month.values()[months[i][j].getMonthValue() - 1].name().length();
                format.set("%" + currentLen + "s%" + (22 - currentLen) + "s");
                System.out.printf(format.get(), Month.values()[months[i][j].getMonthValue() - 1].name(), " ");
            });
            System.out.println();
            System.out.println(" Mo Di Mi Do Fr Sa So  Mo Di Mi Do Fr Sa So  Mo Di Mi Do Fr Sa So");
            IntStream.range(0, 6).forEach(j -> {
                IntStream.range(0, 3).forEach(k -> {
                    printWeek(getCalendarOfMonth(months[i][k].getYear(), months[i][k].getMonthValue())[j]);
                    System.out.print(" ");
                });
                System.out.println();
            });
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
        File o = new File(out);
        try {
            o.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (FileOutputStream oFile = new FileOutputStream(o)) {
            Object[] sudokuObjects = Files.readAllLines(Path.of(in)).toArray();
            String[] sudokus = new String[sudokuObjects.length];
            StringBuilder judged = new StringBuilder();
            for (int i = 0; i < sudokus.length; i++) {
                if (isValidSudoku(sudokuFromString(String.valueOf(sudokuObjects[i])))) {
                    judged.append(sudokuObjects[i]).append(": gültig\n");
                } else {
                    judged.append(sudokuObjects[i]).append(": nicht gültig\n");
                }
            }
            oFile.write(judged.toString().getBytes());

        } catch (IOException e) {
            throw new IllegalArgumentException("Path is not valid");
        }

    }

    public static boolean isValidSudoku(int[][] s) {
        return IntStream.range(0, 9).allMatch(i -> isValid(getBox(s, i)) && isValid(getColumn(s, i)) && isValid(getLine(s, i)));
    }
}
