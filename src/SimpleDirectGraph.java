import java.util.*;

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

    public List<Integer> findLongestPath() {
        List<Integer> longestPath = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();
        boolean[] visited = new boolean[max_vertex];

        for (int i = 0; i < max_vertex; i++) {
            if (vertex[i] != null) {
                DFSLongestPath(i, visited, currentPath, longestPath);
            }
        }
        return longestPath;
    }

    private void DFSLongestPath(int v, boolean[] visited, List<Integer> currentPath, List<Integer> longestPath) {
        if (vertex[v] == null) return;

        visited[v] = true;
        currentPath.add(v);

        if (currentPath.size() > longestPath.size()) {
            longestPath.clear();
            longestPath.addAll(currentPath);
        }

        for (int i = 0; i < max_vertex; i++) {
            if (m_adjacency[v][i] == 1 && !visited[i] && vertex[i] != null) {
                DFSLongestPath(i, visited, currentPath, longestPath);
            }
        }

        visited[v] = false;
        currentPath.removeLast();
    }

    public List<List<Integer>> findAllCycles() {
        List<List<Integer>> cycles = new ArrayList<>();
        boolean[] visited = new boolean[max_vertex];
        int[] parent = new int[max_vertex];
        Arrays.fill(parent, -1);

        // Проверяем каждую вершину как стартовую
        for (int start = 0; start < max_vertex; start++) {
            if (vertex[start] == null || visited[start]) continue;

            Queue<Integer> queue = new LinkedList<>();
            queue.add(start);
            visited[start] = true;

            while (!queue.isEmpty()) {
                int current = queue.poll();

                // Проверяем всех соседей текущей вершины
                for (int neighbor = 0; neighbor < max_vertex; neighbor++) {
                    if (m_adjacency[current][neighbor] != 1 || vertex[neighbor] == null) continue;

                    // Если сосед уже посещён и не является родителем, найден цикл
                    if (visited[neighbor] && neighbor != parent[current]) {
                        // Восстанавливаем цикл
                        List<Integer> cycle = new ArrayList<>();
                        cycle.add(neighbor); // Конечная вершина цикла
                        int v = current;
                        while (v != neighbor && v != -1) {
                            cycle.add(v);
                            v = parent[v];
                        }
                        if (v == neighbor) { // Убедимся, что путь замкнулся
                            cycle.add(neighbor); // Замыкаем цикл
                            Collections.reverse(cycle); // Для корректного порядка
                            cycles.add(cycle);
                        }
                    }
                    // Если сосед не посещён, добавляем его в очередь
                    else if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        parent[neighbor] = current;
                        queue.add(neighbor);
                    }
                }
            }
        }

        return cycles;
    }
}