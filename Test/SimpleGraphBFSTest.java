import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

// Предполагается, что классы Vertex и SimpleGraph доступны в этом же пакете или импортированы
// import your.package.name.Vertex;
// import your.package.name.SimpleGraph;

class SimpleGraphBFSTest {

    private SimpleGraph graph;

    @BeforeEach
    void setUp() {
        // Создаем граф размером 8 перед каждым тестом
        graph = new SimpleGraph(8);
        // Добавляем вершины со значениями 0-7
        for (int i = 0; i < 8; i++) {
            graph.AddVertex(i);
        }
    }

    // Вспомогательный метод для получения списка значений вершин из пути
    private List<Integer> getVertexValues(ArrayList<Vertex> path) {
        if (path == null) {
            return Collections.emptyList();
        }
        return path.stream().map(v -> v.Value).collect(Collectors.toList());
    }

    @Test
    @DisplayName("BFS находит простой путь")
    void testBFS_FindSimplePath() {
        // 0 - 1 - 2
        // |
        // 3
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 2);
        graph.AddEdge(0, 3);

        ArrayList<Vertex> path = graph.BreadthFirstSearch(0, 2);
        assertNotNull(path, "Путь не должен быть null");
        assertEquals(Arrays.asList(0, 1, 2), getVertexValues(path), "Неверный путь найден");

