#!/bin/sh

#check to make sure it has an argument
if [ $# -lt 1 ]
then
	echo "must specify the directory in tags to package for distribution."
	exit 1
fi

if hash svn2cl 2> /dev/null; then
	echo "svn2cl found, good..."
else
	echo "svn2cl must be on your path"
	exit 1
fi

if hash svn 2> /dev/null; then
	echo "svn found, good..."
else
	echo "svn must be on your path"
	exit 1
fi

if hash tar 2> /dev/null; then
	echo "tar found, good..."
else
	echo "tar must be on your path"
	exit 1
fi

if hash ant 2> /dev/null; then
	echo "ant found, good..."
else
	echo "ant must be on your path"
	exit 1
fi

if hash rm 2> /dev/null; then
	echo "rm found, good..."
else
	echo "rm must be on your path"
	exit 1
fi



#check out tag
svn co svn://svn.syncleus.com/dANN/tags/$1 ./tmp/$1-co
svn export svn://svn.syncleus.com/dANN/tags/$1 ./tmp/$1

#Generate ChangeLog
svn2cl -i --group-by-day --authors authors.xml -o ./tmp/$1/java_dann/doc/ChangeLog ./tmp/$1-co/java_dann
svn2cl -i --group-by-day --authors authors.xml -o ./tmp/$1/java_dann_examples/doc/ChangeLog ./tmp/$1-co/java_dann_examples

# tarball source distribution
tar -czvf $1-src.tar.gz -C ./tmp/ $1

#remove dist dir for binary distribution
rm -rf ./tmp/$1/dist

#compile core library
ant -buildfile ./tmp/$1/java_dann/build.xml build-all javadoc

#clean core library to include only files for binary distribution
rm -rf ./tmp/$1/java_dann/src
rm -rf ./tmp/$1/java_dann/build/classes
rm -rf ./tmp/$1/java_dann/build/coverage
rm -rf ./tmp/$1/java_dann/build/tests
rm -f ./tmp/$1/java_dann/cobertura.ser

#compile examples application
ant -buildfile ./tmp/$1/java_dann_examples/build.xml all

#clean examples application to include only files for binary distribution
rm -rf ./tmp/$1/java_dann_examples/src
rm -rf ./tmp/$1/java_dann_examples/build/classes

# tarball binary distribution
tar -czvf $1-bin.tar.gz -C ./tmp/ $1

# tarball javadocs
tar -czvf $1-javadoc.tar.gz -C ./tmp/$1/java_dann/build/ javadoc/

#remove tmp directory
rm -rf ./tmp

exit 0
