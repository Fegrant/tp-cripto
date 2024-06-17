package ar.edu.itba.cripto.group4.esteganography.io;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface ReaderWriter<Impl> {
    ReaderOutput<Impl> readFile(Path filepath) throws ReaderException;
    void writeFile(Path filepath, Metadata<Impl> metadata, Stream<Byte> data);
}
