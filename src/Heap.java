import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Heap {
    public int[] HeapArray; // хранит неотрицательные числа-ключи
    private int End;

    public Heap() {
        HeapArray = null;
        End = 0;
    }

    public void MakeHeap(int[] a, int depth) {
        int size = (int) Math.pow(2, depth + 1) - 1;
        HeapArray = new int[size];
        End = 0;

        for (int i : a) {
            Add(i);
        }
    }

    public int GetMax() {
        if (End == 0 || HeapArray == null || HeapArray.length == 0)
            return -1; // если куча пуста

        int max = HeapArray[0];
        HeapArray[0] = HeapArray[End - 1];
        End--;

        int i = 0;
        while (2 * i + 1 < End) {
            int maxChild = 2 * i + 1;
            if (maxChild + 1 < End && HeapArray[maxChild + 1] > HeapArray[maxChild]) {
                maxChild++;
            }
            if (HeapArray[i] >= HeapArray[maxChild]) {
                break;
            }
            int temp = HeapArray[i];
            HeapArray[i] = HeapArray[maxChild];
            HeapArray[maxChild] = temp;
            i = maxChild;
        }

        return max;
    }

    public boolean Add(int key) {
        if (HeapArray == null || End >= HeapArray.length)
            return false;

        HeapArray[End] = key;
        int i = End;
        while (i > 0 && HeapArray[(i - 1) / 2] < HeapArray[i]) {
            int parent = (i - 1) / 2;
            int temp = HeapArray[parent];
            HeapArray[parent] = HeapArray[i];
            HeapArray[i] = temp;
            i = parent;
        }
        End++;
        return true;
    }

    public int FindMaxInRange(int min, int max) {
        int result = -1;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);

        while (!queue.isEmpty()) {
            int index = queue.poll();
            if (index >= End || HeapArray[index] < min) continue;

            if (HeapArray[index] <= max) {
                result = Math.max(result, HeapArray[index]);
            }

            queue.add(2 * index + 1);
            queue.add(2 * index + 2);
        }

        return result;
    }

    public List<Integer> FindElementsLessThan(int value) {
        List<Integer> result = new ArrayList<>();
        Queue<Integer> currentLevel = new LinkedList<>();
        int lastAdded = -1; // Последняя добавленная вершина

        // Добавляем все листья
        for (int i = (End - 1) / 2 + 1; i < End; i++) {
            if (HeapArray[i] < value) {
                result.add(HeapArray[i]);
                int parent = (i - 1) / 2;
                if (parent != lastAdded) { // Добавляем только если не добавляли подряд
                    currentLevel.add(parent);
                    lastAdded = parent;
                }
            }
        }


        while (!currentLevel.isEmpty()) {
            Queue<Integer> nextLevel = new LinkedList<>();
            lastAdded = -1;

            for (int vertex : currentLevel) {
                if (vertex < 0) continue;

                if (HeapArray[vertex] < value) {
                    result.add(HeapArray[vertex]);
                    int parent = (vertex - 1) / 2;
                    if (parent != lastAdded) {
                        nextLevel.add(parent);
                        lastAdded = parent;
                    }
                }
            }
            currentLevel = nextLevel;
        }

        return result;
    }


    public void MergeHeap(Heap other) {
        if (other == null || other.HeapArray == null) return;
        for (int i = 0; i < other.End; i++) {
            this.Add(other.HeapArray[i]);
        }
    }
}
