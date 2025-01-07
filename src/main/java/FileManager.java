import java.io.*;

public final class FileManager {

    public boolean notExists(String filePath) {
        return !new File(filePath).exists();
    }

    public String read(String filePath) {
        String content;

        try (FileReader reader = new FileReader(filePath)) {
            StringBuilder stringBuilder = new StringBuilder();
            int accumulator;
            while ((accumulator = reader.read()) != -1) {
                stringBuilder.append((char) accumulator);
            }
            content = stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content;
    }

    public void writeContent(String content, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
