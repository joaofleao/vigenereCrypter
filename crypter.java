import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class crypter {

    private static String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private static double value;
    private static char frequent;

    public static void main(String args[]) throws Exception {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Decifrador de Vigenere");
        System.out.println("- João Flores de Leão -");

        do {
            System.out.println("\n\nDigite o número correspondente a operação que deseja realizado");
            System.out.println("1 - Executar um arquivo");
            System.out.println("2 - Executar todos arquivos (cipher1.txt até cipher30.txt)");
            System.out.println("3 - Sair");
            String option = keyboard.nextLine();

            System.out.println("Digite o número correspondente ao idioma você deseja traduzir");
            System.out.println("1 - Português");
            System.out.println("2 - Inglês");
            String language = keyboard.nextLine();

            if (language.equals("1"))
                value = 0.074;
            else if (language.equals("2"))
                value = 0.066;
            else
                break;

            System.out.println("Digite a letra que deve ser utilizada como a mais frequente do idioma");
            frequent = keyboard.nextLine().charAt(0);

            if (language.equals("1"))
                value = 0.074;
            else if (language.equals("2"))
                value = 0.066;
            else
                break;

            if (option.equals("1")) {
                System.out.println("Insira o nome arquivo voce deseja executar");
                String fileName = keyboard.nextLine();
                decryptFile(fileName);
            } else if (option.equals("2")) {
                System.out.println("Decifrando todos arquivos)");
                decryptAll();
            } else if (option.equals("3")) {
                System.out.println("Finalizando Execução");
                break;
            }

        } while (true);

        keyboard.close();

    }

    public static void decryptFile(String fileName) throws Exception {
        String text = "";

        text = readFile("./cryptedFiles/" + fileName);
        if (text.equals("error"))
            return;

        int keySize = findKeySize(text);

        String key = findKey(keySize, text);

        System.out.println("\n\nKey = " + key + "     Size = " + keySize);
        decrypt(text, key, fileName);
    }

    public static void decryptAll() throws Exception {
        for (int j = 1; j < 32; j++) {
            String fileName = ("cipher" + j + ".txt");
            String text = "";

            text = readFile("./cryptedFiles/" + fileName);
            if (text.equals("error"))
                return;

            int keySize = findKeySize(text);
            String key = findKey(keySize, text);
            System.out.println("FileName = " + fileName + "        Key = " + key + "     Size = " + keySize);
            decrypt(text, key, fileName);

        }
    }

    public static void decrypt(String text, String key, String fileName) {
        try {

            FileWriter myWriter = new FileWriter(
                    "./decryptedFiles/" + fileName.substring(0, fileName.length() - 4) + "-decrypted.txt");
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
            System.out.println("Arquivo salvo na pasta decryptedFiles\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo");
        }
    }

    public static String readFile(String name) {
        String text = "";
        try {
            File file = new File(name);
            BufferedReader br = new BufferedReader(new FileReader(file));
            text = br.readLine();
            br.close();
        } catch (Exception e) {
            System.out.println("Arquivo " + name + " não encontrado");
            return "error";
        }
        return text;
    }

    public static String findKey(int keySize, String text) {
        String key = "";
        for (int i = 0; i < keySize; i++) {
            char letra = split(keySize, i, text);
            int number = letra - frequent;
            if (number < 0)
                number += 26;
            key += (char) alphabet.charAt(number);
        }
        return key;
    }

    public static int findKeySize(String text) {
        for (int i = 1; i <= 30; i++) {
            for (int j = 0; j < i; j++) {
                int[] frequencies = new int[26];
                Arrays.fill(frequencies, 0);

                for (int k = j; k < text.length(); k += i) {
                    int position = (int) text.charAt(k) - 97;
                    frequencies[position] = frequencies[position] + 1;
                }
                double index = findCoincidenceIndex(frequencies);

                if (isClose(index, value))
                    return i;
            }
        }

        return 0;
    }

    public static boolean isClose(double value, double language) {
        double difference = value - language;
        return (difference <= 0.008 && difference > 0);
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