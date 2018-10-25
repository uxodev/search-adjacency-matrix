# search-adjacency-matrix

#### Does an A* search on an adjacency matrix, finding the shortest path from start node S to end node G or Z.
#### Also prints the frontier nodes and their cumulative costs after each node expansion.

#### Example:
Adjacency matrix:
```
-	A	B	G	S
A	0	2	-1	3
B	1	0	5	3
G	1	1	0	-1
S	4	7	-1	0
```
Node cost:
```
A	4
B	2
G	0
S	6
```
Result:
```
Format:
source node = |queue node:edges from start:via node|
Priority queue after expanding node:
S = |A:8:S| |B:9:S| 
A = |B:8:A| 
B = |G:11:B| 
Goal found: G
Route to goal:
S A B G
```
