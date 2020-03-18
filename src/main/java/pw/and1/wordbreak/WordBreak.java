package pw.and1.wordbreak;

import pw.and1.wordbreak.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.stream.Collectors;

public class WordBreak {
    public static class WordBreakBuilder {
        private Set<String> dictionary;

        private WordBreakBuilder(boolean useDefault) {
            dictionary = new HashSet<>();
            if (useDefault) {
                Collections.addAll(dictionary, "i","like","sam","sung","samsung","mobile","ice","cream","man go");
            }
        }

        public WordBreakBuilder appendDictionary(String... words) {
            Arrays.stream(words).peek(String::trim).forEach(dictionary::add);
            return this;
        }

        public WordBreak build() {
            return new WordBreak(dictionary);
        }
    }

    public static WordBreakBuilder newBuilder() {
        return new WordBreakBuilder(false);
    }

    public static WordBreakBuilder newDefaultBuilder() {
        return new WordBreakBuilder(true);
    }

    private Set<String> dictionary;
    private Map<String, Set<String>> actualWord;
    private int maxLength;

    private WordBreak(Set<String> dictionary) {
        this.dictionary = new HashSet<>();
        this.actualWord = new HashMap<>();
        for (String word : dictionary) {
            String newWord = word.replaceAll("\\s", "");
            Set<String> words = this.actualWord.get(newWord);
            if (words != null) {
                words.add(word);
            } else if (!newWord.equals(word)) {
                words = new HashSet<>();
                words.add(word);
                this.actualWord.put(newWord, words);
            }
            this.dictionary.add(newWord);
            this.maxLength = Math.max(maxLength, newWord.length());
        }
    }

    public List<String> wordBreak(final String sentence) {
        List<List<String>> result = wordBreak(sentence, maxLength, false);
        return result.stream().map(l -> String.join(" ", l)).collect(Collectors.toList());
    }

    /**
     * based on FMM(Forward Maximum Matching)
     *
     * @param sentence
     * @param max
     * @param isSub
     * @return
     */
    private List<List<String>> wordBreak(final String sentence, int max, boolean isSub) {
        List<List<String>> result = new ArrayList<>();
        if (sentence == null || sentence.isEmpty()) {
            return result;
        }

        StringBuffer mismatches = new StringBuffer();
        int len = sentence.length();
        int pointer = 0;
        while (pointer < len) {
            Set<String> nodes = new HashSet<>();
            int upper = Math.min(len, pointer + max);
            String sub = sentence.substring(pointer, upper);
            int subLen = sub.length();
            boolean mismatch;

            while (mismatch = !dictionary.contains(sub)) {
                if (subLen == 1) {
                    break;
                }
                sub = sub.substring(0, subLen - 1);
                subLen = sub.length();
            }

            if (mismatch && isSub) {
                result.clear();
                return result;
            }

            if (subLen > 1) {
                List<List<String>> r = wordBreak(sub, subLen - 1, true);
                r.stream().filter(l -> !l.isEmpty()).map(l -> String.join(" ", l)).forEach(nodes::add);
            }
            if (mismatch) {
                mismatches.append(sub);
            } else {
                if (mismatches.length() > 0) {
                    result.add(Arrays.asList(mismatches.toString()));
                    mismatches.setLength(0);
                }
                Set<String> words = actualWord.get(sub);
                if (words != null) {
                    nodes.addAll(words);
                } else {
                    nodes.add(sub);
                }
                result.add(new ArrayList<>(nodes));
            }
            pointer += subLen;
        }
        if (mismatches.length() > 0) {
            result.add(Arrays.asList(mismatches.toString()));
        }
        return CollectionUtils.descartes(result);
    }
}