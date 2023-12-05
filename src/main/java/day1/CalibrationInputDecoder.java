package day1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CalibrationInputDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(CalibrationInputDecoder.class);

    public void decodePart1() {
        String fileName = "C:\\Projects\\adventofcode\\aoc2023\\src\\main\\resources\\day1\\calibrationInput.txt";
//        String fileName = "calibrationInput.txt";

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Path path = Paths.get(fileName);
        List<String> lines = getFileAsListOfLines(path);

        int sum = 0;
        for (String line : lines) {
            sum += findNumberPart1(line);
        }
        System.out.println(sum);
    }

    public void decodePart2() {
        String fileName = "C:\\Projects\\adventofcode\\aoc2023\\src\\main\\resources\\day1\\calibrationInput.txt";
//        String fileName = "calibrationInput.txt";

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Path path = Paths.get(fileName);
        List<String> lines = getFileAsListOfLines(path);

        // Test first with single line
//        List<String> lines = new ArrayList<>();
//        lines.add("one7nineqnjsgcjnjmdhdrxbthree");

        int sum = 0;
        for (String line : lines) {
            sum += findNumberPart2(line);
        }
        System.out.println(sum);
    }

    private List<String> getFileAsListOfLines(Path path) {
        try (Stream<String> lines = Files.lines(path)) {
            return lines.collect(toList());
        }
        catch (IOException e) {
            LOG.error("Couldn't read file {}", path);
            throw new RuntimeException(e);
        }
    }

    private Integer findNumberPart1(String line) {
        List<Character> chars = line.chars()
            .mapToObj(e->(char)e).collect(toList());

        char firstDigit = '0', lastDigit = '0';

        for (char c : chars) {
            if (Character.isDigit(c)) {
                firstDigit = c;
                break;
            }
        }

        Collections.reverse(chars);
        for (char c : chars) {
            if (Character.isDigit(c)) {
                lastDigit = c;
                break;
            }
        }
        String numberAsString = "" + firstDigit + lastDigit;
        return Integer.valueOf(numberAsString);
    }

    private Integer findNumberPart2(String line) {

        char firstDigit = '0', lastDigit = '0';

        //FIND FIRST DIGIT
        var charArray = line.toCharArray();
        for (var i = 0; i < charArray.length; i++) {
            if (Character.isDigit(charArray[i])) {
                firstDigit = charArray[i];
                break;
            }
            var c = checkForDigitAsText(line.substring(i));
            if (c.isPresent()) {
                firstDigit = c.get();
                break;
            }
        }
        //FIND LAST DIGIT

        for (int i = charArray.length -1; i >= 0; i--) {
            if (Character.isDigit(charArray[i])) {
                lastDigit = charArray[i];
                break;
            }
            var c = checkForDigitAsLastText(line.substring(0, i+1));
            if (c.isPresent()) {
                lastDigit = c.get();
                break;
            }
        }

        String numberAsString = "" + firstDigit + lastDigit;
        return Integer.valueOf(numberAsString);
    }

    private Optional<Character> checkForDigitAsLastText(String subLine) {
        String[] languageInput = new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};

        for (int j = 0; j < languageInput.length; j++) {
            var input = languageInput[j];
            if (input.length() <= subLine.length()) {
                if (input.equals(subLine.substring(subLine.length() - input.length()))) {
                    return Optional.of(Character.forDigit(j + 1, 10));
                }
            }
        }
        return Optional.empty();
    }

    private Optional<Character> checkForDigitAsText(String subLine) {
        String[] languageInput = new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};

        for (int j = 0; j < languageInput.length; j++) {
            var input = languageInput[j];
            if (input.length() <= subLine.length()) {
                if (input.equals(subLine.substring(0, input.length()))) {
                    return Optional.of(Character.forDigit(j + 1, 10));
                }
            }
        }
        return Optional.empty();
    }
}
