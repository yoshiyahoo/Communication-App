import java.nio.file.*;

public class Database_Test {
	public static void main(String[] args) throws Exception {
        Path testRoot = Paths.get("./src/Database");

        System.out.println("Test root directory: " + testRoot.toAbsolutePath());
        DirectoryStream<Path> stream = Files.newDirectoryStream(testRoot);
        for (Path file: stream) {
            System.out.println(file.getFileName().toString());
        }

	}
}
