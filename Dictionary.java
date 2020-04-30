/* Modified based on https://www.geeksforgeeks.org/avl-tree-set-1-insertion/ */
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class Node {
	int height;
	String key;
	//Obviously added in
	Word term;
	Node left;
  Node right;
	Node(String x) {
		key = x;
		//Added in
		term = new Word(x);
		height = 1;
	}

	//OVERLOADED Constructor
	Node(String x, String def) {
		key = x;
		term = new Word(x);
		term.addDefinition(def);
		height = 1;
	}
}

class Dictionary {
	Node root;

	int height(Node N) {
		if (N == null)
			return 0;
		return N.height;
	}

	int max(int a, int b) {
		return (a > b) ? a : b;
	}

	Node rightRotate(Node y) {
		Node x = y.left;
		Node T2 = x.right;
		// Perform rotation
		x.right = y;
		y.left = T2;
		// Update heights
		y.height = max(height(y.left), height(y.right)) + 1;
		x.height = max(height(x.left), height(x.right)) + 1;
		// Return new root
		return x;
	}

	Node leftRotate(Node x) {
		Node y = x.right;
		Node T2 = y.left;
		// Perform rotation
		y.left = x;
		x.right = T2;
		// Update heights
		x.height = max(height(x.left), height(x.right)) + 1;
		y.height = max(height(y.left), height(y.right)) + 1;
		// Return new root
		return y;
	}

	// Get Balance factor of node N
	int getBalance(Node N) {
		if (N == null)
			return 0;
		return height(N.left) - height(N.right);
	}

	//THIS IS WHERE THE EDITS BEGIN FOR DATA TYPES
	Node insert(Node node, String key) {
		/* 1. Perform the normal BST insertion */
		if (node == null)
			return (new Node(key));

		if (key.compareTo(node.key) < 0)
			node.left = insert(node.left, key);
		else if (key.compareTo(node.key) > 0)
			node.right = insert(node.right, key);
		else // Duplicate keys not allowed
			return node;

		/* 2. Update height of this ancestor node */
		node.height = 1 + max(height(node.left), height(node.right));

		/* 3. Get the balance factor of this ancestor
			node to check whether this node became
			unbalanced */
		int balance = getBalance(node);

		// If this node becomes unbalanced, then there
		// are 4 cases Left Left Case
		if (balance > 1 && key.compareTo(node.left.key) < 0)
			return rightRotate(node);

    // Left Right Case
    if (balance > 1 && key.compareTo(node.left.key) > 0) {
      node.left = leftRotate(node.left);
      return rightRotate(node);
    }

		// Right Right Case
		if (balance < -1 && key.compareTo(node.right.key) > 0)
			return leftRotate(node);

		// Right Left Case
		if (balance < -1 && key.compareTo(node.right.key) < 0) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}

