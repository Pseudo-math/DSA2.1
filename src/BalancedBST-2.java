//6. Строим сбалансированные двоичные деревья поиска (2)
//1. В процессе построения дерева проставьте глубину каждого узла: у корня она равна 0, а у каждого потомка на единицу выше, чем у родителя.
//Ссылка на код: https://github.com/Pseudo-math/DSA2.1/blob/master/src/BalancedBST.java#L11
//Ссылка на тесты: https://github.com/Pseudo-math/DSA2.1/blob/master/Test/BalancedBSTTest.java#L55
// Сложность O(1) обоих типов.
//2.* Добавьте метод проверки, действительно ли дерево получилось правильным: для каждого узла ключ левого потомка должен быть меньше его ключа, а ключ правого потомка должен быть больше или равен ключу родителя.
//Ссылка на код: https://github.com/Pseudo-math/DSA2.1/blob/master/src/BalancedBST.java#L68
//Ссылка на тесты: https://github.com/Pseudo-math/DSA2.1/blob/master/Test/BalancedBSTTest.java#L169
// Метод IsRight имеет временную сложность O(n) и пространственную сложность O(h), где n — число узлов, а h — высота дерева, так как использует рекурсивный обход дерева.
//3.* Добавьте метод проверки, действительно ли дерево получилось сбалансированным, что определяется тремя правилами:
//- правое поддерево каждого узла сбалансировано;
//- левое поддерево каждого узла сбалансировано;
//- разница между глубинами левого и правого поддеревьев не превышает единицы (или, проще говоря, левое и правое поддеревья равны по длинам или отличаются не более чем на одну ветку).
//Ссылка на код: https://github.com/Pseudo-math/DSA2.1/blob/master/src/BalancedBST.java#L81
//Ссылка на тесты: https://github.com/Pseudo-math/DSA2.1/blob/master/Test/BalancedBSTTest.java#L120
// Метод IsBalanced также имеет временную сложность O(n) и пространственную сложность O(h), поскольку выполняет рекурсивный обход и вычисляет высоты поддеревьев.