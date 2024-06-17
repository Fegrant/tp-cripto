package ar.edu.itba.cripto.group4.steganography.steganographers;

import ar.edu.itba.cripto.group4.steganography.io.Metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

public interface Steganographer {
    Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data, SteganographerMethod method);
    Stream<Byte> unhide(Stream<Byte> image, Metadata meta, SteganographerMethod method);
    boolean analyze(InputStream file) throws IOException;
}
