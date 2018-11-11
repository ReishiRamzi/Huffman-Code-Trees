/*
 *    A program to decode a file using Huffman Code Algorithm.
 */

import java.io.*;

import org.graalvm.compiler.api.replacements.Snippet.NonNullParameter;

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
	}
	
	/*
	*/

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
		return;// stub
		
	}
	
	/*
	*	readTree() - reads the code tree from the file
	*	uses the bit reader to construct and return the tree
	*/

	public Node readTree(Node node)
	{
		
		if (bitr.readBit() == 0)
		{
			byte nextByte = bitr.nextByte();
			Node nNode = new Node(nextByte);
			return nNode;
		}
		else 
		{
			Node n = new Node();
			n.lchild = readTree(n.lchild);
			n.rchild = readTree(n.rchild);
			n.lchild.parent = n;
			n.rchild.parent = n;
			return n;
		}
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
            return 0; // stub
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
