package com.demo.bad;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * BAD CODE - For SonarQube demo: Resource leak, use of Random
 */
public class BadResourceLeak {

    // BAD: Stream not closed - SonarQube S2095
    public String readFirstLine(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        return reader.readLine();
    }

    // BAD: Use of java.util.Random for security-sensitive - SonarQube S2245 (if used for token)
    public String generateToken() {
        return String.valueOf(new java.util.Random().nextLong());
    }

    // BAD: PrintStackTrace in production code
    public void copyFile(String src, String dest) {
        try (InputStream in = Files.newInputStream(Path.of(src));
             OutputStream out = Files.newOutputStream(Path.of(dest))) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) {
                out.write(buf, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
