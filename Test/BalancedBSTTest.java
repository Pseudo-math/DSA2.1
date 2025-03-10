import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BalancedBSTTest {

    @Test
    void testGenerateTree() {
        BalancedBST bst = new BalancedBST();
        int[] values = {3, 1, 2, 5, 4, 6};
        bst.GenerateTree(values);

        assertNotNull(bst.Root, "Root should not be null after tree generation");
        assertEquals(3, bst.Root.NodeKey, "Root node should be the median of sorted array");
    }

    @Test
    void testTreeIsBalanced() {
        BalancedBST bst = new BalancedBST();
        int[] values = {3, 1, 2, 5, 4, 6};
        bst.GenerateTree(values);

        assertTrue(bst.IsBalanced(bst.Root), "Generated tree should be balanced");
    }

    @Test
    void testSingleElementTree() {
        BalancedBST bst = new BalancedBST();
        int[] values = {10};
        bst.GenerateTree(values);

        assertNotNull(bst.Root, "Tree with one element should have a root");
        assertEquals(10, bst.Root.NodeKey, "Root node should be the only element");
        assertTrue(bst.IsBalanced(bst.Root), "Single element tree should be balanced");
    }

    @Test
    void testEmptyTree() {
        BalancedBST bst = new BalancedBST();
        int[] values = {};
        bst.GenerateTree(values);

        assertNull(bst.Root, "Tree generated from empty array should have null root");
        assertTrue(bst.IsBalanced(bst.Root), "Empty tree should be balanced");
    }

}
