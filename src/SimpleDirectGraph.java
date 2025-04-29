import java.util.List;

class SimpleDirectGraph
{
    Vertex [] vertex;
    int [][] m_adjacency;
    int max_vertex;

    public SimpleDirectGraph(int size)
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
        {
            RemoveArc(v, i);
            RemoveArc(i, v);
        }
        vertex[v] = null;
    }

    public boolean IsArc(int v1, int v2)
    {
        return  (m_adjacency[v1][v2] == 1);
    }

    public void AddArc(int v1, int v2)
    {
        if (IsArc(v1, v2))
            return;
        m_adjacency[v1][v2] = 1;
    }

    public void RemoveArc(int v1, int v2)
    {
        if (!IsArc(v1, v2))
            return;
        m_adjacency[v1][v2] = 0;
    }

    public boolean HasCycle() {
        int[] state = new int[max_vertex]; // 0 = unvisited, 1 = visiting, 2 = visited

        for (int i = 0; i < max_vertex; i++) {
            if (vertex[i] != null && state[i] == 0) {
                if (DFSHasCycle(i, state)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean DFSHasCycle(int v, int[] state) {
        state[v] = 1;

        for (int i = 0; i < max_vertex; i++) {
            if (m_adjacency[v][i] == 1) {
                if (vertex[i] == null) continue;

                if (state[i] == 1) {
                    return true;
                }

                if (state[i] == 0) {
                    if (DFSHasCycle(i, state)) {
                        return true;
                    }
                }
            }
        }

        state[v] = 2;
        return false;
    }

}