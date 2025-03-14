import java.util.*;

class BSTNode
{
    public int NodeKey; // ключ узла
    public BSTNode Parent; // родитель или null для корня
    public BSTNode LeftChild; // левый потомок
    public BSTNode RightChild; // правый потомок	
    public int     Level; // глубина узла

    public BSTNode(int key, BSTNode parent)
    {
        NodeKey = key;
        Parent = parent;
        LeftChild = null;
        RightChild = null;
        if (parent == null)
            Level = 0;
        else
            Level = parent.Level + 1;
    }
}

class BalancedBST
{
    public BSTNode Root; // корень дерева
    public int Count;
    public BalancedBST()
    {
        Root = null;
        Count = 0;
    }
    private BSTNode AddNode(int key, BSTNode parent)
    {
        Count++;
        var addingNode = new BSTNode(key, parent);
        if (parent == null)
        {
            Root = addingNode;
            return addingNode;
        }
        if (key < parent.NodeKey)
        {
            parent.LeftChild = addingNode;
        }
        if (parent.NodeKey < key)
        {
            parent.RightChild = addingNode;
        }
        return addingNode;
    }

    public void GenerateTree(int[] a)
    {
        int[] sorted = a.clone();
        Arrays.sort(sorted);
        GenerateTreeHelper(sorted, 0, a.length, Root);
    }
    public void GenerateTreeHelper(int[] sorted, int start, int end, BSTNode parent)
    {
        if (start >= end) return;
        int mid = start + (end - start) / 2;
        var addedNode = AddNode(sorted[mid], parent);
        GenerateTreeHelper(sorted, start, mid , addedNode);
        GenerateTreeHelper(sorted, mid + 1, end, addedNode);

    }
    public boolean IsRight(BSTNode root_node) {
        return isBST(root_node, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean isBST(BSTNode node, int min, int max) {
        if (node == null) return true;

        if (node.NodeKey < min || node.NodeKey > max) return false;

        return isBST(node.LeftChild, min, node.NodeKey - 1) &&
                isBST(node.RightChild, node.NodeKey + 1, max);
    }

    public boolean IsBalanced(BSTNode root_node)
    {
        if (root_node == null)
            return true;
        return (TreeLevelAndIsBalanced(root_node) >= 0);
    }
    private int TreeLevelAndIsBalanced(BSTNode root_node)
    {
        int leftTreeLevel = 0;
        int rightTreeLevel = 0;
        if (root_node == null)
            return 0;
        if (root_node.LeftChild != null)
            leftTreeLevel = TreeLevelAndIsBalanced(root_node.LeftChild);
        if (root_node.RightChild != null)
            rightTreeLevel = TreeLevelAndIsBalanced(root_node.RightChild);
        if (leftTreeLevel == -1 || rightTreeLevel == -1)
            return -1;
        if (leftTreeLevel >= rightTreeLevel + 2 || rightTreeLevel >= leftTreeLevel + 2)
            return -1;
        return Math.max(leftTreeLevel, rightTreeLevel) + 1;
    }

}  