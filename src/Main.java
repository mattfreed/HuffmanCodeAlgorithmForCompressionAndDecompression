
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@SuppressWarnings("unchecked")
public class Main {
    //    BitSet bitArray = new BitSet;
    static HashMap<Character, String> encode = new HashMap<>();
    static int bitLength;

    public static void main(String[] args) throws Exception {
        //if encode do this
        Scanner scanner = new Scanner(System.in);
        System.out.print("Type \"Start\" to begin\n");
        String start = scanner.next();
        if (start.equals("Start")) {

            System.out.print("I am ready to accept your input file and compress it.");
            System.out.print("Please enter the Input file path to compress: ");
            String input = scanner.next();
            System.out.print("Please enter the path of output file: ");
            String output = scanner.next();

            //File file = new File("C:\\Users\\Matt Freed\\Desktop\\HW3\\test.txt");
            File file = new File(input);
            //File compressed = new File(args[2]);
            compress(file, encode, output);

            System.out.print("I am done compressing the file. I am ready to decompress the file and create an original one. ");
            System.out.print("Please enter the Input file path to decompress: ");
            String deinput = scanner.next();
            System.out.print("Please enter the path of output file: ");
            String deoutput = scanner.next();
            //File file2 = new File("C:\\Users\\Matt Freed\\Desktop\\HW3\\output");
            File file2 = new File(deinput);
            decompress(file2, encode, deoutput);
        }
        System.out.print("I am done decompressing the file.");

        // if decode do this
    }


    public static void compress(File file, HashMap encode, String compressed) throws Exception {
        int totalCharacters[] = new int[127];
        //first build the text reader
        BufferedReader br = new BufferedReader(new FileReader(file));


        int character;
        char convert;

        //build the character array
        //assign frequency
        while ((character = br.read()) != -1) {
            //System.out.println(character);
            convert = ((char) character);
            totalCharacters[convert]++;

        }

//        for (int i = 0; i<totalCharacters.length; i++){
//            System.out.printf("%c %d\n", i + ' ', totalCharacters[i]);
//        }

        ArrayList<ArrayList<Integer>> twoDValues = new ArrayList<>();
        twoDValues.add(new ArrayList<Integer>());
        twoDValues.add(new ArrayList<Integer>());


        for (int i = 0; i < totalCharacters.length; i++) {
            if (totalCharacters[i] != 0) {
                twoDValues.get(0).add(totalCharacters[i]);
                twoDValues.get(1).add(i);
            }
        }

        for (int i = 0; i < twoDValues.get(0).size(); i++) {
//            System.out.println(twoDValues.get(0).get(i));
//            System.out.println(twoDValues.get(1).get(i));

            //System.out.printf("%c %d\n", twoDValues.get(1).get(i), twoDValues.get(0).get(i));
        }


        //next build the character tree
        int initCapacity = 5;
        PriorityQueue<Node> pq = new PriorityQueue<>(initCapacity, new MyComparator());

        for (int i = 0; i < twoDValues.get(0).size(); i++) {
            Node n = new Node();
            n.frequency = twoDValues.get(0).get(i);
            n.letter = ((char) twoDValues.get(1).get(i).intValue());
            pq.add(n);
        }
        //make root node
        Node root = null;

        while (pq.size() > 1) {
            // first min extract.
            Node x = pq.peek();
            pq.poll();

            // second min extract.
            Node y = pq.peek();
            pq.poll();

            // new node f which is equal
            Node f = new Node();

            // to the sum of the frequency of the two nodes
            // assigning values to the f node.
            f.frequency = x.frequency + y.frequency;
            f.letter = '-';

            // first extracted node as left child.
            f.left = x;

            // second extracted node as the right child.
            f.right = y;

            // marking the f node as the root node.
            root = f;
            //System.out.println(root.toString());

            // add this node to the priority-queue.
            pq.add(f);
        }
        // print the codes by traversing the tree
        printCode(root, "", encode);
        outputToFile(encode, file, compressed);

        //N O T E S
        // output file has to be written to bytes
        //bit vector
        //take bits, convert to bytes, then output buffer for writting to disk

        //write to bit vector by traversing file again, convert to bytes, write to output file
        //from bits, to bytes, to output file (writing to file has to be bytes)


    }


    public static void printCode(Node root, String s, HashMap encode)
    {

        // base case; if the left and right are null
        // then its a leaf node and we print
        // the code s generated by traversing the tree.
        if (root.left == null && root.right == null) {

//            int bitConvert = (Integer.valueOf(s));
            //BigInteger bInt = new BigInteger(s, 2);

            //System.out.println(root.letter + ":" + s);
            encode.put(root.letter, s);


//            System.out.println("Initial pattern in bits1: ");
            //System.out.println(root.letter + ":" + s + ":" + bInt.toString(2));
            //mapping.put(root.letter, bInt);

            return;
        }

        // if we go to left then add "0" to the code.
        // if we go to the right add"1" to the code.

        // recursive calls for left and
        // right sub-tree of the generated tree.
        printCode(root.left, s + "0",encode);
        printCode(root.right, s + "1",encode);
    }


