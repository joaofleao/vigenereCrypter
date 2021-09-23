import java.io.*;
import java.util.Arrays;

public class crypter {

    private static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String args[]) throws Exception {
        String text = readFile("./cipher10.txt");
        int keySize = findKeySize(text);

        String key = "";
        for (int i = 0; i < keySize; i++) {
            char letra = split(keySize, i, text);
            key += (char) (letra - 'e' + 97);
        }

        System.out.println("KeySize = " + keySize);
        System.out.println("Key = " + key);
        decrypt(text, key);
    }

    public static void decrypt(String text, String key) {
        try {
            FileWriter myWriter = new FileWriter("result.txt");
            int j = 0;
            for (int i = 0; i < text.length(); i++) {
                if (j == key.length())
                    j = 0;
                int condition = text.charAt(i) - key.charAt(j);
                if (condition >= 0)
                    myWriter.write(alphabet.charAt(condition));
                else
                    myWriter.write(alphabet.charAt(26 + condition));

                j++;
            }
            System.out.println("Arquivo salvo");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo");
        }
    }

    public static String readFile(String name) throws Exception {
        File file = new File(name);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String text = br.readLine();
        br.close();
        return text;
    }

    public static int findKeySize(String text) {
        for (int i = 1; i <= 21; i++) {
            for (int j = 0; j < i; j++) {
                int[] frequencies = new int[26];
                Arrays.fill(frequencies, 0);

                for (int k = j; k < text.length(); k += i) {
                    int position = (int) text.charAt(k) - 97;
                    frequencies[position] = frequencies[position] + 1;
                }
                double index = findCoincidenceIndex(frequencies);

                if (isClose(index, 0.074))
                    return i;
            }
        }

        return 0;
    }

    public static boolean isClose(double value, double language) {
        double difference = value - language;
        return (difference <= 0.008 && difference >= -0.008);
    }

    public static double findCoincidenceIndex(int[] frequencies) {
        long sum = 0;
        long totalSize = 0;
        for (int i = 0; i < frequencies.length; i++) {
            long number = frequencies[i];
            if (number > 0) {
                sum += number * (number - 1);
                totalSize += number;
            }
        }
        return sum / (double) (totalSize * (totalSize - 1));
    }

    public static char split(int number, int index, String text) {
        int[] frequencies = new int[26];
        Arrays.fill(frequencies, 0);
        for (int k = index; k < text.length(); k += number) {
            int position = (int) text.charAt(k) - 97;
            frequencies[position] = frequencies[position] + 1;
        }
        return mostUsed(frequencies);
    }

    public static char mostUsed(int[] array) {
        int biggest = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > array[biggest])
                biggest = i;
        }

        return (char) (biggest + 97);
    }
}