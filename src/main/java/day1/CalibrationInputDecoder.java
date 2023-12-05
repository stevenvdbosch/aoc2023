package day1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CalibrationInputDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(CalibrationInputDecoder.class);

    public void decode() {
        String fileName = "calibrationInput.txt";
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Path path = Paths.get(fileName);
        List<String> lines = getFileAsListOfLines(path);

        int sum = 0;
        for (String line : lines) {
            sum += findNumber(line);
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

    private Integer findNumber(String line) {
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
}
