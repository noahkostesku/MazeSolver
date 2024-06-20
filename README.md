Explanation of the maze solver:


Each mine is stored in a text file which you can load and it will be displayed graphically so you can see the
mine and watch the movement through the mine once you implement the path-finding algorithm.
Each mine is divided into a grid of square cells. You can walk from one cell to another adjacent
cell but you cannot step onto lava or walk through a wall. There are several different types of
cells which are described and shown below. Some cells allow you to pick up an item such as a
key or a piece of gold ore. From a cell in a mine, there will be up to 4 neighbour cells on which you may be able to walk.
The neighbouring cells are indexes 0 for the north neighbour (above), 1 for the east neighbour
(to the right), 2 for the south neighbour (below), and 3 for the west neighbour (to the left). Note
that some cells will have only two or three neighbours if they are at a corner or along an edge.
However, if they have less than four neighbours, the indexing is still the same. For example, the
starting cell in Figure 2 has only three neighbours since it is along an edge, and its neighbours
are at index 1, 2, and 3. The neighbour that would have been at index 0 does not exist so it has
a "null" 0th neighbour. The other neighbours maintain their indexing (1 is still the east neighbour,
and so on). 

Types of Cells:

Start Cell
Floor Cell - freely walk on floor cells
Exit Cell
Wall Cell - no walking on wall cells
Gold Cell - walk on gold cell and pick up gold
Lava Cell - no walking, gold will be detroyed if walk adjacent to lava
Key Cells (rgb) - pick up key
Locked Door Cells (rgb) - must be unlocked with corresponding colour key
