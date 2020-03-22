package pw.and1.wordbreak;

import pw.and1.wordbreak.component.Dictionary;

import java.util.Collection;
import java.util.List;

public class WordBreakApplication {
    public static void main(String[] args) {
        Dictionary publicDictionary = Dictionary.newBuilder()
                .appendDictionary("i", "like", "sam", "sung", "samsung", "mobile", "ice", "cream", "man go")
                .build();

        Dictionary userDictionary = Dictionary.newBuilder()
                .appendDictionary("i", "like", "sam", "sung", "mobile", "icecream", "man go", "mango")
                .build();

        WordSegmenter wordSegmenter = WordSegmenter.newBuilder()
                .withPublicDictionary(publicDictionary)
                .withUserDictionary(userDictionary)
                .build();

        /**
         * #Stage 1
         * Given a valid sentence without any spaces between the words and a dictionary of valid English words,
         * find all possible ways to break the sentence in individual dictionary words.
         */
        System.out.println("------------#Stage 1------------");
        String sentence = "ilikesamsungmobile";
        List<String> result = wordSegmenter.wordBreak(sentence, WordSegmenter.SearchMode.ONLY_PUBLIC);
        println(sentence, result);

        sentence = "ilikeicecreamandmango";
        result = wordSegmenter.wordBreak(sentence, WordSegmenter.SearchMode.ONLY_PUBLIC);
        println(sentence, result);

        /**
         * #Stage 2
         * If user provide a customized dictionary of valid English words as additional input,
         * and the program will only find in the user customized dictionary.
         */
        System.out.println("------------#Stage 2------------");
        sentence = "ilikesamsungmobile";
        result = wordSegmenter.wordBreak(sentence, WordSegmenter.SearchMode.ONLY_USER);
        println(sentence, result);

        sentence = "ilikeicecreamandmango";
        result = wordSegmenter.wordBreak(sentence, WordSegmenter.SearchMode.ONLY_USER);
        println(sentence, result);

        /**
         * #Stage 3
         * If user provide a customized dictionary of valid English words as additional input,
         * and the program will find all the valid words in the both dictionaries.
         */
        System.out.println("------------#Stage 3------------");
        sentence = "ilikesamsungmobile";
        result = wordSegmenter.wordBreak(sentence, WordSegmenter.SearchMode.ALL);
        println(sentence, result);

        sentence = "ilikeicecreamandmango";
        result = wordSegmenter.wordBreak(sentence, WordSegmenter.SearchMode.ALL);
        println(sentence, result);
    }

    private static void println(String input, Collection<String> collection) {
        System.out.println("input: ");
        System.out.println(input);
        System.out.println("output: ");
        collection.forEach(System.out::println);
        System.out.println();
    }
}