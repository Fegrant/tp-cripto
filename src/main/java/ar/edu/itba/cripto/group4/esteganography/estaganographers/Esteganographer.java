package ar.edu.itba.cripto.group4.esteganography.estaganographers;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

public interface Esteganographer {
    Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data);
    Stream<Byte> unhide(Stream<Byte> image);

    Boolean analyze(InputStream file) throws IOException;
}
