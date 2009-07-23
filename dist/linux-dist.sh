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
if [ $# -lt 3 ]
then
	ARCH_NAME="java_dann-trunk"
	SVN_ROOT="trunk/projects"
else
	if [ $# -lt 4 ]
	then
		ARCH_NAME=$3
		SVN_ROOT="tags/$ARCH_NAME"
	else
		ARCH_NAME=$4
		SVN_ROOT="$3/$ARCH_NAME"
	fi
fi

if [ $# -lt 2 ]
then
	REV=HEAD
else
	REV=$2
fi

if [ $# -lt 1 ]
then
	echo "usage: $0 <output dir> [revision] [tags|branches] [tag/branch name]"
	echo "       $0 <output dir> [revision] [tag name]"
	exit 1
else
	OUT_DIR=$1
fi

SVN_URL="svn://svn.syncleus.com/dANN"


#check out tag
svn co -r $REV $SVN_URL/$SVN_ROOT ./tmp/$ARCH_NAME-co
svn export -r $REV $SVN_URL/$SVN_ROOT ./tmp/$ARCH_NAME

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
