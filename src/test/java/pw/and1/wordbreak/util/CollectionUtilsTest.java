package pw.and1.wordbreak.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionUtilsTest {
    @Test
    public  void testDescartes() {
        List<List<String>> list = new ArrayList<>();
        list.add(Arrays.asList("11", "12"));
        list.add(Arrays.asList("21", "22"));
        list.add(Arrays.asList("31", "32"));
        list = CollectionUtils.descartes(list);

        List<String[]> testList = list.stream().map(l -> l.toArray(new String[]{})).collect(Collectors.toList());
        Assert.assertTrue(testList.size() == 8);
        Assert.assertArrayEquals(new String[]{"11", "21", "31"}, testList.get(0));
        Assert.assertArrayEquals(new String[]{"11", "21", "32"}, testList.get(1));
        Assert.assertArrayEquals(new String[]{"11", "22", "31"}, testList.get(2));
        Assert.assertArrayEquals(new String[]{"11", "22", "32"}, testList.get(3));
        Assert.assertArrayEquals(new String[]{"12", "21", "31"}, testList.get(4));
        Assert.assertArrayEquals(new String[]{"12", "21", "32"}, testList.get(5));
        Assert.assertArrayEquals(new String[]{"12", "22", "31"}, testList.get(6));
        Assert.assertArrayEquals(new String[]{"12", "22", "32"}, testList.get(7));
    }
}