    public static class Node {
        int frequency;
        char letter;
        public Node left;
        public Node right;

        public Node(){
        }

        @Override
        public String toString() {
            return "Freq: " + frequency + "; Letter: " + letter;
        }
    }
    static class MyComparator implements Comparator<Node> {
        public int compare(Node x, Node y)
        {

            return x.frequency - y.frequency;
        }
    }


    public static void outputToFile(HashMap encode, File file, String compressed) throws Exception {
        //read through file
        //find each character in hashtable and write to bitvector. write bitvector to
        BufferedReader br2 = new BufferedReader(new FileReader(file));
        int character2;
        char convert2;
        String totalCode = "";

        while ((character2 = br2.read()) != -1) {
            //System.out.println(character);
            convert2 = ((char) character2);
            //System.out.println(encode.get(convert2));
            totalCode = totalCode + encode.get(convert2);
            //bitLength++;

        }
        bitLength = totalCode.length();
        //System.out.println(totalCode);
        //System.out.println(fromString(totalCode));
        //BitSet bitArray = fromString(totalCode);
        //System.out.println(bitArray);

//        BitSet bitArray = new BitSet();
//        for (int i = 0; i < totalCode.length(); i++){
//            char c = totalCode.charAt(i);
//            int inputValue = (int)c;
//            System.out.println(inputValue);
//            if (inputValue==0)
//                bitArray.set(i,0);
//        }
        //System.out.println(toByteArray(bitArray));
        //toByteArray(fromString(totalCode));

//        try (FileOutputStream fos = new FileOutputStream("C:\\Users\\Matt Freed\\Desktop\\HW3\\output.txt")) {
//            fos.write(toByteArray(fromString(totalCode)));
//            //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
//        }
        //FileOutputStream fos = new FileOutputStream("C:\\Users\\Matt Freed\\Desktop\\HW3\\output");
        FileOutputStream fos = new FileOutputStream(compressed);
        fos.write(toByteArray(fromString(totalCode)));
        fos.close();



    }

    private static BitSet fromString(String binary) {
        BitSet bitset = new BitSet(binary.length());
        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) == '1') {
                bitset.set(i,true);
            }
            else{
                bitset.set(i,false);
            }
        }
        return bitset;
    }

    public static byte[] toByteArray(BitSet bits) {
        byte[] bytes = new byte[(bits.length() + 7) / 8];
        for (int i=0; i<bits.length(); i++) {
            if (bits.get(i)) {
                //bytes[bytes.length-i/8-1] |= 1<<(i%8);
                //bytes[i / 8] |= 1 << (7 - i % 8);
                bytes[i / 8] |= 128 >> (i % 8);
            }
        }
        return bytes;
    }


    public static void decompress (File file, HashMap decode, String decompress) throws Exception{
        byte[] contents = Files.readAllBytes(file.toPath());
        //System.out.println(binaryToText(contents));
        int length = binaryToText(contents).length();
        char check;
        String checkString = "";
        //FileOutputStream fos = new FileOutputStream("C:\\Users\\Matt Freed\\Desktop\\HW3\\output2.txt");
        //FileOutputStream fos = new FileOutputStream(decompress);

        String finalString = "";
        //System.out.println("length input:" + length);
        //System.out.println("length:" + bitLength);
        for (int i = 0; i < bitLength;i++){
            check = binaryToText(contents).charAt(i);
            checkString = checkString+Character.toString(check);

            if (encode.containsValue(checkString)){ //&& finalString.length() <= bitLength)
//                System.out.println(getKeyByValue(checkString));
                finalString = finalString + getKeyByValue(checkString);
                checkString="";
            }


        }


        //BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Matt Freed\\Desktop\\HW3\\output2.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter(decompress));
        writer.write(finalString);
        writer.close();

    }


    public static String binaryToText(byte[] contents){
        //String s = new String(contents);

        StringBuilder decompressed = new StringBuilder();
        for(int i = 0; i <contents.length; i++){
            String s = String.format("%8s", Integer.toBinaryString((contents[i] & 0xFF))).replace(' ','0');
//            System.out.println(s);
            decompressed.append(new StringBuilder(s).toString());
        }
        String done = decompressed.toString();
        return done;
//        byte[] bytes = asciiString.getBytes();
//        StringBuilder binary = new StringBuilder();
//        for (byte b : bytes)
//        {
//            int val = b;
//            for (int i = 0; i < 8; i++)
//            {
//                binary.append((val & 128) == 0 ? 0 : 1);
//                val <<= 1;
//            }
//            // binary.append(' ');
//        }
//        return binary.toString();
// }
    }

    public static char getKeyByValue(String value) {
        for (Map.Entry<Character, String> entry : encode.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return '-';
    }

}

