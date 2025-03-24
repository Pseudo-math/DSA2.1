import java.util.*;

class Vertex
{
    public int Value;
    public Vertex(int val)
    {
        Value = val;
    }
}

class SimpleGraph
{
    Vertex [] vertex;
    int [][] m_adjacency;
    int max_vertex;

    public SimpleGraph(int size)
    {
        max_vertex = size;
        m_adjacency = new int [size][size];
        vertex = new Vertex[size];
    }

    public void AddVertex(int value)
    {
        int index = -1;
        for (int i = 0; i < max_vertex; i++)
        {
            if (vertex[i] == null)
            {
                index = i;
                break;
            }
        }
        if (index == -1)
            return;
        vertex[index] = new Vertex(value);
    }

    // здесь и далее, параметры v -- индекс вершины
    // в списке vertex
    public void RemoveVertex(int v)
    {
        for (int i = 0; i < max_vertex; i++)
            RemoveEdge(v, i);
        vertex[v] = null;
    }

    public boolean IsEdge(int v1, int v2)
    {
        return  (m_adjacency[v1][v1] == 1 && m_adjacency[v2][v2] == 1);
    }

    public void AddEdge(int v1, int v2)
    {
        if (IsEdge(v1, v2))
            return;
        m_adjacency[v1][v2] = 1;
        m_adjacency[v2][v1] = 1;
    }

    public void RemoveEdge(int v1, int v2)
    {
        if (!IsEdge(v1, v2))
            return;
        m_adjacency[v1][v2] = 0;
        m_adjacency[v2][v1] = 0;
    }
}