
del DuplicateFinder.class
call SET_JAVA_HOME.BAT
%JAVA_HOME%\bin\javac                DuplicateFinder.java
%JAVA_HOME%\bin\java -cp . -Xmx1024m DuplicateFinder > dup1.log
PAUSE
