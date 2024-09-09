
import java.io.*;
import java.util.*;

public class Huffman {

    /**
     Code
     provided from previous version and modified for 2020.
     */
    public static void encode() throws IOException{
        // initialize Scanner to capture user input
        Scanner sc = new Scanner(System.in);

        // capture file information from user and read file
        System.out.print("Enter the filename to read from/encode: ");
        String f = sc.nextLine();

        // create File object and build text String
        File file = new File(f);
        Scanner input = new Scanner(file).useDelimiter("\\z");
        String text = input.next();

        // close input file
        input.close();

        // initialize Array to hold frequencies (indices correspond to
        // ASCII values)
        int[] freq = new int[256];
        // concatenate/sanitize text String and create character Array
        // nice that \\s also consumes \n and \r
        // we can add the whitespace back in during the encoding phase

        char[] chars = text.replaceAll("\\s", "").toCharArray();

        // count character frequencies
        for(char c: chars)
            freq[c]++;


        //Your work starts here************************************8

        // declaring queues
        Queue<PairTree> S = new PriorityQueue<>();
        Queue<PairTree> T = new PriorityQueue<>();


        for(int i = 0; i<256; i++){
            if(freq[i]!=0){
                // this method of rounding is good enough
                Pair p = new Pair((char)i, Math.round(freq[i]*10000d/chars.length)/10000d);
                PairTree A = new PairTree(p);
                S.add(A);

            }
        }

        //Apply the huffman algorithm here and build the tree ************************************
        //
        //declaring Pairtree object
        PairTree A ;
        PairTree B ;

        //getting first two elements of queue S and storing in A and B
        A = S.poll();
        B = S.poll();

        //creating a new node whose probability is equal to sum of A and B and whose left child is A and right child is B
        Pair p = new Pair('⁂',A.getProb()+B.getProb());
        PairTree P = new PairTree(p);
        P.attachLeft(A);
        P.attachRight(B);
        T.add(P);

        //Removing elements from queue S and storing in queue T
        while(!S.isEmpty()){

            //comparing first tree of queue S and of queue T and storing result in variable compare
            int compare = S.peek().compareTo(T.peek());

            //if probability of first element of S is greater or equal to T and node from T will removed and stored in A
            if(compare>=0){
                A = T.poll();
            }
            //if probability of first element of S is lesser than T and node from S will removed and stored in A
            else if(compare<0){
                A = S.poll();
            }

            //if after removing fist element both queues still have some elements
            if(!S.isEmpty() && !T.isEmpty()){
                //Again comparing first tree of queue S and of queue T and storing result in variable compare
                compare = S.peek().compareTo(T.peek());

                //if probability of first element of S is greater or equal to T and node from T will removed and stored in B
                if(compare>=0){
                    B = T.poll();
                }
                //if probability of first element of S is lesser than T and node from S will removed and stored in B
                else if(compare<0){
                    B = S.poll();
                }
            }
            //if S got empty after removing for first time then node from T will be remove and store in B
            else if(S.isEmpty()) {
                B = T.poll();
            }
            else{
                B = S.poll();
            }
            //creating a new node whose probability is equal to sum of A and B and whose left child is A and right child is B
             p = new Pair('⁂',A.getProb()+B.getProb());
             P = new PairTree(p);
             P.attachLeft(A);
             P.attachRight(B);
             T.add(P);
       }

        //if size of queue T is greater then this loop will continue to run till size of T become 1 and
        //it becomes one single tree and that tree will be known as Huffman tree
        while(T.size()>1){
            A = T.poll();
            B = T.poll();
             p = new Pair('⁂',A.getProb()+B.getProb());
             P = new PairTree(p);
            P.attachLeft(A);
            P.attachRight(B);
            T.add(P);
        }



        PairTree HuffmanTree = T.poll();
        PairTree.levelorder(HuffmanTree);
        //generating codes of by using findEncoding() method
        String[] codes = findEncoding(HuffmanTree);

        //creating and writing in file
        PrintStream huffmanwriter = new PrintStream(new FileOutputStream("Huffman.txt"));
        huffmanwriter.printf("%-20s%-20s%-20s\n","Symbol","Prob","Huffman Code");
        huffmanwriter.println();

        //Creating a queue q and adding HuffmanTree in it
        Queue<PairTree> q = new LinkedList<PairTree>();
        q.add(HuffmanTree);

        while(!q.isEmpty()){
            //removing first element of queue q
            PairTree tmp = q.remove();
            //if tmp.getValue is not equal to character '⁂' only then it will print its data in file
                if(tmp.getData().getValue()!='⁂') {
                    char value = tmp.getData().getValue();
                    huffmanwriter.printf("%-20s%-20s%s",tmp.getData().getValue(),tmp.getData().getProb(),codes[value]);
                    if(!q.isEmpty()){
                        //if q is not empty only then it will create new line character
                        huffmanwriter.printf("\n");                    }
                }

            if (tmp.getLeft() != null)
                q.add(tmp.getLeft());
            if (tmp.getRight() != null)
                q.add(tmp.getRight());
        }
        //print statement
        System.out.println("Codes generated.Printing codes to Huffman.txt");

        String txt="";
        //storing encoded code for text file provided to read from in string txt
        for(int i=0;i<text.length();i++) {
            if (codes[text.charAt(i)] == null)
                txt += " ";
            else {
                txt += "" + codes[text.charAt(i)];
            }
        }
        //Creating and writing in file Encoded.txt
        PrintStream encoded = new PrintStream(new FileOutputStream("Encoded.txt"));
        encoded.printf(txt);

        //print statements
        System.out.println("Printing encoded text to Encoded.txt");
        System.out.println("* * * * *");


    }


