package ar.edu.itba.cripto.group4.esteganography.io;

import java.util.stream.Stream;

public interface ReaderOutput<Impl> {
    Stream<byte> getData();
    Metadata<Impl> getMetadata();
}
