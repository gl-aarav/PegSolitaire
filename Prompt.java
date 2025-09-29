
// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Prompt {
    private static InputStreamReader streamReader;
    private static BufferedReader bufReader;

    public Prompt() {
    }

    public static String getString(String var0) {
        System.out.print(var0 + " -> ");
        String var1 = "";

        try {
            var1 = bufReader.readLine();
        } catch (IOException var3) {
            System.err.println("ERROR: BufferedReader could not read line");
        }

        return var1;
    }

    public static char getChar(String var0) {
        new String("");
        boolean var2 = true;

        String var1;
        do {
            var1 = getString(var0);
        } while (var1.length() != 1);

        char var3 = var1.charAt(0);
        return var3;
    }

    public static int getInt(String var0) {
        boolean var1 = false;
        new String("");
        int var3 = 0;

        do {
            var1 = false;
            String var2 = getString(var0);

            try {
                var3 = Integer.parseInt(var2);
            } catch (NumberFormatException var5) {
                var1 = true;
            }
        } while (var1);

        return var3;
    }

    public static int getInt(String var0, int var1, int var2) {
        boolean var3 = false;

        int var4;
        do {
            do {
                var4 = getInt(var0 + " (" + var1 + " - " + var2 + ")");
            } while (var4 < var1);
        } while (var4 > var2);

        return var4;
    }

    public static double getDouble(String var0) {
        boolean var1 = false;
        new String("");
        double var3 = 0.0;

        do {
            var1 = false;
            String var2 = getString(var0);

            try {
                var3 = Double.parseDouble(var2);
            } catch (NumberFormatException var6) {
                var1 = true;
            }
        } while (var1);

        return var3;
    }

    public static double getDouble(String var0, double var1, double var3) {
        double var5 = 0.0;
        String var7 = String.format("%.2f", var1);
        String var8 = String.format("%.2f", var3);

        do {
            do {
                var5 = getDouble(var0 + " (" + var7 + " - " + var8 + ")");
            } while (var5 < var1);
        } while (var5 > var3);

        return var5;
    }

    static {
        streamReader = new InputStreamReader(System.in);
        bufReader = new BufferedReader(streamReader);
    }
}
