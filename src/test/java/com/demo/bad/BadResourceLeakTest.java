package com.demo.bad;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class BadResourceLeakTest {

    private final BadResourceLeak badResourceLeak = new BadResourceLeak();

    @Test
    void generateToken_returnsNonEmptyString() {
        String result = badResourceLeak.generateToken();
        assertThat(result).isNotEmpty();
        assertThat(result).matches("-?\\d+");
    }

    @Test
    void copyFile_copiesContent(@TempDir Path tempDir) throws IOException {
        Path src = tempDir.resolve("src.txt");
        Path dest = tempDir.resolve("dest.txt");
        Files.writeString(src, "hello world");

        badResourceLeak.copyFile(src.toString(), dest.toString());

        assertThat(Files.readString(dest)).isEqualTo("hello world");
    }
}
