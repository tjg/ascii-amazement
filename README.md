# ASCII A-maze-ment

Solution to ITA Software's ["ASCII A-maze-ment"
puzzle](http://www.itasoftware.com/careers/puzzle_archive.html).

It solves ASCII mazes like this:


![alt text](https://github.com/tjg/ascii-amazement/raw/master/resources/images/puzzle_archive_ascii_top.gif "Unsolved Maze")

Its output looks like:


![alt text](https://github.com/tjg/ascii-amazement/raw/master/resources/images/puzzle_archive_ascii_lower.gif "Solved Maze")



## Usage

Get help:

```bash
~/ascii-amazement$ lein run -- -h
 Switches      Default  Desc
 --------      -------  ----
 -h, --help    false    Show help
 -i, --input   false    Input maze path
 -o, --output  false    Output maze path 
```


Solve one of ITA's sample puzzles; save it to `test-output.txt`:

```bash
~/ascii-amazement$ lein run -- -i resources/mazes/input1.txt -o test-output.txt
```


Check that a solution is identical to ITA's solution:

```bash
~/ascii-amazement$ lein run -- -i resources/mazes/input6.txt -o test-output.txt && \
                   diff test-output.txt resources/mazes/output6.txt
```


Run unit-tests:

```bash
~/ascii-amazement$ lein midje
All checks (6) succeeded.
```



## License

Copyright © 2013 Tj Gabbour

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
