import java.io.*;

public final class FileManager {

    public boolean exists(File file) {
        return file.exists();
    }

    public String read(File file) {
        String content;

        try (FileReader reader = new FileReader(file)) {
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

    public void writeContent(String content, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
