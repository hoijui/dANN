#!/bin/sh

#check to make sure it has an argument
if [ $# -lt 1 ]
then
	echo "must specify the directory in tags to package for distribution."
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
rm -rf ./tmp/%1/java_dann_examples/src
rm -rf ./tmp/%1/java_dann_examples/build/classes

# tarball binary distribution
tar -czvf $1-bin.tar.gz -C ./tmp/ $1

#remove tmp directory
rm -rf ./tmp

exit 0
