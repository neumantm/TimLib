# TimLib
This is collection of useful methods and classes made by me.

It is licensed under Mozilla Public License Version 2.0. See [LICENSE](LICENSE) for more information.

I know that, there some are popular (and probably better) alternatives for at least some of them.
But I just wanted to do it my self. And most of them are extractions from other projects, 
so they exist anyway and i just collect them here.

I will be working on some better documentation at some point.

Compiled jars can be found in the [Releases](https://github.com/neumantm/TimLib/releases).

To package jars: `mvn compile package`

To get this jar into mvn run:
`mvn install:install-file -Dfile=<path-to-file>`

## Overview
### Misc
Misc contains all static methods that can't be linked to any specifc class
Currently contains methods for:
 - case(in)sensitive searching of Strings in a String Array
### Log
A class for logging with multiple log levels and the ability to log complete exceptions.
### Conf
A class for managing configs on disk.
### Data Manager
A few classes for saving and loading object data to/from disk.
These use the good old java ObjectOutputStream and ObjectInputStream.
I'm currently debating if I still want to continue using and maintaining this, 
or switch to a more state of art json serialization.
