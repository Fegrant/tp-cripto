package ar.edu.itba.cripto.group4.steganography.steganographers;

import ar.edu.itba.cripto.group4.steganography.io.Metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

public class SteganographerImpl implements Steganographer {

    private static final int HEADER_SIZE = 54;
    private static final double CHI_SQUARED_CRITICAL = 3.841;      // Valor crítico para p=0.05 y 1 grado de libertad

    @Override
    public Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data, SteganographerMethod method) {
        return method.hide(image, data);
    }

    @Override
    public Stream<Byte> unhide(Stream<Byte> image, Metadata meta, SteganographerMethod method) {
        return method.unhide(image, meta);
    }

    @Override
    public boolean analyze(InputStream is) throws IOException {
        // TODO: Ver otros métodos (ahora usa análisis chi-cuadrado de bits menos significativos)
        is.skip(HEADER_SIZE);
        byte[] imageData = is.readAllBytes();
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
        System.out.println(chiSquare);
        return chiSquare > CHI_SQUARED_CRITICAL;
    }
}
