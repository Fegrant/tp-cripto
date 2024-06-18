package ar.edu.itba.cripto.group4.steganography.steganographers;

import ar.edu.itba.cripto.group4.steganography.Utils;
import ar.edu.itba.cripto.group4.steganography.io.Metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SteganographerImpl implements Steganographer {

    private static final int HEADER_SIZE = 54;
    private static final double CHI_SQUARED_CRITICAL = 3.841;      // Valor crítico para p=0.05 y 1 grado de libertad

    @Override
    public Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data, String dataFilename, SteganographerMethod method, Function<List<Byte>,List<Byte>> encrypt) {
        final var dataList = data.toList();
        final var concatenated = new ArrayList<>(Arrays.asList(Utils.intToBytes(dataList.size())));
        concatenated.addAll(dataList);
        
        final var splitName = dataFilename.split("\\.");
        final var ext = splitName[splitName.length-1];
        
        concatenated.addAll(Arrays.asList(Utils.stringToBytes(ext)));
        concatenated.add((byte)0);
        
        if(encrypt == null) return method.hide(image, concatenated.stream());
        
        final var encData = encrypt.apply(concatenated);
        final var concatenatedEncData = new ArrayList<>(Arrays.asList(Utils.intToBytes(encData.size())));
        concatenatedEncData.addAll(encData);
        
        return method.hide(image, concatenatedEncData.stream());
    }

    @Override
    public UnhideOutput unhide(Stream<Byte> image, Metadata meta, SteganographerMethod method, Function<List<Byte>,List<Byte>> decrypt) {
        final var unhiddenData = method.unhide(image, meta).toList();
        final List<Byte> decryptedData;
        
        if(decrypt != null) {
            final var dataLen = Utils.intFromBytes(unhiddenData.subList(0,4));
            final var encData = unhiddenData.subList(4, dataLen + 4);
            decryptedData = decrypt.apply(encData);
        } else {
            decryptedData = unhiddenData;
        }
        
        final var dataLen = Utils.intFromBytes(decryptedData.subList(0, 4));
        final var fileData = decryptedData.subList(4, dataLen + 4);
        final var ext = Utils.stringFromBytes(decryptedData.subList(dataLen + 4, decryptedData.size()-2));
        
        return new UnhideOutput(fileData, ext);
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
