import java.util.*;
import java.util.stream.IntStream;

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

    boolean IsConnected()
    {
        OptionalInt maybeInit  = IntStream.range(0, vertex.length)
            .filter(i -> vertex[i] != null)
            .findAny();
        if (maybeInit.isEmpty())
           return true;
        int init = maybeInit.getAsInt();

        for (Vertex i : vertex)
            if (i != null)
                i.Hit = false;

        Stack<Integer> path = new Stack<>();
        path.push(init);
        for (Integer current; !path.isEmpty();)
        {
            current = path.peek();
            vertex[current].Hit = true;

            Integer finalCurrent = current;
            OptionalInt maybeNext = IntStream.range(0, m_adjacency[current].length)
                    .filter(i -> m_adjacency[finalCurrent][i] == 1 && !vertex[i].Hit)
                    .findFirst();
            if (maybeNext.isPresent())
                path.push(maybeNext.getAsInt());
            else
                path.pop();
        }
        return Arrays.stream(vertex)
                .filter(Objects::nonNull)
                .allMatch(v -> v.Hit);
    }
    public ArrayList<Vertex> WeakVertices()
    {
        ArrayList<Vertex> weak = new ArrayList<>();

        for (int i = 0; i < max_vertex; i++) {
            if (vertex[i] == null) continue;

            ArrayList<Integer> neighbors = new ArrayList<>();
            for (int j = 0; j < max_vertex; j++) {
                if (m_adjacency[i][j] == 1) {
                    neighbors.add(j);
                }
            }

            boolean inTriangle = false;
            for (int a = 0; a < neighbors.size(); a++) {
                for (int b = a + 1; b < neighbors.size(); b++) {
                    int u = neighbors.get(a);
                    int v = neighbors.get(b);
                    if (m_adjacency[u][v] == 1) {
                        inTriangle = true;
                        break;
                    }
                }
                if (inTriangle) break;
            }

            if (!inTriangle) {
                weak.add(vertex[i]);
            }
        }

        return weak;
    }

    public int[] findFarthestNodes() {
        int startVertex = -1;
        for (int i = 0; i < max_vertex; i++) {
            if (vertex[i] != null) {
                startVertex = i;
                break;
            }
        }
        if (startVertex == -1) {
            return new int[]{-1, -1}; // Граф пуст
        }

        int nodeB = bfsFarthestNode(startVertex);

        int nodeC = bfsFarthestNode(nodeB);

        return new int[]{nodeB, nodeC};
    }

    private int bfsFarthestNode(int start) {
        if (vertex[start] == null) return -1;

        Queue<Integer> queue = new LinkedList<>();
        int[] distances = new int[max_vertex];
        Arrays.fill(distances, -1);

        queue.add(start);
        distances[start] = 0;
        int farthestNode = start;
        int maxDistance = 0;

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (int i = 0; i < max_vertex; i++) {
                if (m_adjacency[current][i] == 1 && vertex[i] != null && distances[i] == -1) {
                    distances[i] = distances[current] + 1;
                    queue.add(i);
                    if (distances[i] > maxDistance) {
                        maxDistance = distances[i];
                        farthestNode = i;
                    }
                }
            }
        }

        return farthestNode;
    }

    public int countTriangles() {
        Set<String> triangles = new HashSet<>();

        for (int i = 0; i < max_vertex; i++) {
            if (vertex[i] == null) continue;

            for (int j = i + 1; j < max_vertex; j++) {
                if (vertex[j] == null || m_adjacency[i][j] != 1) continue;

                for (int k = j + 1; k < max_vertex; k++) {
                    if (vertex[k] == null || m_adjacency[j][k] != 1 || m_adjacency[i][k] != 1) continue;

                    // Формируем уникальную строку для треугольника (i, j, k)
                    int[] nodes = {i, j, k};
                    Arrays.sort(nodes);
                    String triangle = nodes[0] + "," + nodes[1] + "," + nodes[2];

                    triangles.add(triangle);
                }
            }
        }

        return triangles.size();
    }

    public ArrayList<Vertex> findNonTriangleVertices() {
        ArrayList<Vertex> result = new ArrayList<>();
        Set<Integer> triangleVertices = new HashSet<>();

        // Собираем все вершины, участвующие в треугольниках
        for (int i = 0; i < max_vertex; i++) {
            if (vertex[i] == null) continue;

            for (int j = i + 1; j < max_vertex; j++) {
                if (vertex[j] == null || m_adjacency[i][j] != 1) continue;

                for (int k = j + 1; k < max_vertex; k++) {
                    if (vertex[k] == null || m_adjacency[j][k] != 1 || m_adjacency[i][k] != 1) continue;

                    triangleVertices.add(i);
                    triangleVertices.add(j);
                    triangleVertices.add(k);
                }
            }
        }

        // Добавляем в результат все вершины, не входящие в треугольники
        for (int i = 0; i < max_vertex; i++) {
            if (vertex[i] != null && !triangleVertices.contains(i)) {
                result.add(vertex[i]);
            }
        }

        return result;
    }
}
