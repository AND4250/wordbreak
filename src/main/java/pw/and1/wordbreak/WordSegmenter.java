package pw.and1.wordbreak;

import pw.and1.wordbreak.component.DefaultNewWordProcessorFactory;
import pw.and1.wordbreak.component.Dictionary;
import pw.and1.wordbreak.component.INewWordProcessor;
import pw.and1.wordbreak.component.INewWordProcessorFactory;
import pw.and1.wordbreak.util.CollectionUtils;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class WordSegmenter {
    public enum SearchMode {
        ALL, ONLY_PUBLIC, ONLY_USER
    }

    public static class WordSegmenterBuilder {
        private Dictionary publicDictionary;
        private Dictionary userDictionary;
        private INewWordProcessorFactory newWordProcessorFactory;

        public WordSegmenterBuilder withPublicDictionary(Dictionary dictionary) {
            this.publicDictionary = dictionary;
            return this;
        }

        public WordSegmenterBuilder withUserDictionary(Dictionary dictionary) {
            this.userDictionary = dictionary;
            return this;
        }

        public WordSegmenterBuilder withNewWordProcessorFactory(INewWordProcessorFactory newWordProcessorFactory) {
            this.newWordProcessorFactory = newWordProcessorFactory;
            return this;
        }

        public WordSegmenter build() {
            if (newWordProcessorFactory == null) {
                withNewWordProcessorFactory(DefaultNewWordProcessorFactory.getInstance());
            }
            return new WordSegmenter(publicDictionary, userDictionary, newWordProcessorFactory);
        }
    }

    public static WordSegmenterBuilder newBuilder() {
        return new WordSegmenterBuilder();
    }

    private Dictionary publicDictionary;
    private Dictionary userDictionary;
    private INewWordProcessorFactory newWordProcessorFactory;

    private WordSegmenter(Dictionary publicDictionary, Dictionary userDictionary, INewWordProcessorFactory newWordProcessorFactory) {
        this.publicDictionary = publicDictionary;
        this.userDictionary = userDictionary;
        this.newWordProcessorFactory = newWordProcessorFactory;
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
     * @param maxLength
     * @param isSub
     * @return
     */
    private List<List<String>> wordBreak(final String sentence, Dictionary dictionary, int maxLength, boolean isSub) {
        List<List<String>> result = new ArrayList<>();
        if (sentence == null || sentence.isEmpty()) {
            return result;
        }

        INewWordProcessor newWordProcessor = newWordProcessorFactory.getNewWordProcessor();
        int minLength = dictionary.getMinLength();
        int pointer = 0;
        int len = sentence.length();
        while (pointer < len) {
            int upper = Math.min(len, pointer + maxLength);
            String sub;
            Set<String> searchResults;

            do {
                sub = sentence.substring(pointer, upper);
                searchResults = dictionary.search(sub);
                upper --;
            } while (searchResults.isEmpty() && upper - pointer >= minLength);

            // 子串中不处理新词
            if (searchResults.isEmpty() && isSub) {
                result.clear();
                return result;
            }

            int subLen = sub.length();
            if (subLen > minLength * 2) {// 子串长度必须大于词典中最小长度单词的两倍才做子串分词处理
                List<List<String>> r = wordBreak(sub, dictionary, subLen - 1, true);
                r.stream().filter(l -> !l.isEmpty()).map(l -> String.join(" ", l)).forEach(searchResults::add);
            }
            if (searchResults.isEmpty()) {// 新词采集
                newWordProcessor.collect(sub);
            } else {
                // 新词处理
                result.addAll(newWordProcessor.process());
                result.add(new ArrayList<>(searchResults));
            }
            pointer += subLen;
        }
        // 避免句子结尾有新词漏掉处理
        result.addAll(newWordProcessor.process());
        return CollectionUtils.descartes(result);
    }

    public WordSegmenter setUserDictionary(Dictionary dictionary) {
        userDictionary = dictionary;
        return this;
    }

    private Dictionary selectDictionary(SearchMode mode) {
        switch (mode) {
            case ALL:
                return Dictionary.newBuilder().appendDictionary(publicDictionary, userDictionary).build();
            case ONLY_USER:
                return userDictionary;
            case ONLY_PUBLIC:
            default:
                return publicDictionary;
        }
    }
}