package ar.edu.itba.cripto.group4.steganography;

import ar.edu.itba.cripto.group4.steganography.enums.*;
import org.apache.commons.cli.*;

public class ArgumentParser {

    private ActionType actionType;
    private String inputFile;
    private String bitmapFile;
    private String outputFile;
    private StegType stegType;
    private EncryptionType encryptionType;
    private EncryptionMode encryptionMode;
    private String password;

    public ArgumentParser(String[] args) {
        parseArguments(args);
    }

    private void parseArguments(String[] args) {
        Options options = new Options();

        options.addOption("embed", false, "Indicates embedding information");
        options.addOption("extract", false, "Indicates extracting information");
        options.addOption(Option.builder("in").hasArg().desc("Input file to hide or extract").build());
        options.addOption(Option.builder("p").hasArg().desc("Bitmap file to be used as carrier or to extract from").build());
        options.addOption(Option.builder("out").hasArg().desc("Output bitmap file with embedded information or extracted file").build());
        options.addOption(Option.builder("steg").hasArg().desc("Steganography algorithm: LSB1, LSB4, LSBI").build());
        options.addOption(Option.builder("a").hasArg().desc("Encryption algorithm: aes128, aes192, aes256, des").build());
        options.addOption(Option.builder("m").hasArg().desc("Encryption mode: ecb, cfb, ofb, cbc").build());
        options.addOption(Option.builder("pass").hasArg().desc("Password for encryption").build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("embed") && cmd.hasOption("extract")) {
                throw new ParseException("Cannot specify both -embed and -extract.");
            }

            if (!cmd.hasOption("embed") && !cmd.hasOption("extract")) {
                throw new ParseException("Either -embed or -extract must be specified.");
            }

            actionType = cmd.hasOption("embed") ? ActionType.getActionType("embed") : ActionType.getActionType("extract");

            if (cmd.hasOption("embed") && !cmd.hasOption("in")) {
                throw new ParseException("Missing required parameter -in for embedding.");
            }

            if (!cmd.hasOption("p") || !cmd.hasOption("out") || !cmd.hasOption("steg")) {
                throw new ParseException("Missing required parameters.");
            }

            if (cmd.hasOption("pass")) {
                password = cmd.getOptionValue("pass");
                encryptionType = cmd.hasOption("a") ? EncryptionType.getEncryptionType(cmd.getOptionValue("a")) : EncryptionType.AES128;
                encryptionMode = cmd.hasOption("m") ? EncryptionMode.getEncryptionMode(cmd.getOptionValue("m")) : EncryptionMode.CBC;
            } else if (cmd.hasOption("a") || cmd.hasOption("m")) {
                throw new ParseException("Password must be provided if encryption algorithm or mode is specified.");
            }

            inputFile = cmd.getOptionValue("in");
            bitmapFile = cmd.getOptionValue("p");
            outputFile = cmd.getOptionValue("out");
            stegType = StegType.getStegType(cmd.getOptionValue("steg"));

            if (cmd.hasOption("pass")) {
                encryptionType = EncryptionType.getEncryptionType(cmd.getOptionValue("a", "aes128"));
                encryptionMode = EncryptionMode.getEncryptionMode(cmd.getOptionValue("m", "cbc"));
                password = cmd.getOptionValue("pass");
            }

        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            formatter.printHelp("StegoBMP", options);
            System.exit(1); // Exit the program if arguments are invalid
        }
    }

    // Getters for the parsed values
    public ActionType getActionType() {
        return actionType;
    }

    public String getInputFile() {
        return inputFile;
    }

    public String getBitmapFile() {
        return bitmapFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public StegType getStegType() {
        return stegType;
    }

    public EncryptionType getEncryptionType() {
        return encryptionType;
    }

    public EncryptionMode getEncryptionMode() {
        return encryptionMode;
    }

    public String getPassword() {
        return password;
    }
}
