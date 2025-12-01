package com.vpk.ai.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;

public class MCPFileTool {

    private final Path outDir;

    public MCPFileTool(Path outDir) {
        this.outDir = outDir;
    }

    public Map<String, Object> writeReport(String prefix, String content) throws IOException {
        Files.createDirectories(outDir);

        String filename = prefix + "-" +
                Instant.now().toString().replace(":", "-") + ".txt";

        Path path = outDir.resolve(filename);
        Files.writeString(path, content);

        return Map.of(
                "path", path.toAbsolutePath().toString(),
                "filename", filename
        );
    }
}
