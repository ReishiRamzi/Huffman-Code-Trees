/*
 *    A program to decode a file using Huffman Code Algorithm.
 */

import java.io.*;
import java.nio.file.Files;

public class HDecode {
	
	private Node root = null;      // Root of the Huffman Code Tree.
	
	private String inputFilename;
	private int fileSize;          // The number of bytes 
	                               // in the original file.

	private BitReader bitr;
	private FileOutputStream outF = null;
	
	public static void main(String[] args)
			throws FileNotFoundException, IOException
	{
		HDecode decoder = new HDecode(args[0]);  // Construct a Huffman Decoder
		
		// decode the contents
		decoder.decode();
	}

	/* 
	*	HDecode() - Takes an input file and decodes the file,
	*	using the number of characters (bytes 1 - 4), 
	*	the code tree starting on the 5th byte,
	*	and the compressed file contents afterwards
	*/

	public HDecode(String inputFilename)
	{
		this.inputFilename = inputFilename;
	
	}
	
	/*
	*	decode() - reads the compressed file
	*	reconstructs the tree and reads the encoded bits 
	*	to reconstruct the original file
	*/
	
	public void decode()
	{

		try {
			outF = new FileOutputStream(inputFilename + ".orig");
			// read the first 4 bytes to get the file size

			// construct our bit reader and output file
			bitr = new BitReader(inputFilename);
			fileSize = bitr.readInt();
			System.out.println(fileSize);
			root = readTree(bitr);	
			printTree();

			// declare a new node
			Node currentNode = new Node();
			
			boolean isleaf;

				for (int i = 0; i < fileSize; i++) {
					// read bits from the compressed file
					
					// start at the root
					currentNode = root;
					isleaf = false;
					// no children means leaf has been reached
					// use them to descend code tree until leaf is reached
					while ( !isleaf ) {
						int bit = bitr.readBit();
						if (bit == -1) return;
						System.out.println("bit:  " + bit + "\n");

						// if it's a 0, move left	
						if (bit == 0) {
							currentNode = currentNode.lchild;
						}
						// if a 1, move right
						else if (bit == 1)
						{
							currentNode = currentNode.rchild;
						}
						if (currentNode.lchild == null && currentNode.rchild == null)
						{
						
						isleaf = true;
						// once at the leaf, write data value in the leaf to output file
						// to recreate the original file					
						outF.write(currentNode.data);
						System.out.println("current node data: " + currentNode.data);	
						}
						//bit = bitr.readBit();	
					}

				}
			
			// close the files
			bitr.close();
			outF.close();
		}
		catch (FileNotFoundException e) {
			System.out.printf("Error opening file %s\n", inputFilename);
			System.exit(0);
		}
		catch (IOException e) { 
			System.out.printf("IOException reading from: %s\n", inputFilename);
			System.exit(0);
		}
	
	}
	
	/*
	*	readTree() - reads the code tree from the file
	*	uses the bit reader to construct and return the root of the tree
	*/

	public Node readTree(BitReader bitr)
	{
		int bit = bitr.readBit();
		if (bit == 0)
		{			
			Node nNode = new Node();
			byte nextByte = (byte) bitr.readByte(); 
			nNode.data = nextByte;
			return nNode;
		}
		else 
		{
			Node lTree = readTree(bitr);
			Node rTree = readTree(bitr);
			Node nNode = new Node();
			nNode.lchild = lTree;
			nNode.rchild = rTree;
			lTree.parent = nNode;
			rTree.parent = nNode;
			return nNode;
		}
	}
	
		/*
	 *   printTree() - Print the Huffman Code Tree to
	 *                 standard output.
	 */


	public void printTree()
	{
		rPrintTree(root,0);
	}

	/*
	 *    rPrintTree() - the usual quick recursive method to print a tree.
	 */

	public void rPrintTree(Node r, int level)
	{

		if (r == null)          // Empty tree.
			return;

		rPrintTree(r.rchild, level + 1);   // Print the right subtree.

		for (int i = 0; i < level; i++)
			System.out.print("         ");

		if (r.data > (byte) 31)
			System.out.printf("%c-%d\n", (char) r.data, r.frequency);
		else
			System.out.printf("%c-%d\n", '*', r.frequency);

		rPrintTree(r.lchild, level + 1);
	}
	
	public class Node implements Comparable<Node>
	{
		byte data;           // A byte of data - stored in an Integer.
		Node lchild;         // Left child pointer.
		Node rchild;         // Right child pointer.
		Node parent;         // Pointer to parent node.
		Integer frequency;   // Frequency the data within
		                     // a file being encoded.
		/*
		 *   Basic node constructor.
		 */
		
		public Node()
		{
			data = 0;          // Each Huffman Code Tree node
			lchild = null;     // contains data, pointers to
			rchild = null;     // children and parent nodes
			parent = null;     // plus a frequency count
			frequency = 0;     // associated with the data.
		}
		
		
		/*
		 *   Constructor specifying all values
		 *   of the node instance variables.
		 */
		
		public Node(byte data, Node lchild, Node rchild,
				               Node parent, int frequency)
		{
			this.data = data;
			this.lchild = lchild;
			this.rchild = rchild;
			this.parent = parent;
			this.frequency = frequency;
		}
		
		
		/*
		 *    compareTo() - Compare two frequency values.  We want Nodes
		 *                  with lower frequencies to have higher priority
		 *                  in the priority queue.
		 *                  
		 */
		
		public int compareTo(Node other)
		{
			// if the old node is less
			if (other.frequency > this.frequency) return 1;
			else return 0;
		}
		
		public String toString()
		{
			char ch = (char) this.data;
			
			String str = "byte: " + data + "  char: ";
			
			if (data > (byte) 31)
				str = str + (char) data + "  freq: " + frequency;
			else
				str = str + " " + "  freq: " + frequency;
			
			return str;
		}
		
	}
}
