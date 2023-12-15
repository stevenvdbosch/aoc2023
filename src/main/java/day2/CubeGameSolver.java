package day2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CubeGameSolver {

    private static final Logger LOG = LoggerFactory.getLogger(CubeGameSolver.class);

    public static final int MAX_GREEN_CUBES = 13;
    public static final int MAX_BLUE_CUBES = 14;
    public static final int MAX_RED_CUBES = 12;

    public void solvePossibleGames() {
        List<String> lines = getInputFromFile();
//        List<String> lines = getSampleInput();

        List<Game> games = parseGamesInputs(lines);
        int totalSumOfGameIds = sumGoodGameIds(games);
    }

    public void solvePowerOfGames() {
        List<String> lines = getInputFromFile();
//        List<String> lines = getSampleInput();

        List<Game> games = parseGamesInputs(lines);
        int totalPower = sumPowerOfMinimal(games);
    }

    public int sumPowerOfMinimal(List<Game> games) {
        int powerOfGames = 0;
        for (Game game : games) {
            int minGreen = 0, minRed = 0, minBlue = 0;

            for (ComboCubeReveal reveal : game.comboCubeReveals) {
                int totalGreen = 0, totalRed = 0,totalBlue = 0;
                for (CubeReveal cubeReveal : reveal.cubeReveals) {

                    switch(cubeReveal.color) {
                        case RED -> totalRed += cubeReveal.amount;
                        case BLUE -> totalBlue += cubeReveal.amount;
                        case GREEN -> totalGreen += cubeReveal.amount;
                    }
                }
//                System.out.println(": " + totalRed + " " + totalBlue + " " + totalGreen);

                if (totalRed > minRed) {
                    minRed = totalRed;
                }
                if (totalGreen > minGreen) {
                    minGreen = totalGreen;
                }
                if (totalBlue > minBlue) {
                    minBlue = totalBlue;
                }
            }
            System.out.println("game " + game.number + ": " + minBlue + " " + minGreen + " " + minRed);

            int gamePower = minBlue * minGreen * minRed;
            powerOfGames +=gamePower;
        }

        System.out.println("The total power of games: " + powerOfGames);
        return powerOfGames;
    }

    private int sumGoodGameIds(List<Game> games) {
        int totalGood = 0;
        for (Game game : games) {
            boolean gameGood = true;


            for (ComboCubeReveal reveal : game.comboCubeReveals) {
                int totalGreen = 0, totalRed = 0,totalBlue = 0;
                for (CubeReveal cubeReveal : reveal.cubeReveals) {

                    switch(cubeReveal.color) {
                        case RED -> totalRed += cubeReveal.amount;
                        case BLUE -> totalBlue += cubeReveal.amount;
                        case GREEN -> totalGreen += cubeReveal.amount;
                    }
                }
//                System.out.println("totalGreen= "+ totalGreen);
//                System.out.println("totalBlue= "+ totalBlue);
//                System.out.println("totalRed= "+ totalRed);
                if (totalGreen > MAX_GREEN_CUBES || totalBlue > MAX_BLUE_CUBES || totalRed > MAX_RED_CUBES) {
                    gameGood = false;
                    break;
                }
            }
            System.out.println("Game " + game.number + ": " + gameGood);
            if (gameGood) {
                totalGood += game.number;
            }
        }

        System.out.println("The total sum of good games: " + totalGood);
        return totalGood;
    }

    private List<Game> parseGamesInputs(List<String> lines) {
        List<Game> games = new ArrayList<>();

        //for each game
        for (String line : lines) {
            //get  game number
            var indexOfGameSeparator = line.indexOf(':');
            var gameString = line.substring(5, indexOfGameSeparator);
            var gameNumber = Integer.parseInt(gameString);

            //break down line into combo cube reveals
            var comboCubeRevealListString = line.substring(indexOfGameSeparator+1);
            String[] splitted = comboCubeRevealListString.strip().split(";");

            // For each comboCubeReveal
            List<ComboCubeReveal> combos = new ArrayList<>();
            for (String comboCubeRevealString : splitted) {
                var cubeRevealListString = comboCubeRevealString.strip().split(",");

                List<CubeReveal> comboCubeReveal = new ArrayList<>();
                //For each CubeReveal
                for (String cubeRevealString : cubeRevealListString) {
//                    System.out.println("cubeRevealString = " + cubeRevealString);
                    var cubeRevealParts = cubeRevealString.strip().split(" ");
                    var amount = Integer.parseInt(cubeRevealParts[0]);
                    var color = cubeRevealParts[1];
                    var reveal = new CubeReveal(amount, CubeColor.valueOf(color.toUpperCase(Locale.ROOT)));
                    comboCubeReveal.add(reveal);
//                    System.out.println("reveal = " + reveal);
                }

                combos.add(new ComboCubeReveal(comboCubeReveal));
            }
            var newGame = new Game(gameNumber, combos);
            games.add(newGame);
        }
        return games;
    }

    private List<String> getInputFromFile() {
        String fileName = "C:\\Projects\\adventofcode\\aoc2023\\src\\main\\resources\\day2\\cubeInput.txt";

//        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Path path = Paths.get(fileName);
        List<String> lines = getFileAsListOfLines(path);
        return lines;
    }

    private List<String> getSampleInput() {
        List<String> lines = new ArrayList<>();
//        var input = "Game 1: 4 blue, 4 red, 16 green; 14 green, 5 red; 1 blue, 3 red, 5 green";
        var input = "Game 2: 3 green, 8 red, 1 blue; 5 green, 6 blue; 4 green, 4 blue, 10 red; 2 green, 6 red, 4 " +
                    "blue; 8 red, 11 blue, 4 green; 10 red, 10 blue";
        lines.add(input);
        return lines;
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

    public record CubeReveal(int amount, CubeColor color){}

    public record ComboCubeReveal(List<CubeReveal> cubeReveals){}

    public record Game(int number, List<ComboCubeReveal> comboCubeReveals){}

}
