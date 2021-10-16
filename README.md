# mini-plugins
A repository of mini plugins for Bukkit.

## why not use alt-java like Kotlin and Scala?
This is because it avoids the problem that if two or more alt-java plugins are installed, each with a different version of the standard library, the plugin will refer to a version of the standard library that is not its own.
I have tried to solve the problem by relocating the standard library at build time, but it has not worked. I consider replacing it written in alt-java once the problem is solved.

## plugins
- Airfoil
- AloneWithSunrise
- DeepMining
- Oakin
- PaperDoll
