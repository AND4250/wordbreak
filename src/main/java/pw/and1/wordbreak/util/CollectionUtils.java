package pw.and1.wordbreak.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CollectionUtils {
    /**
     * cartesian product
     * 集合笛卡尔乘积运算
     * @param sourceList
     * @return
     */
    public static List<List<String>> descartes(List<List<String>> sourceList) {
        List<List<String>> targetList =  new ArrayList<>();
        descartes(sourceList, targetList, new Stack<>(), 0);
        return targetList;
    }

    private static void descartes(List<List<String>> sourceList, List<List<String>> targetList, Stack<String> stack, int layer) {
        boolean isEnd = layer == sourceList.size() - 1;
        sourceList.get(layer).forEach(str -> {
            stack.push(str);
            if (isEnd) {
                targetList.add(new ArrayList<>(stack));
            } else {
                descartes(sourceList, targetList, stack, layer + 1);
            }
            stack.pop();
        });
    }
}
