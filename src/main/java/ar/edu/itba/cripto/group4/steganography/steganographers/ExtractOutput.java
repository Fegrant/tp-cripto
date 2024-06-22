package ar.edu.itba.cripto.group4.steganography.steganographers;

import java.util.List;

public record ExtractOutput(
    List<Byte> data,
    String extension
) {
}
