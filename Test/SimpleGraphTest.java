import org.junit.jupiter.api.Test;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SimpleGraphTest {

    @Test
    void testAddVertex() {
        SimpleGraph graph = new SimpleGraph(5);
        graph.AddVertex(10);
        assertNotNull(graph.vertex[0]);
        assertEquals(10, graph.vertex[0].Value);
    }

    @Test
    void testAddIsolatedVertex() {
        SimpleGraph graph = new SimpleGraph(5);
        graph.AddVertex(20);
        assertNotNull(graph.vertex[0]);
        for (int i = 0; i < 5; i++) {
            assertEquals(0, graph.m_adjacency[0][i]);
        }
    }

    @Test
    void testRemoveVertex() {
        SimpleGraph graph = new SimpleGraph(5);
        graph.AddVertex(10);
        graph.RemoveVertex(0);
        assertNull(graph.vertex[0]);
    }

    @Test
    void testAddEdge() {
        SimpleGraph graph = new SimpleGraph(5);
        graph.AddVertex(1);
        graph.AddVertex(2);
        assertFalse(graph.IsEdge(0, 1));
        graph.AddEdge(0, 1);
    }

    @Test
    void testRemoveEdge() {
        SimpleGraph graph = new SimpleGraph(5);
        graph.AddVertex(1);
        graph.AddVertex(2);
        graph.AddEdge(0, 1);
        graph.RemoveEdge(0, 1);
        assertFalse(graph.IsEdge(0, 1));
    }

    @Test
    void testIsEdge() {
        SimpleGraph graph = new SimpleGraph(5);
        graph.AddVertex(1);
        graph.AddVertex(2);
        graph.AddEdge(0, 1);
        assertFalse(graph.IsEdge(1, 2));
    }

    @Test
    void testRemoveVertexAndEdges() {
        SimpleGraph graph = new SimpleGraph(5);
        graph.AddVertex(1);
        graph.AddVertex(2);
        graph.AddVertex(3);
        graph.AddEdge(0, 1);
        graph.AddEdge(0, 2);
        graph.RemoveVertex(0);
        assertNull(graph.vertex[0]);
        assertFalse(graph.IsEdge(0, 1));
        assertFalse(graph.IsEdge(0, 2));
    }

    @Test
    void testDFSPathExists() {
        SimpleGraph graph = new SimpleGraph(5);
        for (int i = 0; i < 5; i++) graph.AddVertex(i);
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 2);
        graph.AddEdge(2, 3);
        graph.AddEdge(3, 4);

        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 4);
        assertEquals(5, path.size());
        assertEquals(0, path.get(0).Value);
        assertEquals(4, path.get(path.size() - 1).Value);
    }

    @Test
    void testDFSPathNotExists() {
        SimpleGraph graph = new SimpleGraph(3);
        graph.AddVertex(1);
        graph.AddVertex(2);
        graph.AddVertex(3);
        // no edges
        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 2);
        assertTrue(path.isEmpty());
    }

    @Test
    void testSimplePathExists() {
        SimpleGraph graph = new SimpleGraph(3);
        graph.AddVertex(1);
        graph.AddVertex(2);
        graph.AddVertex(3);
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 2);

        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 2);
        assertEquals(3, path.size());
        assertEquals(1, path.get(0).Value);
        assertEquals(2, path.get(1).Value);
        assertEquals(3, path.get(2).Value);
    }

    @Test
    void testNoPathExists() {
        SimpleGraph graph = new SimpleGraph(4);
        for (int i = 0; i < 4; i++) graph.AddVertex(i);
        graph.AddEdge(0, 1);
        graph.AddEdge(2, 3);

        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 3);
        assertTrue(path.isEmpty());
    }

    @Test
    void testCycleGraph() {
        SimpleGraph graph = new SimpleGraph(4);
        for (int i = 0; i < 4; i++) graph.AddVertex(i);
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 2);
        graph.AddEdge(2, 3);
        graph.AddEdge(3, 0); // цикл

        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 2);
        assertEquals(3, path.size());
        assertEquals(0, path.get(0).Value);
        assertEquals(1, path.get(1).Value);
        assertEquals(2, path.get(2).Value);
    }

    @Test
    void testDisconnectedGraph() {
        SimpleGraph graph = new SimpleGraph(6);
        for (int i = 0; i < 6; i++) graph.AddVertex(i);
        // Компонента 1: 0 - 1 - 2
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 2);
        // Компонента 2: 3 - 4 - 5
        graph.AddEdge(3, 4);
        graph.AddEdge(4, 5);

        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 5);
        assertTrue(path.isEmpty());
    }

    @Test
    void testEdgeCases_EmptyGraph() {
        SimpleGraph graph = new SimpleGraph(3);
        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 1);
        assertTrue(path.isEmpty());
    }

    @Test
    void testPathToSelfInDisconnectedGraph() {
        SimpleGraph graph = new SimpleGraph(1);
        graph.AddVertex(42);
        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 0);
        assertEquals(1, path.size());
        assertEquals(42, path.get(0).Value);
    }

    private SimpleGraph graph;

    // Helper method to extract vertex values from the result list for easier comparison
    private List<Integer> getPathValues(ArrayList<Vertex> path) {
        if (path == null) {
            return null; // Or throw exception, depending on desired behavior for null path
        }
        return path.stream().map(v -> v.Value).collect(Collectors.toList());
    }

    // Helper method to assert that a path is valid (nodes exist, edges exist)
    // This is useful when the exact path isn't guaranteed, but *a* path should exist.
    private void assertPathIsValid(SimpleGraph g, ArrayList<Vertex> path, int startValue, int endValue) {
        assertNotNull(path, "Path should not be null");
        assertFalse(path.isEmpty(), "Path should not be empty");

        // Check start and end points
        assertEquals(startValue, path.get(0).Value, "Path should start at the correct vertex");
        assertEquals(endValue, path.get(path.size() - 1).Value, "Path should end at the correct vertex");

        // Check connectivity
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex u = path.get(i);
            Vertex v = path.get(i + 1);
            int uIndex = -1, vIndex = -1;
            // Find indices (assuming values are unique for this test helper)
            for (int j = 0; j < g.max_vertex; ++j) {
                if (g.vertex[j] != null && g.vertex[j].Value == u.Value) uIndex = j;
                if (g.vertex[j] != null && g.vertex[j].Value == v.Value) vIndex = j;
            }
            assertTrue(uIndex != -1 && vIndex != -1, "Vertices in path must exist in graph");
            // Use direct adjacency matrix check as IsEdge might be unreliable
            assertTrue(g.m_adjacency[uIndex][vIndex] == 1,
                    "Edge must exist between consecutive vertices in the path: " + u.Value + " -> " + v.Value);
        }
    }

    @BeforeEach
    void setUp() {
        // Reset graph before each test
        graph = null;
    }

    @Test
    @DisplayName("DFS_01: Simple linear path")
    void testSimpleLinearPath() {
        graph = new SimpleGraph(5);
        graph.AddVertex(0); // idx 0
        graph.AddVertex(1); // idx 1
        graph.AddVertex(2); // idx 2
        graph.AddVertex(3); // idx 3
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 2);
        graph.AddEdge(2, 3);

        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 3);
        assertPathIsValid(graph, path, 0, 3);
        assertEquals(Arrays.asList(0, 1, 2, 3), getPathValues(path), "Path should be 0 -> 1 -> 2 -> 3");

        // Test in reverse
        path = graph.DepthFirstSearch(3, 0);
        assertPathIsValid(graph, path, 3, 0);
        assertEquals(Arrays.asList(3, 2, 1, 0), getPathValues(path), "Path should be 3 -> 2 -> 1 -> 0");
    }

    @Test
    @DisplayName("DFS_02: No path between disconnected components")
    void testNoPathDisconnected() {
        graph = new SimpleGraph(6);
        graph.AddVertex(0);
        graph.AddVertex(1);
        graph.AddVertex(2); // Component 1
        graph.AddVertex(10);
        graph.AddVertex(11);
        graph.AddVertex(12); // Component 2
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 2);
        graph.AddEdge(3, 4); // Indices for 10, 11
        graph.AddEdge(4, 5); // Indices for 11, 12

        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 5); // Search between components
        assertTrue(path.isEmpty(), "Path should be empty for disconnected components");

        path = graph.DepthFirstSearch(4, 1); // Search between components (reverse)
        assertTrue(path.isEmpty(), "Path should be empty for disconnected components (reverse)");
    }

    @Test
    @DisplayName("DFS_03: Path with cycles, ensuring termination")
    void testPathWithCycle() {
        graph = new SimpleGraph(6);
        // 0 -> 1 -> 2 -> 3 (Target)
        // ^    |    ^
        // |----4 -> 5
        graph.AddVertex(0);
        graph.AddVertex(1);
        graph.AddVertex(2);
        graph.AddVertex(3);
        graph.AddVertex(4);
        graph.AddVertex(5);
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 2);
        graph.AddEdge(2, 3); // Path to target
        graph.AddEdge(1, 4); // Branch
        graph.AddEdge(4, 0); // Cycle back to start
        graph.AddEdge(4, 5); // Another branch
        graph.AddEdge(5, 2); // Cycle back into path

        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 3);
        assertPathIsValid(graph, path, 0, 3);
        // The exact path might differ based on neighbor order,
        // e.g., 0-1-2-3 or 0-1-4-5-2-3. We only assert validity.
        System.out.println("Cycle Test Path: " + getPathValues(path)); // Optional: print path
    }

    @Test
    @DisplayName("DFS_04: Path requiring backtracking from dead end")
    void testBacktracking() {
        graph = new SimpleGraph(7);
        //      0
        //     / \
        //    1   4 -- 5 (Dead End)
        //   / \   \
        //  2   3   6 (Target)
        graph.AddVertex(0);
        graph.AddVertex(1);
        graph.AddVertex(2);
        graph.AddVertex(3);
        graph.AddVertex(4);
        graph.AddVertex(5);
        graph.AddVertex(6);
        graph.AddEdge(0, 1);
        graph.AddEdge(0, 4);
        graph.AddEdge(1, 2);
        graph.AddEdge(1, 3);
        graph.AddEdge(4, 5); // Dead end from 4
        graph.AddEdge(4, 6); // Path to target from 4

        // DFS might explore 0 -> 4 -> 5 first if neighbors are checked in index order.
        // It must backtrack from 5 to 4, then find 6.
        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 6);
        assertPathIsValid(graph, path, 0, 6);
        // Possible valid paths: [0, 4, 6] or [0, 1, ?, ..., 4, 6] - depends on implementation details
        // Let's assert one specific valid path possibility if neighbor checking order is known/assumed (e.g., by index)
        List<Integer> expectedPath = Arrays.asList(0, 4, 6); // Assumes 4 is checked before 1, or 5 before 6 if 4 is visited
        List<Integer> actualValues = getPathValues(path);
        System.out.println("Backtracking Test Path: " + actualValues); // Optional: print path
        // We'll stick to validity check as exact path isn't guaranteed by DFS definition
        // assertTrue(expectedPath.equals(actualValues) || otherPossiblePath.equals(actualValues), "Path should be valid");
    }

    @Test
    @DisplayName("DFS_05: Start and End nodes are the same")
    void testStartEqualsEnd() {
        graph = new SimpleGraph(3);
        graph.AddVertex(0);
        graph.AddVertex(1);
        graph.AddVertex(2);
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 2);

        ArrayList<Vertex> path = graph.DepthFirstSearch(1, 1);
        assertPathIsValid(graph, path, 1, 1);
        assertEquals(Arrays.asList(1), getPathValues(path), "Path from node to itself should contain only that node");
    }

    @Test
    @DisplayName("DFS_06: Graph with isolated vertex")
    void testIsolatedVertex() {
        graph = new SimpleGraph(4);
        graph.AddVertex(0);
        graph.AddVertex(1);
        graph.AddVertex(2); // Connected component
        graph.AddVertex(10); // Isolated vertex at index 3
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 2);

        // Search to isolated node
        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 3);
        assertTrue(path.isEmpty(), "Path to isolated vertex should be empty");

        // Search from isolated node
        path = graph.DepthFirstSearch(3, 1);
        assertTrue(path.isEmpty(), "Path from isolated vertex should be empty");

        // Search from isolated node to itself
        path = graph.DepthFirstSearch(3, 3);
        assertPathIsValid(graph, path, 10, 10);
        assertEquals(Arrays.asList(10), getPathValues(path), "Path from isolated node to itself");
    }

    @Test
    @DisplayName("DFS_07: Invalid vertex indices")
    void testInvalidIndices() {
        graph = new SimpleGraph(3);
        graph.AddVertex(0);
        graph.AddVertex(1);
        graph.AddEdge(0, 1);

        ArrayList<Vertex> path;

        // Index out of bounds
        path = graph.DepthFirstSearch(-1, 1);
        assertTrue(path.isEmpty(), "Path should be empty for negative VFrom");
        path = graph.DepthFirstSearch(0, 3);
        assertTrue(path.isEmpty(), "Path should be empty for VTo >= max_vertex");
        path = graph.DepthFirstSearch(0, -1);
        assertTrue(path.isEmpty(), "Path should be empty for negative VTo");
        path = graph.DepthFirstSearch(3, 0);
        assertTrue(path.isEmpty(), "Path should be empty for VFrom >= max_vertex");

        // Index points to null vertex slot
        path = graph.DepthFirstSearch(0, 2); // vertex[2] is null
        assertTrue(path.isEmpty(), "Path should be empty if VTo vertex is null");
        // To test VFrom being null, we'd need to add/remove
        graph.AddVertex(20); // at index 2
        graph.RemoveVertex(0); // vertex[0] is now null
        path = graph.DepthFirstSearch(0, 1);
        assertTrue(path.isEmpty(), "Path should be empty if VFrom vertex is null");

    }

    @Test
    @DisplayName("DFS_08: Larger graph with multiple paths")
    void testMultiplePaths() {
        graph = new SimpleGraph(8);
        //       0 -- 1 -- 2 (Target)
        //       | \  |  / |
        //       3 -- 4 -- 5
        //       |    |    |
        //       6 -- 7 ---
        graph.AddVertex(0);
        graph.AddVertex(1);
        graph.AddVertex(2);
        graph.AddVertex(3);
        graph.AddVertex(4);
        graph.AddVertex(5);
        graph.AddVertex(6);
        graph.AddVertex(7);
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 2); // Direct path
        graph.AddEdge(0, 3);
        graph.AddEdge(3, 4);
        graph.AddEdge(4, 1); // Path via 3, 4
        graph.AddEdge(0, 4); // Shortcut
        graph.AddEdge(1, 5);
        graph.AddEdge(5, 2); // Path via 5
        graph.AddEdge(4, 5);
        graph.AddEdge(3, 6);
        graph.AddEdge(6, 7);
        graph.AddEdge(7, 4); // Longer loop
        graph.AddEdge(5, 7); // Another connection

        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 2);
        assertPathIsValid(graph, path, 0, 2);
        System.out.println("Multiple Paths Test Path: " + getPathValues(path)); // Optional: print path
        // We cannot assert a specific path, only that *a* valid path is found.
    }

    @Test
    @DisplayName("DFS_09: Empty graph")
    void testEmptyGraph() {
        graph = new SimpleGraph(0);
        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 0); // Indices invalid anyway
        assertTrue(path.isEmpty(), "Path in empty graph should be empty");

        graph = new SimpleGraph(5); // Graph with capacity but no vertices added
        ArrayList<Vertex> path2 = graph.DepthFirstSearch(0, 1);
        assertTrue(path2.isEmpty(), "Path when vertices are null should be empty");
    }

    @Test
    @DisplayName("DFS_10: Check Hit flag reset (requires multiple calls)")
    void testHitFlagReset() {
        // This test relies on the DFS method correctly resetting flags internally
        // OR the test runner creating new graph instances (@BeforeEach helps).
        // We'll test as if flags *should* be reset by the method itself.
        graph = new SimpleGraph(4);
        graph.AddVertex(0);
        graph.AddVertex(1);
        graph.AddVertex(2);
        graph.AddVertex(3);
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 2);
        graph.AddEdge(0, 3); // Alternative path branch

        // First search: 0 to 2. Should mark 0, 1, 2 as Hit.
        ArrayList<Vertex> path1 = graph.DepthFirstSearch(0, 2);
        assertPathIsValid(graph, path1, 0, 2);
        assertEquals(Arrays.asList(0, 1, 2), getPathValues(path1));

        // If Hit flags weren't reset internally by DFS, this second search might fail
        // or find a non-optimal path depending on implementation.
        // Second search: 0 to 3
        ArrayList<Vertex> path2 = graph.DepthFirstSearch(0, 3);
        assertPathIsValid(graph, path2, 0, 3);
        assertEquals(Arrays.asList(0, 3), getPathValues(path2), "Second search should work correctly, finding path 0 -> 3");

        // Verify flags are false after search (optional, depends if method guarantees reset)
        // assertFalse(graph.vertex[0].Hit);
        // assertFalse(graph.vertex[1].Hit);
        // assertFalse(graph.vertex[2].Hit);
        // assertFalse(graph.vertex[3].Hit);
        // Note: The provided code *does* reset flags at the start, so this test should pass.
    }
}