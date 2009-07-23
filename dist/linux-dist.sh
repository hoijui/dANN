#!/bin/sh

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


#pull the arguments
if [ $# -lt 2 ]
then
	ARCH_NAME="java_dann-trunk"
	SVN_ROOT="trunk/projects"
else
	ARCH_NAME=$2
	SVN_ROOT="tags/$ARCH_NAME"
fi

if [ $# -lt 1 ]
then
	echo "usage: $0 <output dir> [tag name]"
else
	OUT_DIR=$1
fi

SVN_URL="svn://svn.syncleus.com/dANN"


#check out tag
svn co $SVN_URL/$SVN_ROOT ./tmp/$ARCH_NAME-co
svn export $SVN_URL/$SVN_ROOT ./tmp/$ARCH_NAME

#Generate ChangeLog
svn2cl -i --group-by-day --authors authors.xml -o ./tmp/$ARCH_NAME/java_dann/doc/ChangeLog ./tmp/$ARCH_NAME-co/java_dann
svn2cl -i --group-by-day --authors authors.xml -o ./tmp/$ARCH_NAME/java_dann_examples/doc/ChangeLog ./tmp/$ARCH_NAME-co/java_dann_examples

# tarball source distribution
tar -czvf $OUT_DIR/$ARCH_NAME-src.tar.gz -C ./tmp/ $ARCH_NAME

#remove dist dir for binary distribution
rm -rf ./tmp/$ARCH_NAME/dist

#compile core library
ant -buildfile ./tmp/$ARCH_NAME/java_dann/build.xml build-all javadoc

#clean core library to include only files for binary distribution
rm -rf ./tmp/$ARCH_NAME/java_dann/src
rm -rf ./tmp/$ARCH_NAME/java_dann/build/classes
rm -rf ./tmp/$ARCH_NAME/java_dann/build/coverage
rm -rf ./tmp/$ARCH_NAME/java_dann/build/tests
rm -f ./tmp/$ARCH_NAME/java_dann/cobertura.ser

#compile examples application
ant -buildfile ./tmp/$ARCH_NAME/java_dann_examples/build.xml all

#clean examples application to include only files for binary distribution
rm -rf ./tmp/$ARCH_NAME/java_dann_examples/src
rm -rf ./tmp/$ARCH_NAME/java_dann_examples/build/classes

# tarball binary distribution
tar -czvf $OUT_DIR/$ARCH_NAME-bin.tar.gz -C ./tmp/ $ARCH_NAME

# tarball javadocs
tar -czvf $OUT_DIR/$ARCH_NAME-javadoc.tar.gz -C ./tmp/$ARCH_NAME/java_dann/build/ javadoc/

#remove tmp directory
rm -rf ./tmp

#exit 0
