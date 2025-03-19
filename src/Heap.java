class Heap
{
    public int [] HeapArray; // хранит неотрицательные числа-ключи
    private int End;
    public Heap() { HeapArray = null; End = 0;}

    public void MakeHeap(int[] a, int depth)
    {
        int size = (int)Math.pow(2, depth + 1) - 1;
        HeapArray = new int[size];

        for (int i : a) Add(i);
    }

    public int GetMax()
    {
        if (End == 0 || HeapArray.length == 0)
            return -1; // если куча пуста

        int max = HeapArray[0];
        HeapArray[0] = HeapArray[End - 1];
        End--;

        for (int i = 0, maxChild; 2 * i + 2 < End && HeapArray[i] < Math.max(HeapArray[2 * i + 1], HeapArray[2 * i + 2]); i = maxChild)
        {
            maxChild = HeapArray[2 * i + 1] > HeapArray[2 * i + 2] ? 2 * i + 1 : 2 * i + 2;
            int temp = HeapArray[maxChild];
            HeapArray[maxChild] = HeapArray[i];
            HeapArray[i] = temp;
        }

        return max;
    }

    public boolean Add(int key)
    {
        if (End >= HeapArray.length)
            return false;

        HeapArray[End] = key;

        for (int i = End, parent; 0 < i && HeapArray[(i - 1) / 2] < HeapArray[i]; i = parent)
        {
            parent = (i - 1) / 2;
            int temp = HeapArray[parent];
            HeapArray[parent] = HeapArray[i];
            HeapArray[i] = temp;
        }

        End++;
        return true;
    }

}