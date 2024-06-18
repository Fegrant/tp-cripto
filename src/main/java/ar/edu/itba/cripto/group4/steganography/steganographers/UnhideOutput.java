package ar.edu.itba.cripto.group4.steganography.steganographers;

import java.util.List;
import java.util.stream.Stream;

public record UnhideOutput(
    List<Byte> data,
    String extension
) {
}
