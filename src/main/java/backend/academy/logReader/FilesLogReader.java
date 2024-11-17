package backend.academy.logReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import lombok.Getter;

public class FilesLogReader implements LogReader {

    private Path pathToCurrentFile;
    private BufferedReader reader;
    private Deque<Path> paths = new ArrayDeque<>();
    @Getter private List<Path> pathsOut = new ArrayList<>();
    private Path startPath = Path.of(System.getenv("HOME"));

    public FilesLogReader(String path) throws IOException {
        findFiles(Path.of(path));
        pathsOut = paths.stream().toList();
        if (paths.isEmpty()) {
            throw new IOException("No such files.");
        }
        pathToCurrentFile = paths.getFirst();
        reader = Files.newBufferedReader(pathToCurrentFile);
    }

    public void findFiles(Path path) {
        try {
            Path currentDir = Paths.get("").toAbsolutePath();

            Path resolvedPath = path.isAbsolute() ? path : currentDir.resolve(path);

            if (Files.exists(resolvedPath) && !resolvedPath.toString().contains("*")
                && !resolvedPath.toString().contains("?")) {
                if (Files.isDirectory(resolvedPath)) {
                    Files.walkFileTree(resolvedPath, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                            paths.add(file.toAbsolutePath());
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } else {
                    paths.add(resolvedPath.toAbsolutePath());
                }
            } else {
                String pattern = resolvedPath.toString();
                PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);

                Files.walkFileTree(currentDir, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        if (matcher.matches(file.toAbsolutePath())) {
                            paths.add(file.toAbsolutePath());
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Error while searching files: " + e.getMessage());
        }
    }

    @Override
    public String readLog() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            paths.removeFirst();
            reader.close();
            if (!paths.isEmpty()) {
                pathToCurrentFile = paths.getFirst();
                reader = Files.newBufferedReader(pathToCurrentFile);
                return readLog();
            } else {
                return null;
            }
        }
        return line;
    }
}
