import org.junit.jupiter.api.Test;
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
}
