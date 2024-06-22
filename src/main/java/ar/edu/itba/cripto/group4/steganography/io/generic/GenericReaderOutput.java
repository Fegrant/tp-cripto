package ar.edu.itba.cripto.group4.steganography.io.generic;

import ar.edu.itba.cripto.group4.steganography.io.Metadata;
import ar.edu.itba.cripto.group4.steganography.io.ReaderOutput;

import java.util.stream.Stream;

public record GenericReaderOutput(
    Stream<Byte> data,
    Metadata meta
) implements ReaderOutput {
    @Override
    public Stream<Byte> getData() {
        return data;
    }

    @Override
    public Metadata getMetadata() {
        return meta;
    }
}
