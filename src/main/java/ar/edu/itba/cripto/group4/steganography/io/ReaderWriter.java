package ar.edu.itba.cripto.group4.steganography.io;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface ReaderWriter {
    ReaderOutput readFile(Path filepath) throws ReaderException;
    void writeFile(Path filepath, Stream<Byte> data, Metadata meta);
}
