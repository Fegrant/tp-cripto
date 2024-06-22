package ar.edu.itba.cripto.group4.steganography.io.generic;

import ar.edu.itba.cripto.group4.steganography.io.Metadata;

public record GenericMetadata(
    int filesize,
    String filename
) implements Metadata {
    @Override
    public int getTotalFileSize() {
        return filesize;
    }

    @Override
    public int getHeaderSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getDataSize() {
        return filesize;
    }

    @Override
    public Byte[] getFirstFour() {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getHeader() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFilename() {
        return filename;
    }
}
