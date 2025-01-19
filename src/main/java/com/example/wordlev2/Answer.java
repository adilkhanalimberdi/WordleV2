package com.example.wordlev2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Answer {
    private static ArrayList <String> threeLetterWords;
    private static ArrayList <String> fourLetterWords;
    private static ArrayList <String> fiveLetterWords;

    static void init() {
        threeLetterWords = new ArrayList<>();
        try {
            FileReader file = new FileReader("src/main/java/com/example/wordlev2/answers/ThreeLetterWords.txt");
            getData(threeLetterWords, file);
        } catch (FileNotFoundException e) {
            System.out.println("File for 3 letter words not found.");
        }

        fourLetterWords = new ArrayList<>();
        try {
            FileReader file = new FileReader("src/main/java/com/example/wordlev2/answers/FourLetterWords.txt");
            getData(fourLetterWords, file);
        } catch (FileNotFoundException e) {
            System.out.println("File for 4 letter words not found.");
        }

        fiveLetterWords = new ArrayList<>();
        try {
            FileReader file = new FileReader("src/main/java/com/example/wordlev2/answers/FiveLetterWords.txt");
            getData(fiveLetterWords, file);
        } catch (FileNotFoundException e) {
            System.out.println("File for 5 letter words not found.");
        }
    }

    private static void getData(ArrayList<String> arr, FileReader file) {
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
            String word = scan.nextLine().trim();
            arr.add(word);
        }
    }

    public static String get3letterWord() {
        Collections.shuffle(threeLetterWords);
        return threeLetterWords.getFirst();
    }

    public static String get4letterWord() {
        Collections.shuffle(fourLetterWords);
        return fourLetterWords.getFirst();
    }

    public static String get5letterWord() {
        Collections.shuffle(fiveLetterWords);
        return fiveLetterWords.getFirst();
    }

}
