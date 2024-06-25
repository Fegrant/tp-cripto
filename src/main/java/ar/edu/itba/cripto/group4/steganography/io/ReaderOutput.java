package ar.edu.itba.cripto.group4.steganography.io;

import java.util.stream.Stream;

public interface ReaderOutput {
    Stream<Byte> getData();

    Metadata getMetadata();
}
