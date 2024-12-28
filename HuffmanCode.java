import java.util.*;
import java.io.*;

public class HuffmanCode {
    HuffmanNode overallRoot;

    // constructor 
    public HuffmanCode(int[] freqArr) {
        Queue<HuffmanNode> priority = new PriorityQueue<>();

        // Add frequencies and characters to Priority Queue
        for (int i = 0; i < freqArr.length; i++) {
            if (freqArr[i] > 0) {
                HuffmanNode temp = new HuffmanNode(freqArr[i], i);
                priority.add(temp);
            }
        }

        while (priority.size() > 1) {
            HuffmanNode node1 = priority.remove();
            HuffmanNode node2 = priority.remove();

            HuffmanNode parent = new HuffmanNode(node1.frequency + node2.frequency, node1, node2);
            priority.add(parent);
        }

        this.overallRoot = priority.remove();
    }

    public HuffmanCode(Scanner input) {
        this.overallRoot = new HuffmanNode();
        while (input.hasNextLine()) {
            int character = Integer.parseInt(input.nextLine());
            String path = input.nextLine();
            this.overallRoot = constructorHelper(overallRoot, character, path);
        }
    }

    private HuffmanNode constructorHelper(HuffmanNode root, int character, String path) {
        if (root == null) {
            root = new HuffmanNode();
        }

        if (path.length() == 0) {
            return new HuffmanNode(0, character);
        } else {
            char direction = path.charAt(0);
            if (direction == '0') {
                root.left = constructorHelper(root.left, character, path.substring(1));
            } else if (direction == '1') {
                root.right = constructorHelper(root.right, character, path.substring(1));
            }
            return root;
        }
    }

    public void save(PrintStream output) {
        save(output, overallRoot, "");
    }

    private void save(PrintStream output, HuffmanNode root, String path) {
        if (root != null) {
            if (root.left == null && root.right == null) {
                output.println(root.character);
                output.println(path);
            } else {
                save(output, root.left, path + "0");
                save(output, root.right, path + "1");
            }
        }
    }

    public void translate(BitInputStream input, PrintStream output) {
        HuffmanNode current = overallRoot;
        
        while (input.hasNextBit()) {
            int currBit = input.nextBit();
            
            if (currBit == 0) {
                current = current.left;
            } else if (currBit == 1) {
                current = current.right;
            }
            
            if (current.left == null && current.right == null) {
                output.write(current.character);
                current = overallRoot;
            }
        }
    }
    
    

    private static class HuffmanNode implements Comparable<HuffmanNode> {
        public final int frequency;
        public final int character;
        public HuffmanNode left;
        public HuffmanNode right;

        public HuffmanNode(int freq, int character) {
            this.frequency = freq;
            this.character = character;
        }

        // use this one for parent nodes that don't have a character
        public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
            this.frequency = frequency;
            this.left = left;
            this.right = right;
            this.character = -1;
        }

        public HuffmanNode() {
            this(-1, null, null);
        }

        public int compareTo(HuffmanNode other) {
            return this.frequency - other.frequency;
        }
    }
}
