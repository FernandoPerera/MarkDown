import java.io.FileNotFoundException;

public final class MarkDownTransformer {

    private final FileManager fileManager;

    public MarkDownTransformer(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void execute(String inputFilePath, String outputFilePath) throws FileNotFoundException {
        checkForFiles(inputFilePath, outputFilePath);

        String inputFileContent = fileManager.read(inputFilePath);

        MarkDownPage page = new MarkDownPage(inputFileContent);
        page.buildFooterMovingLinks();

        fileManager.writeContent(page.getPage(), outputFilePath);
    }

    private void checkForFiles(String inputFilePath, String outputFilePath) throws FileNotFoundException {
        if (fileManager.notExists(inputFilePath)) {
            throw new FileNotFoundException("File not found: " + inputFilePath);
        }
        if (fileManager.notExists(outputFilePath)) {
            throw new FileNotFoundException("File not found: " + outputFilePath);
        }
    }

}
