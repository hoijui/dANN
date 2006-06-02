all: alllibrary
alllibrary: clean compilelibrary linklibrary
allexecutable: clean compile linkexecutable

clean:
	rm -f ./*.o
	rm -f ./libdann.so
	rm -f ./dann

compile:
	g++ -c -g -I/opt/sun-jdk-1.4.2.10/include/ -I/opt/sun-jdk-1.4.2.10/include/linux ./*.cpp
compilelibrary:
	g++ -c -g -fPIC -D_REENTRANT -I/opt/sun-jdk-1.4.2.10/include/ -I/opt/sun-jdk-1.4.2.10/include/linux ./*.cpp


linklibrary:
	g++ -g -shared *.o -Istdc++ -o libdann.so

linkexecutable:
	g++ -g -o dann ./*.o