        // Проверяем, что Hit флаги были установлены правильно (минимум для пути)
        assertTrue(graph.vertex[0].Hit, "Вершина 0 должна быть посещена");
        assertTrue(graph.vertex[1].Hit, "Вершина 1 должна быть посещена");
        assertTrue(graph.vertex[2].Hit, "Вершина 2 должна быть посещена");
        // Вершина 3 тоже может быть посещена в зависимости от порядка обхода соседей 0
    }

    @Test
    @DisplayName("BFS находит кратчайший путь при наличии нескольких")
    void testBFS_FindShortestPathWithMultipleOptions() {
        // 0 -- 1 -- 2 -- 7 (длина 3)
        // |         |
        // 3 -- 4 -- 5 -- 7 (длина 3)
        // |               |
        // 6 --------------- 7 (длина 2) - Кратчайший
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 2);
        graph.AddEdge(2, 7);
        graph.AddEdge(0, 3);
        graph.AddEdge(3, 4);
        graph.AddEdge(4, 5);
        graph.AddEdge(5, 7);
        graph.AddEdge(0, 6);
        graph.AddEdge(6, 7);


        ArrayList<Vertex> path = graph.BreadthFirstSearch(0, 7);
        assertNotNull(path);
        assertEquals(Arrays.asList(0, 6, 7), getVertexValues(path), "Должен быть найден кратчайший путь");
    }


    @Test
    @DisplayName("BFS в графе с циклом находит кратчайший путь")
    void testBFS_FindPathInGraphWithCycle() {
        // 0 -- 1 -- 2
        // |    |    |
        // 3 -- 4 -- 5
        //      |
        //      6 -- 7
        graph.AddEdge(0, 1); graph.AddEdge(1, 2);
        graph.AddEdge(0, 3); graph.AddEdge(1, 4); graph.AddEdge(2, 5);
        graph.AddEdge(3, 4); graph.AddEdge(4, 5);
        graph.AddEdge(4, 6); graph.AddEdge(6, 7);

        // Ищем путь от 0 к 5
        ArrayList<Vertex> path = graph.BreadthFirstSearch(0, 5);
        assertNotNull(path);
        // Ожидаемый кратчайший путь: 0 -> 1 -> 2 -> 5 ИЛИ 0 -> 3 -> 4 -> 5. Оба имеют длину 3.
        // BFS гарантирует кратчайший по числу рёбер. Какой именно из них будет найден - зависит от порядка обхода соседей.
        // Проверим длину пути и его начало/конец.
        assertEquals(4, path.size(), "Длина пути должна быть 4 (3 ребра)");
        assertEquals(0, path.get(0).Value, "Начало пути должно быть 0");
        assertEquals(5, path.get(path.size() - 1).Value, "Конец пути должен быть 5");
        // Более строгая проверка одного из возможных путей:
        assertTrue(
                getVertexValues(path).equals(Arrays.asList(0, 1, 2, 5)) ||
                        getVertexValues(path).equals(Arrays.asList(0, 3, 4, 5)) ||
                        getVertexValues(path).equals(Arrays.asList(0, 1, 4, 5)), // Еще один возможный кратчайший путь
                "Найденный путь не является одним из ожидаемых кратчайших"
        );

        // Ищем путь от 0 к 7
        path = graph.BreadthFirstSearch(0, 7);
        assertNotNull(path);
        // Кратчайший путь: 0 -> 1 -> 4 -> 6 -> 7 ИЛИ 0 -> 3 -> 4 -> 6 -> 7. Длина 4.
        assertEquals(5, path.size(), "Длина пути от 0 к 7 должна быть 5 (4 ребра)");
        assertEquals(0, path.get(0).Value);
        assertEquals(7, path.get(path.size() - 1).Value);
        assertTrue(
                getVertexValues(path).equals(Arrays.asList(0, 1, 4, 6, 7)) ||
                        getVertexValues(path).equals(Arrays.asList(0, 3, 4, 6, 7)),
                "Найденный путь 0->7 не является одним из ожидаемых кратчайших"
        );
    }


    @Test
    @DisplayName("BFS не находит путь в несвязанном графе")
    void testBFS_NoPathFound() {
        // Компонент 1: 0 - 1
        // Компонент 2: 2 - 3
        graph.AddEdge(0, 1);
        graph.AddEdge(2, 3);

        ArrayList<Vertex> path = graph.BreadthFirstSearch(0, 3);
        assertNotNull(path, "Результат не должен быть null");
        assertTrue(path.isEmpty(), "Путь между несвязанными компонентами должен быть пустым");

        // Проверяем флаги Hit - должны быть установлены только для достижимых из VFrom
        assertTrue(graph.vertex[0].Hit, "Вершина 0 должна быть посещена");
        assertTrue(graph.vertex[1].Hit, "Вершина 1 должна быть посещена");
        assertFalse(graph.vertex[2].Hit, "Вершина 2 не должна быть посещена");
        assertFalse(graph.vertex[3].Hit, "Вершина 3 не должна быть посещена");
    }

    @Test
    @DisplayName("BFS когда начальная и конечная вершины совпадают")
    void testBFS_StartEqualsEnd() {
        graph.AddEdge(0, 1); // Добавим хоть какое-то ребро

        ArrayList<Vertex> path = graph.BreadthFirstSearch(0, 0);
        assertNotNull(path);
        assertEquals(1, path.size(), "Путь должен состоять из одной вершины");
        assertEquals(0, path.get(0).Value, "Путь должен содержать стартовую/конечную вершину");
        assertTrue(graph.vertex[0].Hit, "Стартовая вершина должна быть посещена");
        assertFalse(graph.vertex[1].Hit, "Другие вершины не должны быть посещены без необходимости");
    }

    @Test
    @DisplayName("BFS с некорректным индексом VFrom")
    void testBFS_InvalidVFromIndex() {
        ArrayList<Vertex> path = graph.BreadthFirstSearch(-1, 3);
        assertNotNull(path);
        assertTrue(path.isEmpty(), "Путь должен быть пуст при некорректном VFrom");

        path = graph.BreadthFirstSearch(graph.max_vertex, 3);
        assertNotNull(path);
        assertTrue(path.isEmpty(), "Путь должен быть пуст при некорректном VFrom");
    }

    @Test
    @DisplayName("BFS с некорректным индексом VTo")
    void testBFS_InvalidVToIndex() {
        ArrayList<Vertex> path = graph.BreadthFirstSearch(0, -1);
        assertNotNull(path);
        assertTrue(path.isEmpty(), "Путь должен быть пуст при некорректном VTo");

        path = graph.BreadthFirstSearch(0, graph.max_vertex);
        assertNotNull(path);
        assertTrue(path.isEmpty(), "Путь должен быть пуст при некорректном VTo");
    }

    @Test
    @DisplayName("BFS когда стартовая вершина не существует (null)")
    void testBFS_NullVFromVertex() {
        graph.RemoveVertex(0); // Удаляем вершину с индексом 0
        ArrayList<Vertex> path = graph.BreadthFirstSearch(0, 1);
        assertNotNull(path);
        assertTrue(path.isEmpty(), "Путь должен быть пуст, если VFrom == null");
    }

    @Test
    @DisplayName("BFS когда конечная вершина не существует (null)")
    void testBFS_NullVToVertex() {
        graph.RemoveVertex(1); // Удаляем вершину с индексом 1
        graph.AddEdge(0, 2); // Добавим ребро, чтобы было что обходить

        ArrayList<Vertex> path = graph.BreadthFirstSearch(0, 1);
        assertNotNull(path);
        assertTrue(path.isEmpty(), "Путь должен быть пуст, если VTo == null");
        // Проверим, что поиск вообще начался
        assertTrue(graph.vertex[0].Hit, "Вершина 0 должна быть посещена");
        assertTrue(graph.vertex[2].Hit, "Вершина 2 должна быть посещена");
    }

    @Test
    @DisplayName("BFS на пустом графе")
    void testBFS_EmptyGraph() {
        SimpleGraph emptyGraph = new SimpleGraph(0);
        ArrayList<Vertex> path = emptyGraph.BreadthFirstSearch(0, 0); // Индексы некорректны для пустого графа
        assertNotNull(path);
        assertTrue(path.isEmpty(), "Путь в пустом графе должен быть пуст");

        SimpleGraph graphNoVertices = new SimpleGraph(5); // Граф без вершин
        path = graphNoVertices.BreadthFirstSearch(0, 1);
        assertNotNull(path);
        assertTrue(path.isEmpty(), "Путь в графе без вершин должен быть пуст");
    }

    @Test
    @DisplayName("BFS на графе с одной вершиной")
    void testBFS_SingleVertexGraph() {
        SimpleGraph singleGraph = new SimpleGraph(1);
        singleGraph.AddVertex(100);

        ArrayList<Vertex> path = singleGraph.BreadthFirstSearch(0, 0);
        assertNotNull(path);
        assertEquals(1, path.size());
        assertEquals(100, path.get(0).Value);

        // Поиск несуществующей вершины
        path = singleGraph.BreadthFirstSearch(0, 1);
        assertNotNull(path);
        assertTrue(path.isEmpty());
    }

    @Test
    @DisplayName("BFS проверяет флаги Hit корректно сбрасываются между вызовами")
    void testBFS_HitFlagsReset() {
        // 0 -- 1 -- 2
        graph.AddEdge(0, 1);
        graph.AddEdge(1, 2);

        // Первый поиск
        ArrayList<Vertex> path1 = graph.BreadthFirstSearch(0, 2);
        assertEquals(Arrays.asList(0, 1, 2), getVertexValues(path1));
        assertTrue(graph.vertex[0].Hit);
        assertTrue(graph.vertex[1].Hit);
        assertTrue(graph.vertex[2].Hit);

        // Второй поиск в обратном направлении
        ArrayList<Vertex> path2 = graph.BreadthFirstSearch(2, 0);
        // Перед вторым вызовом флаги должны сброситься внутри BFS
        assertEquals(Arrays.asList(2, 1, 0), getVertexValues(path2));
        // Флаги снова должны быть true после второго поиска
        assertTrue(graph.vertex[0].Hit);
        assertTrue(graph.vertex[1].Hit);
        assertTrue(graph.vertex[2].Hit);
    }

    @Test
    @DisplayName("BFS находит путь в более сложной структуре")
    void testBFS_ComplexStructure() {
        //     1 -- 2
        //    / \  / \
        //   0---3---4
        //    \ / \ / \
        //     7---6---5
        graph.AddEdge(0, 1); graph.AddEdge(0, 3); graph.AddEdge(0, 7);
        graph.AddEdge(1, 2); graph.AddEdge(1, 3);
        graph.AddEdge(2, 3); graph.AddEdge(2, 4);
        graph.AddEdge(3, 4); graph.AddEdge(3, 6); graph.AddEdge(3, 7);
        graph.AddEdge(4, 5); graph.AddEdge(4, 6);
        graph.AddEdge(5, 6);
        graph.AddEdge(6, 7);

        // Путь от 0 до 5
        ArrayList<Vertex> path = graph.BreadthFirstSearch(0, 5);
        assertNotNull(path);
        // Возможные кратчайшие пути (длина 3):
        // 0 -> 3 -> 4 -> 5
        // 0 -> 3 -> 6 -> 5
        // 0 -> 7 -> 6 -> 5 (если обход соседей 0 идет 7, 3, 1)
        // 0 -> 1 -> 2 -> 4 -> 5 (не кратчайший)
        // 0 -> 1 -> 3 -> 4 -> 5 (не кратчайший)
        // 0 -> 1 -> 3 -> 6 -> 5 (не кратчайший)

        assertEquals(4, path.size(), "Длина кратчайшего пути от 0 до 5 должна быть 4 (3 ребра)");
        assertEquals(0, path.get(0).Value);
        assertEquals(5, path.get(path.size() - 1).Value);
        List<Integer> values = getVertexValues(path);
        assertTrue(
                values.equals(Arrays.asList(0, 3, 4, 5)) ||
                        values.equals(Arrays.asList(0, 3, 6, 5)) ||
                        values.equals(Arrays.asList(0, 7, 6, 5)) || // Менее вероятно, но возможно
                        values.equals(Arrays.asList(0, 1, 2, 5)), // Добавим вариант, если 1-2-5 быстрее из-за порядка обхода
                "Найденный путь 0->5 не является одним из ожидаемых кратчайших"
        );


        // Путь от 1 до 6
        path = graph.BreadthFirstSearch(1, 6);
        assertNotNull(path);
        // Возможные кратчайшие пути (длина 2):
        // 1 -> 3 -> 6
        // 1 -> 0 -> 7 -> 6 (не кратч.)
        // 1 -> 2 -> 4 -> 6
        // 1 -> 2 -> 3 -> 6 (не кратч.)
        assertEquals(3, path.size(), "Длина кратчайшего пути от 1 до 6 должна быть 3 (2 ребра)");
        assertEquals(1, path.get(0).Value);
        assertEquals(6, path.get(path.size() - 1).Value);
        values = getVertexValues(path);
        assertTrue(
                values.equals(Arrays.asList(1, 3, 6)) ||
                        values.equals(Arrays.asList(1, 2, 4, 6)), // Этот вариант длинее, но добавим на всякий
                "Найденный путь 1->6 не является одним из ожидаемых кратчайших"
        );
        // Скорее всего, будет 1 -> 3 -> 6
        assertEquals(Arrays.asList(1, 3, 6), values, "Ожидается путь 1->3->6");


    }
}