import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HeapTest {

    @Test
    public void testEmptyHeap() {
        Heap heap = new Heap();
        assertEquals(-1, heap.GetMax()); // Куча пуста, должен вернуть -1
        assertFalse(heap.Add(10)); // Нельзя добавить в неинициализированную кучу
    }

    @Test
    public void testSingletonHeap() {
        Heap heap = new Heap();
        heap.MakeHeap(new int[]{15}, 0); // Только один элемент

        assertEquals(15, heap.GetMax());
        assertEquals(-1, heap.GetMax()); // После удаления элемента куча пуста
    }

    @Test
    public void testMakeHeap() {
        Heap heap = new Heap();
        int[] values = {10, 20, 5, 30, 25};
        heap.MakeHeap(values, 2);

        assertNotNull(heap.HeapArray);
        assertEquals(7, heap.HeapArray.length);
        assertEquals(30, heap.HeapArray[0]); // Корень должен быть максимумом
    }

    @Test
    public void testAdd() {
        Heap heap = new Heap();
        heap.MakeHeap(new int[]{10, 20, 5}, 2);

        assertTrue(heap.Add(25));
        assertEquals(25, heap.HeapArray[0]);

        assertTrue(heap.Add(35));
        assertEquals(35, heap.HeapArray[0]);

        assertFalse(heap.Add(40)); // Проверка, что не добавляется, если куча заполнена
    }

    @Test
    public void testGetMax() {
        Heap heap = new Heap();
        heap.MakeHeap(new int[]{10, 20, 5, 30, 25}, 2);

        assertEquals(30, heap.GetMax());
        assertEquals(25, heap.GetMax());
        assertEquals(20, heap.GetMax());
        assertEquals(10, heap.GetMax());
        assertEquals(5, heap.GetMax());
        assertEquals(-1, heap.GetMax()); // Куча должна быть пуста
    }

}
