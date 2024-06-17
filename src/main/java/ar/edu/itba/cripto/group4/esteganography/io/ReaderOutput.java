package ar.edu.itba.cripto.group4.esteganography.io;

import java.util.stream.Stream;

public interface ReaderOutput {
    Stream<Byte> getData();
    Metadata getMetadata();
}
