package org.xel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        if (args.length < 1) {
            System.out.println("No file was given as an argument..!");
            System.exit(1);
        }
        String fileName = args[0];
        Scanner scanner = null;
        try {
            String elasticPL = new String(Files.readAllBytes(Paths.get(fileName)));

            String c = CodeGetter.convert(elasticPL);

            System.out.println(c);

        } catch (IOException e) {
            System.out.println("File not found: " + fileName);
            System.exit(1);
        } catch (Exceptions.SyntaxErrorException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            if(scanner!=null)
                scanner.close(); // Put this call in a finally block
        }
    }
}
