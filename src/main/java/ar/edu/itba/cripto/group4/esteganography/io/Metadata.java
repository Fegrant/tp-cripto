package ar.edu.itba.cripto.group4.esteganography.io;

public interface Metadata {
    int getTotalFileSize();
    
    int getHeaderSize();
    
    int getDataSize();
    
    Byte[] getFirstFour();
    
    byte[] getHeader();
}
