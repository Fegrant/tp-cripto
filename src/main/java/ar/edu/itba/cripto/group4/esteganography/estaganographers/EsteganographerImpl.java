package ar.edu.itba.cripto.group4.esteganography.estaganographers;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

public class EsteganographerImpl implements Esteganographer {

    private final int HEADER_SIZE = 54;
    private final double CHI_SQUARED_CRITICAL = 3.841;      // Valor crítico para p=0.05 y 1 grado de libertad

    @Override
    public Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data) {
        return null;
    }

    @Override
    public Stream<Byte> unhide(Stream<Byte> image) {
        return null;
    }

    @Override
    public Boolean analyze(InputStream is) throws IOException {
        // TODO: Ver otros métodos (ahora usa análisis chi-cuadrado de bits menos significativos)
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

        // Guarda la cantidad de '1' y '0' en los bits menos significativos
        int[] bitCounts = new int[2];
        for (byte imageDatum : imageData) {
            bitCounts[imageDatum & 1]++;
        }

        return chiSquareTest(bitCounts, imageData.length / 2.0);
    }

    private boolean chiSquareTest(int[] observed, double expected) {
        double chiSquare = 0;
        for (int count : observed) {
            chiSquare += Math.pow(count - expected, 2) / expected;
        }
        return chiSquare > CHI_SQUARED_CRITICAL;
    }
}
