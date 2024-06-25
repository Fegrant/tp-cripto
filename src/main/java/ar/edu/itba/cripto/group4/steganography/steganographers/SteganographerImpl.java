package ar.edu.itba.cripto.group4.steganography.steganographers;

import ar.edu.itba.cripto.group4.steganography.Utils;
import ar.edu.itba.cripto.group4.steganography.io.Metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SteganographerImpl implements Steganographer {
    @Override
    public Stream<Byte> embed(Stream<Byte> image, Metadata meta, Stream<Byte> data, String dataFilename, SteganographerMethod method, Function<List<Byte>, List<Byte>> encrypt) {
        final var dataList = data.toList();
        final var concatenated = new ArrayList<>(Arrays.asList(Utils.intToBytes(dataList.size())));
        concatenated.addAll(dataList);

        final var splitName = dataFilename.split("\\.");
        final var ext = "." + splitName[splitName.length - 1];

        concatenated.addAll(Arrays.asList(Utils.stringToBytes(ext)));
        concatenated.add((byte) 0);

        if (encrypt == null) return method.embed(image, meta, concatenated.stream(), concatenated.size());

        final var encData = encrypt.apply(concatenated);
        final var concatenatedEncData = new ArrayList<>(Arrays.asList(Utils.intToBytes(encData.size())));
        concatenatedEncData.addAll(encData);

        return method.embed(image, meta, concatenatedEncData.stream(), concatenatedEncData.size());
    }

    @Override
    public ExtractOutput extract(Stream<Byte> image, Metadata meta, SteganographerMethod method, Function<List<Byte>, List<Byte>> decrypt) {
        final var unhiddenData = method.extract(image, meta).toList();
        final List<Byte> decryptedData;

        if (decrypt != null) {
            final var dataLen = Utils.intFromBytes(unhiddenData.subList(0, 4));
            if(dataLen < 0) throw new NoHiddenDataException();
            final var encData = unhiddenData.subList(4, dataLen + 4);
            decryptedData = decrypt.apply(encData);
        } else {
            decryptedData = unhiddenData;
        }

        final var dataLen = Utils.intFromBytes(decryptedData.subList(0, 4));
        if (dataLen > decryptedData.size() - 4) throw new NoHiddenDataException();

        final var fileData = decryptedData.subList(4, 4 + dataLen);
        final var stringWithExt = Utils.stringFromBytes(decryptedData.subList(4 + dataLen, decryptedData.size() - 1));
        final String ext = stringWithExt.split("\0", 2)[0];

        return new ExtractOutput(fileData, ext);
    }
}
