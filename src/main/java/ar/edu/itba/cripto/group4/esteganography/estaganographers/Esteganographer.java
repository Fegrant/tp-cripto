package ar.edu.itba.cripto.group4.esteganography.estaganographers;

import ar.edu.itba.cripto.group4.esteganography.io.Metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

public interface Esteganographer {
    Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data, EsteganographerMethod method);
    Stream<Byte> unhide(Stream<Byte> image, Metadata meta, EsteganographerMethod method);
    boolean analyze(InputStream file) throws IOException;
}
