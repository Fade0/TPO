package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;


public class Futil {

    public static void processDir(String dirName, String resultFileName) {
        Charset inputCharset = Charset.forName("windows-1250");
        Charset outputCharset = StandardCharsets.UTF_8;
        Path startPath = Paths.get(dirName);
        Path resultPath = Paths.get(resultFileName);

        try {
            FileChannel dest = FileChannel.open(resultPath, StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE);

            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                {
                    ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                    FileChannel source = FileChannel.open(file);

                    while (source.read(buffer) != -1) {
                        buffer.flip();
                        CharBuffer charBuffer = inputCharset.decode(buffer);
                        dest.write(outputCharset.encode(charBuffer));
                        buffer.clear(); //?
                    }

                    source.close();
                    return FileVisitResult.CONTINUE;
                }
            });

            dest.close(); //?
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}