		/* return the (unchanged) node pointer */
		return node;
	}

	//OVERLOADED METHOD
	Node insert(Node node, Word key) {
		/* 1. Perform the normal BST insertion */
		if (node == null)
			return (new Node(key.toString(), key.getDefinition()));

		if (key.toString().compareTo(node.key.toString()) < 0)
			node.left = insert(node.left, key);
		else if (key.toString().compareTo(node.key.toString()) > 0)
			node.right = insert(node.right, key);
		else // Duplicate keys not allowed
			return node;

		/* 2. Update height of this ancestor node */
		node.height = 1 + max(height(node.left), height(node.right));

		/* 3. Get the balance factor of this ancestor
			node to check whether this node became
			unbalanced */
		int balance = getBalance(node);

		// If this node becomes unbalanced, then there
		// are 4 cases Left Left Case
		if (balance > 1 && key.toString().compareTo(node.left.key.toString()) < 0)
			return rightRotate(node);

    // Left Right Case
    if (balance > 1 && key.toString().compareTo(node.left.key.toString()) > 0) {
      node.left = leftRotate(node.left);
      return rightRotate(node);
    }

		// Right Right Case
		if (balance < -1 && key.toString().compareTo(node.right.key.toString()) > 0)
			return leftRotate(node);

		// Right Left Case
		if (balance < -1 && key.toString().compareTo(node.right.key.toString()) < 0) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}

		/* return the (unchanged) node pointer */
		return node;
	}

  static void print2DUtil(Node root, int space, int COUNT) {
    // Base case
    if (root == null)
        return;
    // Increase distance between levels
    space += COUNT;
    // Process right child first
    print2DUtil(root.right, space, COUNT);
    // Print current node after space
    // count
    System.out.print("\n");
    for (int i = COUNT; i < space; i++)
        System.out.print(" ");
    System.out.print(root.key + "\n");
    // Process left child
    print2DUtil(root.left, space, COUNT);
  }

	//Returns whether or not the string is all uppercase (used for dictionary parsing)
	public static boolean isUpperCase(String input) {

		for(int i = 0; i < input.length(); i++) {
			if(Character.isLowerCase(input.charAt(i)))
				return false;
		}

		return true;
	}

	public boolean findELement(String value) {
		Node current = root;

		while(current!= null) {
			int comparison = value.compareTo(current.key.toString());

			if(comparison == 0)
				return true;
			else if(comparison < 0)
				current = current.left;
			else
				current = current .right;

		}

		return false;
	}

	public String searchFor(String value) {
		Node current = root;

		while(current!= null) {
			int comparison = value.compareTo(current.key.toString());

			if(comparison == 0)
				return current.term.getDefinition();
			else if(comparison < 0)
				current = current.left;
			else
				current = current .right;

		}

		return ("WORD does not exist.");
	}

  public static void main(String[] args) {
		Dictionary tree = new Dictionary();
    try {

			File file1 = new File(args[0]);
			Scanner sc = new Scanner(file1);


			ArrayList<String> words = new ArrayList<>();

			String prevString = "";
			String prevName = "";

			while(sc.hasNext()) {
				if(sc.nextLine().compareTo("Produced by Graham Lawrence") == 0)
					break;
			}

			while(sc.hasNext()) {
				String currLine = sc.nextLine();

				//If it is caps insert the previous string, then start storing the next
				if(Dictionary.isUpperCase(currLine) && currLine.compareTo("") != 0) {
					Word currWord = new Word(prevName);
					currWord.addDefinition(prevString.trim());
					tree.root = tree.insert(tree.root, currWord);
					prevString = "";
					prevName = currLine;

					words.add(currLine);
				}
				//If its not caps append it
				else {
					prevString = prevString + "\n" + currLine;

				}
			}
			//System.out.println(tree.searchFor("DETERMINE"));
			System.out.println(tree.height(tree.root));
			System.out.print("$ ");

			sc = new Scanner(System.in);

			String inputLine = sc.nextLine();

			//Here's where the input starts going

			while(inputLine.compareTo("EXIT") != 0) {
				String[] inputWords = inputLine.split(" ");
				if(inputWords[0].compareTo("SEARCH") == 0) {
					try {
						if(inputWords[1] != null && tree.findELement(inputWords[1])) {
							String printStr = (tree.searchFor(inputWords[1]));
							printStr = printStr.trim();
							System.out.println("\n" + printStr + "\n");
						}
						else if(inputWords [1] != null)
							System.out.println(inputWords[1] + " does not exist");
						else
							System.out.println(" does not exist");
					}
					catch(Exception e) {
					  System.out.println(" does not exist");
					}
				}

				else {
					System.out.println("Invalid command");
				}

				System.out.print("$ ");
				inputLine = sc.nextLine();
			}

    } catch (IOException e) {
      e.printStackTrace();
    }
	}
}

class Word {

	//The word in the dictionary
	String name;

	String definition = "";

	public Word(String input) {
		name = input;
	}

	//Appends the new definition to the current one (for the first time it will already be blank)
	public void addDefinition(String input) {
		definition = definition + "\n" + input;
	}

	public String getDefinition() {
		return definition;
	}

	public String toString() {
		return name;
	}

}
