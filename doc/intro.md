# Introduction to ASCII A-maze-ment

The approach is pretty simple. Once you treat the ASCII maze as a
coordinate grid, and have a notion of legal moves, then you can plug
it into a graph search. The search just needs:

* a start vertex
* a way to know whether a vertex is a goal
* a vertex's children.

To print the solution, draw the end node's ancestors on the map.

```
      ______________ITA
     |         XX XX|
     |________   ___|
     |XX XX XX|XX   |
     |  ___   |     |
     |XX|   XX XX|  |
     |  |__   ___|  |
     |XX|     |     |
Start|__|_____|_____|
```

One curve-ball is the peculiar steps through the maze. It looks like
someone's hopping through it. (Perhaps we don't need to copy that
style, but it certainly makes testing easier, since we can use ITA's
sample solutions unmodified.)

Also, you move through visual whitespace. (Walls are `|` or `_`.) But
actually, you can conceivably stand on a `_`, unlike `|`. You just
can't legally walk through it.


**Why is there also a BFS which isn't used?**

*[BFS = breadth-first search, DFS = depth-first search]*

This is a puzzle, so it's in the sprit of fun: Whenever I implement
DFS, I like to see that BFS is the same, except for using a queue
instead of a stack. (I don't know why; it's just nice to see.)

So, this is a benign exception of my rule to eliminate dead code.

As for preferring DFS, it has better memory consumption than BFS and
all mazes are finite. (ITA states, *"The input is guaranteed to be a
well-formed maze..."*)


**Multiple mazes in one file?**

One example puts 3 mazes in one file. I believe that's just for the
convenience of casual readers; in the absence of needing it,
implementing it might just be another bell & whistle which clutters
the code.

ITA's sample tarball sticks to 1 maze/file.