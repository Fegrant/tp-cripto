package ar.edu.itba.cripto.group4.esteganography.io;

import java.nio.file.Path;
import java.util.stream.Stream;

public class BmpReaderWriter implements ReaderWriter<BmpReaderWriter> {
    @Override
    public ReaderOutput<BmpReaderWriter> readFile(Path filepath) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void writeFile(Path filepath, Metadata<BmpReaderWriter> metadata, Stream<byte> data) {
        throw new RuntimeException("Not Implemented");
    }
}
