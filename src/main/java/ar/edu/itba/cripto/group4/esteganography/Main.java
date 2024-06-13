package ar.edu.itba.cripto.group4.esteganography;

import ar.edu.itba.cripto.group4.esteganography.estaganographers.Esteganographer;
import ar.edu.itba.cripto.group4.esteganography.estaganographers.EsteganographerImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ArgumentParser argumentParser = new ArgumentParser(args);
        Esteganographer esteganographer = new EsteganographerImpl();

        String path = "test_files/lado.bmp";
        File bmpFile = new File(path);
        boolean isBmpFile = esteganographer.analyze(new FileInputStream(bmpFile));

        if (isBmpFile) {
            System.out.println("Es probable que el archivo BMP contenga datos ocultos");
        } else {
            System.out.println("Es poco probable que el archivo BMP contenga datos ocultos");
        }
    }
}
