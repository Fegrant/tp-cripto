package ar.edu.itba.cripto.group4.esteganography.io.bmp;

import ar.edu.itba.cripto.group4.esteganography.io.Metadata;

public record BmpMetadata(
    int totalSize,
    Byte[] firstFour,
    byte[] header
) implements Metadata {
    @Override
    public int getTotalFileSize() {
        return totalSize;
    }

    @Override
    public int getHeaderSize() {
        return header.length;
    }

    @Override
    public int getDataSize() {
        return totalSize - header.length;
    }

    @Override
    public Byte[] getFirstFour() {
        return firstFour;
    }

    @Override
    public byte[] getHeader() {
        return header;
    }
}
