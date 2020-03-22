package pw.and1.wordbreak;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pw.and1.wordbreak.component.Dictionary;
import java.util.List;

public class WordSegmenterTest {
    private Dictionary publicDictionary;
    private Dictionary userDictionary;
    private WordSegmenter wordSegmenter;

    @Before
    public void init() {
        publicDictionary = Dictionary.newBuilder()
                .appendDictionary("i", "like", "sam", "sung", "samsung", "mobile", "ice", "cream", "man go")
                .build();

        userDictionary = Dictionary.newBuilder()
                .appendDictionary("i", "like", "sam", "sung", "mobile", "icecream", "man go", "mango")
                .build();

        wordSegmenter = WordSegmenter.newBuilder()
                .withPublicDictionary(publicDictionary)
                .withUserDictionary(userDictionary)
                .build();
    }

    /**
     * #Stage 1
     * Given a valid sentence without any spaces between the words and a dictionary of valid English words,
     * find all possible ways to break the sentence in individual dictionary words.
     */
    @Test
    public void testWordBreak1() {
        List<String> result = wordSegmenter.wordBreak("ilikesamsungmobile", WordSegmenter.SearchMode.ONLY_PUBLIC);
        Assert.assertTrue(result.size() == 2);
        Assert.assertEquals("i like sam sung mobile", result.get(0));
        Assert.assertEquals("i like samsung mobile", result.get(1));

        result = wordSegmenter.wordBreak("ilikeicecreamandmango", WordSegmenter.SearchMode.ONLY_PUBLIC);
        Assert.assertTrue(result.size() == 1);
        Assert.assertEquals("i like ice cream and man go", result.get(0));
    }

    /**
     * #Stage 2
     * If user provide a customized dictionary of valid English words as additional input,
     * and the program will only find in the user customized dictionary.
     */
    @Test
    public void testWordBreak2() {
        List<String> result = wordSegmenter.wordBreak("ilikesamsungmobile", WordSegmenter.SearchMode.ONLY_USER);
        Assert.assertTrue(result.size() == 1);
        Assert.assertEquals("i like sam sung mobile", result.get(0));

        result = wordSegmenter.wordBreak("ilikeicecreamandmango", WordSegmenter.SearchMode.ONLY_USER);
        Assert.assertTrue(result.size() == 2);
        Assert.assertEquals("i like icecream and man go", result.get(0));
        Assert.assertEquals("i like icecream and mango", result.get(1));
    }

    /**
     * #Stage 3
     * If user provide a customized dictionary of valid English words as additional input,
     * and the program will find all the valid words in the both dictionaries.
     */
    @Test
    public void testWordBreak3() {
        List<String> result = wordSegmenter.wordBreak("ilikesamsungmobile", WordSegmenter.SearchMode.ALL);
        Assert.assertTrue(result.size() == 2);
        Assert.assertEquals("i like sam sung mobile", result.get(0));
        Assert.assertEquals("i like samsung mobile", result.get(1));

        result = wordSegmenter.wordBreak("ilikeicecreamandmango", WordSegmenter.SearchMode.ALL);
        Assert.assertTrue(result.size() == 4);
        Assert.assertEquals("i like icecream and man go", result.get(0));
        Assert.assertEquals("i like icecream and mango", result.get(1));
        Assert.assertEquals("i like ice cream and man go", result.get(2));
        Assert.assertEquals("i like ice cream and mango", result.get(3));
    }

    @Test
    public void testWordBreak4() {
        Dictionary userDictionary = Dictionary.newBuilder()
                .appendDictionary("i", "like", "sam", "sung", "mobile", "icecream", "man go")
                .build();
        wordSegmenter.setUserDictionary(userDictionary);

        List<String> result = wordSegmenter.wordBreak("ilikeicecreamandmango", WordSegmenter.SearchMode.ONLY_USER);
        Assert.assertTrue(result.size() == 1);
        Assert.assertEquals("i like icecream and man go", result.get(0));
    }

    @Test
    public void testWordBreak5() {
        Dictionary userDictionary = Dictionary.newBuilder()
                .appendDictionary("i", "like", "sam", "sung", "samsung", "mobile", "samsungmobile", "icecream", "man go")
                .build();
        wordSegmenter.setUserDictionary(userDictionary);

        List<String> result = wordSegmenter.wordBreak("youlikesamsungmobile", WordSegmenter.SearchMode.ONLY_USER);
        Assert.assertTrue(result.size() == 3);
        Assert.assertEquals("you like samsungmobile", result.get(0));
        Assert.assertEquals("you like samsung mobile", result.get(1));
        Assert.assertEquals("you like sam sung mobile", result.get(2));
    }

    @Test
    public void testWordBreak6() {
        Dictionary userDictionary = Dictionary.newBuilder()
                .appendDictionary("i", "like", "sam", "sung", "samsung", "mobile", "samsungmobile", "ice", "cream", "icecream", "mango")
                .build();
        wordSegmenter.setUserDictionary(userDictionary);

        List<String> result = wordSegmenter.wordBreak("youlikesamsungmobileandicecreamandmango", WordSegmenter.SearchMode.ALL);
        Assert.assertTrue(result.size() == 12);
        Assert.assertEquals("you like samsungmobile and icecream and man go", result.get(0));
        Assert.assertEquals("you like samsungmobile and icecream and mango", result.get(1));
        Assert.assertEquals("you like samsungmobile and ice cream and man go", result.get(2));
        Assert.assertEquals("you like samsungmobile and ice cream and mango", result.get(3));
        Assert.assertEquals("you like samsung mobile and icecream and man go", result.get(4));
        Assert.assertEquals("you like samsung mobile and icecream and mango", result.get(5));
        Assert.assertEquals("you like samsung mobile and ice cream and man go", result.get(6));
        Assert.assertEquals("you like samsung mobile and ice cream and mango", result.get(7));
        Assert.assertEquals("you like sam sung mobile and icecream and man go", result.get(8));
        Assert.assertEquals("you like sam sung mobile and icecream and mango", result.get(9));
        Assert.assertEquals("you like sam sung mobile and ice cream and man go", result.get(10));
        Assert.assertEquals("you like sam sung mobile and ice cream and mango", result.get(11));
    }
}
