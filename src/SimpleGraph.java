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
        return  (m_adjacency[v1][v2] == 1 && m_adjacency[v2][v1] == 1);
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
        if (VFrom < 0 || VFrom >= max_vertex || VTo < 0 || VTo >= max_vertex
                || vertex[VFrom] == null || vertex[VTo] == null) {
            return new ArrayList<>();
        }

        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < max_vertex; i++) {
            if (vertex[i] != null) {
                vertex[i].Hit = false;
            }
        }

        int current = VFrom;
        vertex[current].Hit = true;
        stack.push(current);

        while (!stack.isEmpty()) {
            current = stack.peek();

            if (current == VTo) {
                ArrayList<Vertex> result = new ArrayList<>();
                for (int index : stack) {
                    result.add(vertex[index]);
                }
                return result;
            }

            int nextNeighbor = -1;
            for (int i = 0; i < max_vertex; i++) {
                if (m_adjacency[current][i] == 1 && vertex[i] != null && !vertex[i].Hit) {
                    nextNeighbor = i;
                    break;
                }
            }

            if (nextNeighbor != -1) {
                vertex[nextNeighbor].Hit = true;
                stack.push(nextNeighbor);
            } else {
                stack.pop();
            }
        }

        return new ArrayList<>();
    }

    public ArrayList<Vertex> BreadthFirstSearch(int VFrom, int VTo) {
        ArrayList<Vertex> path = new ArrayList<>();

        // Проверка на корректность индексов
        if (VFrom < 0 || VFrom >= max_vertex || VTo < 0 || VTo >= max_vertex
                || vertex[VFrom] == null || vertex[VTo] == null) {
            return path;
        }

        // Очистка Hit для всех вершин
        for (int i = 0; i < max_vertex; i++) {
            if (vertex[i] != null) {
                vertex[i].Hit = false;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        int[] parents = new int[max_vertex];
        Arrays.fill(parents, -1); // для восстановления пути

        vertex[VFrom].Hit = true;
        queue.add(VFrom);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            if (current == VTo) {
                // Восстановление пути из массива родителей
                LinkedList<Vertex> reversePath = new LinkedList<>();
                int step = VTo;
                while (step != -1) {
                    reversePath.addFirst(vertex[step]);
                    step = parents[step];
                }
                path.addAll(reversePath);
                return path;
            }

            for (int i = 0; i < max_vertex; i++) {
                if (m_adjacency[current][i] == 1 && vertex[i] != null && !vertex[i].Hit) {
                    vertex[i].Hit = true;
                    parents[i] = current;
                    queue.add(i);
                }
            }
        }

        return path; // Путь не найден
    }

}
