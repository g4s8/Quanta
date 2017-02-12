package com.g4s8.quanta

import java.util.zip.ZipFile

/**
 * Created by g4s8 on 2/11/17.
 */
class ResInputStream extends InputStream {

    private static byte[] content(String name) throws IOException {
        URL url = ResInputStream.class.getClassLoader().getResource(name)
        if (url == null) {
            throw new IOException("Resource was not found: $name")
        }

        try {
            return url.openStream().bytes
        } catch (IOException ignored) {
            // fallback to read JAR directly
            def connection = url.openConnection() as JarURLConnection
            def jarFile = connection.jarFileURL.toURI()
            ZipFile zip
            try {
                zip = new ZipFile(new File(jarFile))
            } catch (FileNotFoundException ex) {
                throw new IOException("Resource was not found in jar: $name", ex)
            }
            return zip.getInputStream((zip.getEntry(name))).bytes
        }
    }

    private final InputStream origin

    private static String normalized(String origin) {
        return origin.startsWith("/") ? origin.substring(1) : origin
    }

    ResInputStream(String name) {
        origin = new ByteArrayInputStream(content(normalized(name)))
    }

    @Override
    int read() throws IOException {
        return origin.read()
    }

    @Override
    int read(byte[] bytes) throws IOException {
        return origin.read(bytes)
    }

    @Override
    int read(byte[] bytes, int i, int i1) throws IOException {
        return origin.read(bytes, i, i1)
    }

    @Override
    void close() throws IOException {
        origin.close()
    }
}
