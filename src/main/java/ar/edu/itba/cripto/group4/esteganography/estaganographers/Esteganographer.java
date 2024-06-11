package ar.edu.itba.cripto.group4.esteganography.estaganographers;

import java.util.stream.Stream;

public interface Esteganographer {
    Stream<byte> hide(Stream<byte> image, Stream<byte> data);
    Stream<byte> unhide(Stream<byte> image);
}
