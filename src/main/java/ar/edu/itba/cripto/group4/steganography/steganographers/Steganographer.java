package ar.edu.itba.cripto.group4.steganography.steganographers;

import ar.edu.itba.cripto.group4.steganography.io.Metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Steganographer {
    Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data, String dataFilename, SteganographerMethod method, Function<List<Byte>,List<Byte>> encriptionFunction);
    UnhideOutput unhide(Stream<Byte> image, Metadata meta, SteganographerMethod method, Function<List<Byte>,List<Byte>> decryptionFunction);
    boolean analyze(InputStream file) throws IOException;
}