   public static void decode()throws IOException{
        // initialize Scanner to capture user input
        Scanner sc = new Scanner(System.in);

        // capture file information from user and read file
        System.out.print("Enter the filename to read from/decode: ");
        String f = sc.nextLine();

        // create File object and build text String
        File file = new File(f);
        Scanner input = new Scanner(file).useDelimiter("\\Z");
        String text = input.next();
        // ensure all text is consumed, avoiding false positive end of
        // input String
        input.useDelimiter("\\z");
        if(input.hasNext())
        text += input.next();


        // close input file
        input.close();

        // capture file information from user and read file
        System.out.print("Enter the filename of document containing Huffman codes: ");
        f = sc.nextLine();

        // create File object and build text String
        file = new File(f);
        input = new Scanner(file).useDelimiter("\\Z");
        String codes = input.next();

        // close input file
        input.close();

        //Your work starts here********************************************

       //initialising scanner
       Scanner Is = new Scanner(codes);
       //consume/discard header row and blank line
       Is.nextLine();
       Is.nextLine();

        //declaring HashMap object to store codes and character
       Map<String,Character> decoder = new HashMap<>();

       while (Is.hasNextLine()){
           char c = Is.next().charAt(0);                //string character

           Is.next();                                   //consume/discard probability
           String code = Is.next();                     //storing code
           decoder.put(code,c);
       }

       String decode="",decodedtext="";
       //checks the code and change the code to character if when found in Mao decoder
       for(int i=0;i<text.length();i++){
           decode += text.charAt(i);
           if(text.charAt(i)==' '){
               decode = "";
               decodedtext += " ";
           }

           if(decoder.containsKey(decode)){
              decodedtext +=   decoder.get(decode);
               decode = "";

           }
       }

       //creating and writing in Decoded.txt
       PrintStream huffmandecoded = new PrintStream(new FileOutputStream("Decoded.txt"));
       huffmandecoded.printf(decodedtext);

       //print statement
       System.out.println("Printing decoded text to Decoded.txt");



   }

    private static String[] findEncoding(PairTree pt){
        // initialize String array with indices corresponding to ASCII values
        String[] result = new String[256];
        // first call from wrapper
        findEncoding(pt, result, "");
        return result;
    }

    private static void findEncoding(PairTree pt, String[] a, String prefix){
        // test is node/tree is a leaf
        if (pt.getLeft()==null && pt.getRight()==null){
            a[pt.getValue()] = prefix;
        }
        // recursive calls
        else{  findEncoding(pt.getLeft(), a, prefix+"0");
            findEncoding(pt.getRight(), a, prefix+"1");
        }
    }


}
