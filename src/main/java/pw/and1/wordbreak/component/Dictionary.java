package pw.and1.wordbreak.component;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Objects;
import java.util.Map;
import java.util.HashMap;

public class Dictionary {
    public static class DictionaryBuilder {
        private Set<String> dictionary;

        private DictionaryBuilder() {
            this.dictionary = new HashSet<>();
        }

        public DictionaryBuilder appendDictionary(Dictionary... dictionaries) {
            Arrays.stream(dictionaries).filter(Objects::nonNull).map(d -> d.dictionary).forEach(dictionary::addAll);
            return this;
        }

        public DictionaryBuilder appendDictionary(String... words) {
            Arrays.stream(words).map(String::trim).forEach(dictionary::add);
            return this;
        }

        public Dictionary build() {
            return new Dictionary(dictionary);
        }
    }

    public static DictionaryBuilder newBuilder() {
        return new DictionaryBuilder();
    }

    private Set<String> dictionary;
    private Map<String, Set<String>> actualWords;
    private int maxLength;
    private int minLength;

    private Dictionary(Set<String> dictionary) {
        this.dictionary = dictionary;
        this.actualWords = new HashMap<>();
        this.maxLength = 0;
        this.minLength = Integer.MAX_VALUE;
        for (String word : dictionary) {
            String newWord = word.replaceAll("[\\s-]", "");
            Set<String> words = this.actualWords.get(newWord);
            if (words != null) {
                words.add(word);
            } else if (!newWord.equals(word)) {
                words = new HashSet<>();
                words.add(word);
                this.actualWords.put(newWord, words);
            }
            this.maxLength = Math.max(maxLength, newWord.length());
            this.minLength = Math.min(minLength, newWord.length());
        }
    }

    /**
     * One or more ways to find matching words in a dictionary
     * 从词典中查找匹配词的一种或多种写法
     *
     * @param word
     * @return
     */
    public Set<String> search(String word) {
        Set<String> words = actualWords.get(word);
        if (words == null) {
            words = new HashSet<>();
            if (dictionary.contains(word)) {
                words.add(word);
            }
        }
        return words;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getMinLength() {
        return minLength;
    }
}