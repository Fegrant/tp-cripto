package ar.edu.itba.cripto.group4.steganography;

import ar.edu.itba.cripto.group4.steganography.encryption.Encryption;
import ar.edu.itba.cripto.group4.steganography.encryption.EncryptionImpl;
import ar.edu.itba.cripto.group4.steganography.steganographers.Steganographer;
import ar.edu.itba.cripto.group4.steganography.steganographers.SteganographerImpl;
import ar.edu.itba.cripto.group4.steganography.steganographers.SteganographerMethod;
import ar.edu.itba.cripto.group4.steganography.io.ReaderOutput;
import ar.edu.itba.cripto.group4.steganography.io.ReaderWriter;
import ar.edu.itba.cripto.group4.steganography.io.bmp.BmpReaderWriter;
import ar.edu.itba.cripto.group4.steganography.steganographers.UnhideOutput;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        ArgumentParser argumentParser = new ArgumentParser(args);

        Encryption encryption = null;
        if (argumentParser.getPassword() != null)
            encryption = new EncryptionImpl(argumentParser);

        final Path bitmapFile = Path.of(argumentParser.getBitmapFile());
        final ReaderWriter rw = new BmpReaderWriter();
        final ReaderOutput imageRo = rw.readFile(bitmapFile);

        Steganographer steganographer = new SteganographerImpl();
        SteganographerMethod method = SteganographerMethod.LSB1;
        switch(argumentParser.getStegType()) {
            case LSB1 -> method = SteganographerMethod.LSB1;
            case LSB4 -> method = SteganographerMethod.LSB4;
            case LSBI -> method = SteganographerMethod.LSBI;
        }

        String outputFile = argumentParser.getOutputFile();

        switch (argumentParser.getActionType()) {
            case EMBED -> {
                final String filename = argumentParser.getInputFile();
                final Stream<Byte> hideOutput = steganographer.hide(imageRo.getData(), readFile(filename), filename, method, encryption != null ? encryption::encrypt : null);
                writeFile(hideOutput.toList(), outputFile);
            }
            case EXTRACT -> {
                final UnhideOutput unhideOutput = steganographer.unhide(imageRo.getData(), imageRo.getMetadata(), method, encryption != null ? encryption::decrypt : null);
                writeFile(unhideOutput.data(), outputFile + unhideOutput.extension());
            }
        }
    }

    private static Stream<Byte> readFile(String file) {
        // TODO: Leer archivo y devolver bytes
        return new ArrayList<Byte>().stream();
    }

    private static void writeFile(List<Byte> output, String outputFile) {
        byte[] outputBytes = new byte[output.size()];
        for (int i = 0; i < output.size(); i++) {
            Byte b = output.get(i);
            outputBytes[i] = b != null ? b : 0;
        }

        try (FileOutputStream fos = new FileOutputStream(outputFile)){
            fos.write(outputBytes);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
