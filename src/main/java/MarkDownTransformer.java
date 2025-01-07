import java.io.File;
import java.io.FileNotFoundException;

public final class MarkDownTransformer {

    private final FileManager fileManager;

    public MarkDownTransformer(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void execute(String inputFilePath, String outputFilePath) throws FileNotFoundException {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        checkForFiles(inputFile, outputFile);

        String inputFileContent = fileManager.read(inputFile);

        MarkDownPage page = new MarkDownPage(inputFileContent);
        page.buildFooterMovingLinks();

        fileManager.writeContent(page.getContent(), outputFile);
    }

    private void checkForFiles(File inputFile, File outputFile) throws FileNotFoundException {
        if (!fileManager.exists(inputFile)) {
            throw new FileNotFoundException("File not found: " + inputFile.getAbsolutePath());
        }
        if (!fileManager.exists(outputFile)) {
            throw new FileNotFoundException("File not found: " + outputFile.getAbsolutePath());
        }
    }

}
