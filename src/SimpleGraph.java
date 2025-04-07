import java.util.*;

class Vertex
{
    public int Value;
    public boolean Hit;
    public Vertex(int val)
    {
        Value = val;
        Hit = false;
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
    public ArrayList<Vertex> DepthFirstSearch(int VFrom, int VTo) {
        ArrayList<Vertex> path = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();

        for (Vertex v : vertex) {
            if (v != null) v.Hit = true;
        }

        stack.push(VFrom);
        vertex[VFrom].Hit = true;

        while (!stack.isEmpty()) {
            int current = stack.peek();

            if (current == VTo) {
                ArrayList<Vertex> result = new ArrayList<>();
                for (int index : stack) {
                    result.add(vertex[index]);
                }
                return result;
            }

            boolean foundUnvisited = false;
            for (int i = 0; i < max_vertex; i++) {
                if (m_adjacency[current][i] == 1 && !vertex[i].Hit) {
                    vertex[i].Hit = true;
                    stack.push(i);
                    foundUnvisited = true;
                    break;
                }
            }

            if (!foundUnvisited) {
                stack.pop();
            }
        }

        return new ArrayList<>();
    }

}