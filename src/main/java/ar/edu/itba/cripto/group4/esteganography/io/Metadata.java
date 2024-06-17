package ar.edu.itba.cripto.group4.esteganography.io;

public interface Metadata<Impl> {
    int getTotalFileSize();
    
    int getHeaderSize();
    
    int getDataSize();
    
    byte[] getFirstFour();
    
    byte[] getHeader();
}
