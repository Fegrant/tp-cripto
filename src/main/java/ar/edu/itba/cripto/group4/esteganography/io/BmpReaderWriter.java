package ar.edu.itba.cripto.group4.esteganography.io;

import java.nio.file.Path;
import java.util.stream.Stream;

public class BmpReaderWriter implements ReaderWriter<BmpReaderWriter> {
    @Override
    public ReaderOutput<BmpReaderWriter> readFile(Path filepath) {
        /*
        byte[] header = is.readNBytes(HEADER_SIZE);

        // Verifico que sea un archivo BMP válido
        if (header.length != HEADER_SIZE || header[0] != 'B' || header[1] != 'M') {
            return false;
        }

        int fileSize = ((header[5] & 0xff) << 24) | ((header[4] & 0xff) << 16) |
                ((header[3] & 0xff) << 8) | (header[2] & 0xff);

        int dataOffset = ((header[13] & 0xff) << 24) | ((header[12] & 0xff) << 16) |
                ((header[11] & 0xff) << 8) | (header[10] & 0xff);

        is.skip(dataOffset - HEADER_SIZE);
        byte[] imageData = is.readNBytes(fileSize - dataOffset);

        // Si no se pudieron leer todos los datos de la imagen
        if (imageData.length != fileSize - dataOffset) {
            return false;
        }
         */
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void writeFile(Path filepath, Metadata<BmpReaderWriter> metadata, Stream<Byte> data) {
        throw new RuntimeException("Not Implemented");
    }
}
