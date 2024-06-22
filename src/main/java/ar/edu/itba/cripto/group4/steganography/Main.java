package ar.edu.itba.cripto.group4.steganography;

import ar.edu.itba.cripto.group4.steganography.encryption.Encryption;
import ar.edu.itba.cripto.group4.steganography.encryption.EncryptionImpl;
import ar.edu.itba.cripto.group4.steganography.io.ReaderException;
import ar.edu.itba.cripto.group4.steganography.io.ReaderOutput;
import ar.edu.itba.cripto.group4.steganography.io.ReaderWriter;
import ar.edu.itba.cripto.group4.steganography.io.WriterException;
import ar.edu.itba.cripto.group4.steganography.io.bmp.BmpReaderWriter;
import ar.edu.itba.cripto.group4.steganography.io.generic.GenericReaderWriter;
import ar.edu.itba.cripto.group4.steganography.steganographers.Steganographer;
import ar.edu.itba.cripto.group4.steganography.steganographers.SteganographerImpl;
import ar.edu.itba.cripto.group4.steganography.steganographers.SteganographerMethod;
import ar.edu.itba.cripto.group4.steganography.steganographers.UnhideOutput;

import java.nio.file.Path;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        try {
            final ArgumentParser argumentParser = new ArgumentParser(args);

            Encryption encryption = null;
            if (argumentParser.getPassword() != null)
                encryption = new EncryptionImpl(argumentParser);

            final Path bitmapFile = Path.of(argumentParser.getBitmapFile());
            final ReaderWriter bmpRw = new BmpReaderWriter();
            final ReaderWriter genRw = new GenericReaderWriter();
            final ReaderOutput imageRo = bmpRw.readFile(bitmapFile);

            Steganographer steganographer = new SteganographerImpl();
            SteganographerMethod method;
            switch (argumentParser.getStegType()) {
                case LSB4 -> method = SteganographerMethod.LSB4;
                case LSBI -> method = SteganographerMethod.LSBI;
                default -> method = SteganographerMethod.LSB1;
            }

            final var outName = argumentParser.getOutputFile();

            switch (argumentParser.getActionType()) {
                case EMBED -> {
                    final var filename = argumentParser.getInputFile();
                    final var ro = genRw.readFile(Path.of(filename));
                    final Stream<Byte> hideOutput = steganographer.hide(imageRo.getData(), ro.getData(), filename, method, encryption != null ? encryption::encrypt : null);

                    bmpRw.writeFile(Path.of(outName + ".bmp"), hideOutput, imageRo.getMetadata());
                }
                case EXTRACT -> {
                    final UnhideOutput unhideOutput = steganographer.unhide(imageRo.getData(), imageRo.getMetadata(), method, encryption != null ? encryption::decrypt : null);
                    final var path = Path.of(outName + unhideOutput.extension());

                    genRw.writeFile(path, unhideOutput.data().stream(), null);
                }
            }
        } catch(ReaderException | WriterException e) {
            System.err.println(e.getMessage());
        }
    }
}
