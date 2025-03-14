import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BalancedBSTTest {

    @Test
    void testGenerateTreeEmptyArray() {
        BalancedBST tree = new BalancedBST();
        tree.GenerateTree(new int[]{});

        assertNull(tree.Root, "Корень дерева должен быть null при пустом массиве");
        assertTrue(tree.IsBalanced(tree.Root), "Пустое дерево должно быть сбалансированным");
    }

    @Test
    void testGenerateTreeSingleElement() {
        BalancedBST tree = new BalancedBST();
        tree.GenerateTree(new int[]{10});

        assertNotNull(tree.Root, "Корень дерева не должен быть null");
        assertEquals(10, tree.Root.NodeKey, "Корень должен содержать правильное значение");
        assertNull(tree.Root.LeftChild, "Левый потомок должен быть null");
        assertNull(tree.Root.RightChild, "Правый потомок должен быть null");
        assertTrue(tree.IsBalanced(tree.Root), "Дерево из одного узла должно быть сбалансированным");
    }

    @Test
    void testGenerateTree() {
        BalancedBST bst = new BalancedBST();
        int[] values = {3, 1, 2, 5, 4, 6};
        bst.GenerateTree(values);

        assertNotNull(bst.Root, "Root should not be null after tree generation");
        assertEquals(4, bst.Root.NodeKey, "Root node should be 4");
    }
    @Test
    void test2GenerateTree() {
        BalancedBST tree = new BalancedBST();
        int[] input = {50, 25, 75, 20, 37, 62, 84, 19, 21, 31, 43, 55, 70, 80, 92};
        tree.GenerateTree(input);

        assertTrue(tree.IsBalanced(tree.Root), "Дерево должно быть сбалансированным");

        for (int key : input) {
            assertTrue(contains(tree.Root, key), "Дерево должно содержать " + key);
        }
    }

    private boolean contains(BSTNode node, int key) {
        if (node == null) return false;
        if (node.NodeKey == key) return true;
        return key < node.NodeKey ? contains(node.LeftChild, key) : contains(node.RightChild, key);
    }

    @Test
    void testSingleNodeLevel() {
        BalancedBST tree = new BalancedBST();
        tree.GenerateTree(new int[]{42});

        assertNotNull(tree.Root, "Корень дерева не должен быть null");
        assertEquals(0, tree.Root.Level, "Корень должен иметь уровень 0");
    }

    @Test
    void testLevelsInSmallTree() {
        BalancedBST tree = new BalancedBST();
        tree.GenerateTree(new int[]{10, 20, 30});

        BSTNode root = tree.Root;
        assertNotNull(root, "Корень не должен быть null");
        assertEquals(20, root.NodeKey, "Корень должен быть 20");
        assertEquals(0, root.Level, "Корень должен иметь уровень 0");

        assertNotNull(root.LeftChild, "Левый потомок не должен быть null");
        assertEquals(10, root.LeftChild.NodeKey, "Левый потомок должен быть 10");
        assertEquals(1, root.LeftChild.Level, "Левый потомок должен иметь уровень 1");

        assertNotNull(root.RightChild, "Правый потомок не должен быть null");
        assertEquals(30, root.RightChild.NodeKey, "Правый потомок должен быть 30");
        assertEquals(1, root.RightChild.Level, "Правый потомок должен иметь уровень 1");
    }

    @Test
    void testLevelsInLargerTree() {
        BalancedBST tree = new BalancedBST();
        int[] input = {50, 25, 75, 20, 37, 62, 84, 19, 21, 31, 43, 55, 70, 80, 92};
        tree.GenerateTree(input);

        BSTNode root = tree.Root;
        assertNotNull(root, "Корень не должен быть null");
        assertEquals(50, root.NodeKey, "Корень должен быть 50");
        assertEquals(0, root.Level, "Корень должен иметь уровень 0");

        assertNotNull(root.LeftChild, "Левый потомок не должен быть null");
        assertEquals(25, root.LeftChild.NodeKey, "Левый потомок должен быть 25");
        assertEquals(1, root.LeftChild.Level, "Левый потомок должен иметь уровень 1");

        assertNotNull(root.RightChild, "Правый потомок не должен быть null");
        assertEquals(75, root.RightChild.NodeKey, "Правый потомок должен быть 75");
        assertEquals(1, root.RightChild.Level, "Правый потомок должен иметь уровень 1");

        // Проверяем более глубокие узлы
        assertNotNull(root.LeftChild.LeftChild, "Левый потомок 25 не должен быть null");
        assertEquals(20, root.LeftChild.LeftChild.NodeKey, "Левый потомок 25 должен быть 20");
        assertEquals(2, root.LeftChild.LeftChild.Level, "Левый потомок 25 должен иметь уровень 2");

        assertNotNull(root.LeftChild.RightChild, "Правый потомок 25 не должен быть null");
        assertEquals(37, root.LeftChild.RightChild.NodeKey, "Правый потомок 25 должен быть 37");
        assertEquals(2, root.LeftChild.RightChild.Level, "Правый потомок 25 должен иметь уровень 2");
    }

    @Test
    void testEmptyTreeLevel() {
        BalancedBST tree = new BalancedBST();
        tree.GenerateTree(new int[]{});

        assertNull(tree.Root, "Корень дерева должен быть null при пустом массиве");
    }

    @Test
    void testTreeIsBalanced() {
        BalancedBST bst = new BalancedBST();
        int[] values = {3, 1, 2, 5, 4, 6};
        bst.GenerateTree(values);

        assertTrue(bst.IsBalanced(bst.Root), "Generated tree should be balanced");
    }

    @Test
    void testBalancedTree() {
        BalancedBST tree = new BalancedBST();
        tree.GenerateTree(new int[]{1, 2, 3, 4, 5, 6, 7});
        assertTrue(tree.IsBalanced(tree.Root), "Сбалансированное дерево должно быть сбалансированным");
    }

    @Test
    void testUnbalancedTree() {
        BalancedBST tree = new BalancedBST();

        // Ручное создание несбалансированного дерева
        tree.Root = new BSTNode(10, null);
        tree.Root.RightChild = new BSTNode(20, tree.Root);
        tree.Root.RightChild.RightChild = new BSTNode(30, tree.Root.RightChild);

        assertFalse(tree.IsBalanced(tree.Root), "Несбалансированное дерево не должно быть сбалансированным");
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

    @Test
    void testIsRightCorrectBST() {
        BalancedBST tree = new BalancedBST();
        int[] input = {50, 25, 75, 20, 37, 62, 84, 19, 21, 31, 43, 55, 70, 80, 92};
        tree.GenerateTree(input);

        assertTrue(tree.IsRight(tree.Root), "Дерево должно быть правильным BST");
    }

    @Test
    void testIsRightIncorrectBST() {
        BalancedBST tree = new BalancedBST();
        tree.Root = new BSTNode(10, null);
        tree.Root.LeftChild = new BSTNode(15, tree.Root); // Нарушает правило BST

        assertFalse(tree.IsRight(tree.Root), "Дерево с ошибкой не должно быть BST");
    }

    @Test
    void testIsRightEmptyTree() {
        BalancedBST tree = new BalancedBST();
        assertTrue(tree.IsRight(tree.Root), "Пустое дерево считается правильным BST");
    }

    @Test
    void testIsRightSingleElement() {
        BalancedBST tree = new BalancedBST();
        tree.GenerateTree(new int[]{42});

        assertTrue(tree.IsRight(tree.Root), "Дерево из одного узла должно быть правильным BST");
    }

}
