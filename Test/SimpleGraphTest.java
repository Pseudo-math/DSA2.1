import org.junit.jupiter.api.Test;

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
    void testStartEqualsEnd() {
        SimpleGraph graph = new SimpleGraph(2);
        graph.AddVertex(100);
        graph.AddVertex(200);

        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 0);
        assertEquals(1, path.size());
        assertEquals(100, path.get(0).Value);
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
    void testMultiplePaths() {
        SimpleGraph graph = new SimpleGraph(5);
        for (int i = 0; i < 5; i++) graph.AddVertex(i);
        // Два пути: 0-1-4 и 0-2-3-4
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 4);
        graph.AddEdge(0, 2);
        graph.AddEdge(2, 3);
        graph.AddEdge(3, 4);

        ArrayList<Vertex> path = graph.DepthFirstSearch(0, 4);
        // DFS должен выбрать 0-1-4, как первый доступный путь
        assertEquals(3, path.size());
        assertEquals(0, path.get(0).Value);
        assertEquals(1, path.get(1).Value);
        assertEquals(4, path.get(2).Value);
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
}
