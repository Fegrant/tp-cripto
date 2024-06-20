package ar.edu.itba.cripto.group4.steganography;

import ar.edu.itba.cripto.group4.steganography.encryption.Encryption;
import ar.edu.itba.cripto.group4.steganography.encryption.EncryptionImpl;
import ar.edu.itba.cripto.group4.steganography.steganographers.Steganographer;
import ar.edu.itba.cripto.group4.steganography.steganographers.SteganographerImpl;
import ar.edu.itba.cripto.group4.steganography.steganographers.SteganographerMethod;
import ar.edu.itba.cripto.group4.steganography.io.ReaderOutput;
import ar.edu.itba.cripto.group4.steganography.io.ReaderWriter;
import ar.edu.itba.cripto.group4.steganography.io.bmp.BmpReaderWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        ArgumentParser argumentParser = new ArgumentParser(args);
        Encryption encryption = new EncryptionImpl(argumentParser);
        Steganographer steganographer = new SteganographerImpl();
        final ReaderWriter rw = new BmpReaderWriter();

        Path baseImagePath = Path.of("definitive_files/secreto1.bmp");
//        Path baseImagePath = Path.of("test_files/ladoLSBIaesofbsalt0.bmp");

        ReaderOutput ro = rw.readFile(baseImagePath);

        /*
        final var hideInput = Arrays.stream(Utils.stringToBytes("Hola!")).toList().stream();
        final var hideOutput = steganographer.hide(ro.getData(), hideInput, "hola.txt", SteganographerMethod.LSBI, null);

        Path steggedImagePath = Path.of("steg.bmp");
        rw.writeFile(steggedImagePath, hideOutput, ro.getMetadata());

        ro = rw.readFile(steggedImagePath);

         */

        final var unhideOutput = steganographer.unhide(ro.getData(), ro.getMetadata(), SteganographerMethod.LSB4, encryption::decrypt);

        try (FileOutputStream fos = new FileOutputStream("salida" + unhideOutput.extension())){
            unhideOutput.data().forEach(b -> {
                try {
                    fos.write(b);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // rw.writeFile(Path.of("image" + unhideOutput.extension()), unhideOutput.data().stream(), ro.getMetadata());
        
        // rw.writeFile(Path.of("ladoLSB1.bmp"), hideOutput, ro.getMetadata());
        
        
//        File bmpFile = new File(path);
//        boolean isBmpFile = steganographer.analyze(new FileInputStream(bmpFile));
//
//        if (isBmpFile) {
//            System.out.println("Es probable que el archivo BMP contenga datos ocultos");
//        } else {
//            System.out.println("Es poco probable que el archivo BMP contenga datos ocultos");
//        }
    }
}
