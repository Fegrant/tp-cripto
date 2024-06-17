package ar.edu.itba.cripto.group4.steganography.io.bmp;

import ar.edu.itba.cripto.group4.steganography.io.Metadata;
import ar.edu.itba.cripto.group4.steganography.io.ReaderOutput;

import java.util.stream.Stream;

public record BmpReaderOutput (
    Metadata metadata,
    Stream<Byte> data
) implements ReaderOutput {
    @Override
    public Stream<Byte> getData() {
        return data;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }
}
