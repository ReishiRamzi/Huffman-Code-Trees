/*
 *    A program to decode a file using Huffman Code Algorithm.
 */

import java.io.*;

public class HDecode {
	// Root of the Huffman Code Tree.
	private Node root = null;
	private String inputFilename;
	// Declare the number of bytes in the original file
	private int fileSize;
	// Declare a BitReader read bits from the file
	private BitReader bitr;
	// Initialize an output file stream to write decoded file to
	private FileOutputStream outF = null;
	
	public static void main(String[] args)
			throws FileNotFoundException, IOException
	{
		// Construct a Huffman Decoder
		// from file name passed through command line
		HDecode decoder = new HDecode(args[0]); 
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
		// initialize global file name
		this.inputFilename = inputFilename;
	}
	
	/*
	*	decode() - reads the compressed file
	*	reconstructs the tree and reads the encoded bits 
	*	to reconstruct the original file
	*/
	
	public void decode()
	{
		// try / catch file read/write errors
		try
		{
			// split the file name on period
			String[] newFileNameArr =  inputFilename.split("\\.");
			// take the first two parts (filename and file type) and append .orig
			String newFileName = newFileNameArr[0] + "." + newFileNameArr[1] + ".orig";
			// make a new output file stream
			outF = new FileOutputStream(newFileName);
			// construct our bit reader and output file
			bitr = new BitReader(inputFilename);
			// read first 4 bytes as int to get encoded size of file in characters
			fileSize = bitr.readInt();
			// Initialize the root of the tree with the following bits
			root = readTree(bitr);	
			// initialize a new node
			Node currentNode = new Node();
			// declare the flag for finding a leaf node
			boolean isleaf;

			// loop through until all the characters have been decoded
			for (int i = 0; i < fileSize; i++)
			{
				// start at the root
				currentNode = root;
				isleaf = false;
				// no children means leaf has been reached
				// use them to descend code tree until leaf is reached
				while (!isleaf)
				{
					// read bits from the compressed file
					int bit = bitr.readBit();
					// if end of file, bit reader returns -1, escape
					if (bit == -1) return;

					// if a 0, move down left	
					if (bit == 0) {
						currentNode = currentNode.lchild;
					}
					// if a 1, move down right
					else if (bit == 1)
					{
						currentNode = currentNode.rchild;
					}
					// once at the leaf
					if (currentNode.lchild == null && currentNode.rchild == null)
					{
					// set leaf flag to true to signal next bit is for the next character
					isleaf = true;
					// write data value in leaf to output file
					outF.write(currentNode.data);
					}
				}
			}
			
			// close the files
			bitr.close();
			outF.close();
		}
		// catch exceptions
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
	*	readTree() - recursively reads the code tree from the file
	*	uses the bit reader to construct and return the root of the tree
	*/

	public Node readTree(BitReader bitr)
	{
		// read the first bit
		int bit = bitr.readBit();
		// if a 0, we have a leaf
		if (bit == 0)
		{
			// Initialize a new node
			Node nNode = new Node();
			// store the following 8 bits as a byte
			byte nextByte = bitr.readByte();
			// store the byte as data on the node
			nNode.data = nextByte;
			// return the node
			return nNode;
		}
		else 
		{
			// read the left tree
			Node lTree = readTree(bitr);
			// read the right tree
			Node rTree = readTree(bitr);
			// Initialize a new node
			Node nNode = new Node();
			// set its children
			nNode.lchild = lTree;
			nNode.rchild = rTree;
			// set the new node as parent to the left and right trees
			lTree.parent = nNode;
			rTree.parent = nNode;
			// return the node
			return nNode;
		}
	}
	
	 /*
     *    Node - an inner class to represent a node of
     *           a Huffman Code Tree.
     */
	
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
