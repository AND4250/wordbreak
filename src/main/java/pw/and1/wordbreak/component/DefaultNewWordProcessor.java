package pw.and1.wordbreak.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 默认新词处理器
 * 线程不安全，只能局部变量使用
 */
public class DefaultNewWordProcessor implements INewWordProcessor {
    private StringBuilder builder;

    DefaultNewWordProcessor() {
        builder = new StringBuilder();
    }

    @Override
    public void collect(String word) {
        builder.append(word);
    }

    @Override
    public List<List<String>> process() {
        List<List<String>> result = new ArrayList<>();
        if (builder.length() == 0) {
            return result;
        }
        result.add(Arrays.asList(builder.toString()));
        builder.setLength(0);
        return result;
    }
}
