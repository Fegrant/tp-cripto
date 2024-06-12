
public class Main {
    public static void main(String[] args) {

        ArgumentParser argumentParser = new ArgumentParser(args);
        System.out.println(argumentParser.getActionType());
        System.out.println(argumentParser.getInputFile());
        System.out.println(argumentParser.getBitmapFile());
        System.out.println(argumentParser.getOutputFile());
        System.out.println(argumentParser.getStegType());
        System.out.println(argumentParser.getEncryptionType());
        System.out.println(argumentParser.getEncryptionMode());
        System.out.println(argumentParser.getPassword());

        
    }
}