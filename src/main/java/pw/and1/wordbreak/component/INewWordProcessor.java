package pw.and1.wordbreak.component;

import java.util.List;

public interface INewWordProcessor {
    /**
     * collect new word
     * 收集新词
     *
     * @param word
     */
    void collect(String word);

    /**
     * process collected new word
     * 处理已收集的新词并返回
     *
     * @return
     */
    List<List<String>> process();
}
