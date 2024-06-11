package ar.edu.itba.cripto.group4.esteganography;

public class ArgumentParser {
    public ArgumentParser(String[] args) {
        if (args.length == 0) {
            System.out.println("No arguments provided");
        } else {
            System.out.println("Arguments provided");
        }
    }
}
