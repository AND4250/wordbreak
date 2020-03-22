package pw.and1.wordbreak.component;

public class DefaultNewWordProcessorFactory implements INewWordProcessorFactory {
    private static DefaultNewWordProcessorFactory instance = new DefaultNewWordProcessorFactory();

    public static DefaultNewWordProcessorFactory getInstance() {
        return instance;
    }

    private DefaultNewWordProcessorFactory() {

    }

    @Override
    public INewWordProcessor getNewWordProcessor() {
        return new DefaultNewWordProcessor();
    }
}
