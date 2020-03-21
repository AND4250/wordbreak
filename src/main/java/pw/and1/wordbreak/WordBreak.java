package pw.and1.wordbreak;

import pw.and1.wordbreak.util.CollectionUtils;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class WordBreak {
    public enum SearchMode {
        ALL, ONLY_PUBLIC, ONLY_USER;
    }

    public static class WordBreakBuilder {
        private Dictionary publicDictionary;
        private Dictionary userDictionary;

        public WordBreakBuilder withPublicDictionary(Dictionary dictionary) {
            this.publicDictionary = dictionary;
            return this;
        }

        public WordBreakBuilder withUserDictionary(Dictionary dictionary) {
            this.userDictionary = dictionary;
            return this;
        }

        public WordBreak build() {
            return new WordBreak(publicDictionary, userDictionary);
        }
    }

    public static WordBreakBuilder newBuilder() {
        return new WordBreakBuilder();
    }

    private Dictionary publicDictionary;
    private Dictionary userDictionary;

    private WordBreak(Dictionary publicDictionary, Dictionary userDictionary) {
        this.publicDictionary = publicDictionary;
        this.userDictionary = userDictionary;
    }

    public List<String> wordBreak(final String sentence, SearchMode mode) {
        Dictionary dictionary = selectDictionary(mode);
        List<List<String>> result = wordBreak(sentence, dictionary, dictionary.getMaxLength(), false);
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
    private List<List<String>> wordBreak(final String sentence, Dictionary dictionary, int max, boolean isSub) {
        List<List<String>> result = new ArrayList<>();
        if (sentence == null || sentence.isEmpty()) {
            return result;
        }

        StringBuffer mismatches = new StringBuffer();
        int pointer = 0;
        int len = sentence.length();
        while (pointer < len) {
            Set<String> nodes = new HashSet<>();
            int upper = Math.min(len, pointer + max);
            String sub;
            Set<String> searchResults;

            do {
                sub = sentence.substring(pointer, upper);
                searchResults = dictionary.search(sub);
                upper --;
            } while (searchResults.isEmpty() && upper > pointer);

            // 子串中不处理新词
            if (searchResults.isEmpty() && isSub) {
                result.clear();
                return result;
            }

            int subLen = sub.length();
            if (subLen > 1) {
                List<List<String>> r = wordBreak(sub, dictionary, subLen - 1, true);
                r.stream().filter(l -> !l.isEmpty()).map(l -> String.join(" ", l)).forEach(nodes::add);
            }
            if (searchResults.isEmpty()) {
                mismatches.append(sub);
            } else {
                if (mismatches.length() > 0) {
                    result.add(Arrays.asList(mismatches.toString()));
                    mismatches.setLength(0);
                }
                nodes.addAll(searchResults);
                result.add(new ArrayList<>(nodes));
            }
            pointer += subLen;
        }
        if (mismatches.length() > 0) {
            result.add(Arrays.asList(mismatches.toString()));
        }
        return CollectionUtils.descartes(result);
    }

    public WordBreak setPublicDictionary(Dictionary dictionary) {
        publicDictionary = dictionary;
        return this;
    }

    public WordBreak setUserDictionary(Dictionary dictionary) {
        userDictionary = dictionary;
        return this;
    }

    private Dictionary selectDictionary(SearchMode mode) {
        switch (mode) {
            case ALL:
                return Dictionary.merge(publicDictionary, userDictionary);
            case ONLY_USER:
                return userDictionary;
            case ONLY_PUBLIC:
            default:
                return publicDictionary;
        }
    }
}