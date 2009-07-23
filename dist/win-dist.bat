@echo off

REM Make sure the %1 argument is set
if "%1"=="" goto :abort
goto :begin

:abort
echo "must specify the directory in tags to package for distribution."
goto :EOF

REM Process
:begin

REM check out tag
svn co svn://svn.syncleus.com/dANN/tags/%1 .\tmp\%1-co
svn export svn://svn.syncleus.com/dANN/tags/%1 .\tmp\%1

REM Generate ChangeLog
CScript.exe svn2cl.vbs -i --group-by-day --authors authors.xml -o .\tmp\%1\java_dann\doc\ChangeLog .\tmp\%1-co\java_dann
CScript.exe svn2cl.vbs -i --group-by-day --authors authors.xml -o .\tmp\%1\java_dann_examples\doc\ChangeLog .\tmp\%1-co\java_dann_examples

REM ZIP up source distribution
cd .\tmp
zip -r9 ..\%1-src.zip .\%1
cd ..

REM clean dist directory
rmdir /S /Q .\tmp\%1\dist

REM compile core library
call ant -buildfile .\tmp\%1\java_dann\build.xml build-all javadoc

REM clean core library to include only files for binary distribution
rmdir /S /Q .\tmp\%1\java_dann\src
rmdir /S /Q .\tmp\%1\java_dann\build\classes
rmdir /S /Q .\tmp\%1\java_dann\build\coverage
rmdir /S /Q .\tmp\%1\java_dann\build\tests
rm /F /Q .\tmp\%1\java_dann\cobertura.ser

REM compile examples application
call ant -buildfile .\tmp\%1\java_dann_examples\build.xml all

REM clean examples application to include only files for binary distribution
rmdir /S /Q .\tmp\%1\java_dann_examples\src
rmdir /S /Q .\tmp\%1\java_dann_examples\build\classes

REM ZIP up binary distribution
cd .\tmp
zip -r9 ..\%1-bin.zip .\%1
cd ..

REM ZIP up javadocs
cd .\tmp\%1\java_dann\build\
zip -r9 ..\%1-javadoc.zip .\javadoc
cd ..\..\..\..

REM remove the checked out files
rmdir /S /Q .\tmp

:EOF

@PAUSE
