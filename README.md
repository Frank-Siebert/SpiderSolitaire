SpiderSolitaire
===============
reads the savegame of SpiderSolitaire as shipped with Windows XP and tries to solve it.

Solving
-------
The solver assigns a *chaos* value to every gamestate. Sequences of the right suit are no chaos,
other chaos values were determined and tweaked by trial and error.

The solver always processes the GameState with the lowest chaos value,
inserts all possible successor states into its database / hashmap and
continues until a solution is found.

New row deals are forced every 100000 states with no limit after the 5th.

Installation
------------
It is not intended for non-developers.
You will certainly need to set up paths yourself and get a dll like `swt-win32-3235.dll`.
