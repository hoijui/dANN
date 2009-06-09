' svn2cl.vbs - port of svn2cl.sh for windows scripting host.
'              front end of svn2cl.xsl.
'              MSXML is used for transformation.
' 
' Copyright (C) 2006, 2007 Iwasa Kazmi.
' Original script : Copyright (C) 2005, 2006 Arthur de Jong.
' 
' Redistribution and use in source and binary forms, with or without
' modification, are permitted provided that the following conditions
' are met:
' 1. Redistributions of source code must retain the above copyright
'    notice, this list of conditions and the following disclaimer.
' 2. Redistributions in binary form must reproduce the above copyright
'    notice, this list of conditions and the following disclaimer in
'    the documentation and/or other materials provided with the
'    distribution.
' 3. The name of the author may not be used to endorse or promote
'    products derived from this software without specific prior
'    written permission.
' 
' THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
' IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
' WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
' ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
' DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
' DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
' GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
' INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
' IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
' OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
' IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Option Explicit

'----------------------------------------------------------------------------------
' Note for windows users.
' - This script uses system-default encoding for output.
'   If you want to generate a html file, you may have to modify svn2html.xsl so that
'   "charset" is specified properly in the html file.
'   For example, if the system-default encoding is Shift-JIS, modify line
'      <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
'   to
'      <meta http-equiv="Content-Type" content="text/html; charset=shift_jis" />
'----------------------------------------------------------------------------------

Dim VERSION

Dim STRIPPREFIX
Dim LINELEN
Dim GROUPBYDAY
Dim INCLUDEREV
Dim BREAKBEFOREMSG
Dim REPARAGRAPH
Dim SEPARATEDAYLOGS
Dim CHANGELOG
Dim OUTSTYLE
Dim SVNLOGCMD
Dim SVNINFOCMD
Dim AUTHORSFILE
Dim IGNORE_MESSAGE_STARTING
Dim TITLE
Dim REVISION_LINK

Dim Prog
Dim ProgDir
Dim Arguments
Dim I
Dim Arg
Dim TempPath

Dim FS

Dim XSL

' svn2cl version
VERSION = "0.7"

' set default parameters
STRIPPREFIX = "AUTOMATICALLY-DETERMINED"
LINELEN = 75
GROUPBYDAY = "no"
INCLUDEREV = "no"
BREAKBEFOREMSG = "no"
REPARAGRAPH = "no"
SEPARATEDAYLOGS = "no"
CHANGELOG = ""
OUTSTYLE = "cl"
SVNLOGCMD = "svn --verbose --xml log"
SVNINFOCMD = "svn info"
AUTHORSFILE = ""
IGNORE_MESSAGE_STARTING = ""
TITLE = "ChangeLog"
REVISION_LINK = "#r"

Const TmpXmlFile = "__svnlog.xml"

' do command line checking
Prog = WScript.ScriptName
Set Arguments = WScript.Arguments

For I = 0 To Arguments.Count - 1
	Arg = Arguments(I)

	Select Case Arg

	Case "--strip-prefix"
		I = I + 1
		If I < Arguments.Count Then
			STRIPPREFIX = Arguments(I)
		Else
			WScript.Echo Prog & ": option requires an argument -- " & Arg
			WScript.Quit 1
		End If

	Case "--linelen"
		I = I + 1
		If I < Arguments.Count Then
			LINELEN = Arguments(I)
		Else
			WScript.Echo Prog & ": option requires an argument -- " & Arg
			WScript.Quit 1
		End If

	Case "--group-by-day"
		GROUPBYDAY = "yes"

	Case "--separate-daylogs"
		SEPARATEDAYLOGS = "yes"

	Case "-i", "--include-rev"
		INCLUDEREV = "yes"

	Case "--break-before-msg", "--breaks-before-msg"
		If I + 1 < Arguments.Count And IsInteger(Arguments(I+1)) Then
			I = I + 1
			BREAKBEFOREMSG = Arguments(I)
		Else
			BREAKBEFOREMSG = "yes"
		End If

	Case "--reparagraph"
		REPARAGRAPH = "yes"

	Case "--title"
		I = I + 1
		If I < Arguments.Count Then
			TITLE = Arguments(I)
		Else
			WScript.Echo Prog & ": option requires an argument -- " & Arg
			WScript.Quit 1
		End If

	Case "--revision-link"
		I = I + 1
		If I < Arguments.Count Then
			REVISION_LINK = Arguments(I)
		Else
			WScript.Echo Prog & ": option requires an argument -- " & Arg
			WScript.Quit 1
		End If

	Case "--ignore-message-starting"
		I = I + 1
		If I < Arguments.Count Then
			IGNORE_MESSAGE_STARTING = Arguments(I)
		Else
			WScript.Echo Prog & ": option requires an argument -- " & Arg
			WScript.Quit 1
		End If

	Case "-f", "--file", "-o", "--output"
		I = I + 1
		If I < Arguments.Count Then
			CHANGELOG = Arguments(I)
		Else
			WScript.Echo Prog & ": option requires an argument -- " & Arg
			WScript.Quit 1
		End If

	Case "--stdout"
		CHANGELOG = "-"

	Case "--authors"
		I = I + 1
		If I < Arguments.Count Then
			AUTHORSFILE = Arguments(I)
		Else
			WScript.Echo Prog & ": option requires an argument -- " & Arg
			WScript.Quit 1
		End If

	Case "--html"
		OUTSTYLE = "html"

	Case "-r", "--revision", "--targets", "--limit"
		' add these as extra options to the command (with argument)
		I = I + 1
		If I < Arguments.Count Then
			SVNLOGCMD = SVNLOGCMD & " " & Arg & " """ & Arguments(I) & """"
		Else
			WScript.Echo Prog & ": option requires an argument -- " & Arg
			WScript.Quit 1
		End If

	Case "--username", "--password", "--config-dir"
		' add these as extra options to the command (with argument)
		I = I + 1
		If I < Arguments.Count Then
			SVNLOGCMD = SVNLOGCMD & " " & Arg & " """ & Arguments(I) & """"
			' also add to svn info command
			SVNINFOCMD = SVNINFOCMD & " " & Arg & " """ & Arguments(I) & """"
		Else
			WScript.Echo Prog & ": option requires an argument -- " & Arg
			WScript.Quit 1
		End If

	Case "--stop-on-copy"
		' add these as simple options
		SVNLOGCMD = SVNLOGCMD & " " & Arg

	Case "--no-auth-cache", "--non-interactive"
		' add these as simple options
		SVNLOGCMD = SVNLOGCMD & " " & Arg
		' also add to svn info command
		SVNINFOCMD = SVNINFOCMD & " " & Arg

	Case "-V", "--version"
		WScript.Echo Prog & " " & VERSION & vbCrLf & _
					"Original Script: Written by Arthur de Jong." & vbCrLf & vbCrLf & _
					"Copyright (C) 2005, 2006, 2007 Arthur de Jong." & vbCrLf & _
					"This is free software; see the source for copying conditions.  There is NO" & vbCrLf & _
				    "warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE."
		WScript.Quit 0

	Case "-h", "--help", "/?"
		ShowUsage
		WScript.Quit 0

	Case Else
		If Left(Arg, 1) = "-" Then
			WScript.Echo Prog & ": invalid option -- " & Arg & vbCrLf & _
						"Try `" & Prog & " --help' for more information."
			WScript.Quit 1
		Else
			SVNLOGCMD = SVNLOGCMD & " """ & Arg & """"
			SVNINFOCMD = SVNINFOCMD & " """ & Arg & """"
		End If

	End Select
Next


Set FS = WScript.CreateObject("Scripting.FileSystemObject")

' find the directory that this script resides in
ProgDir = FS.GetParentFolderName(WScript.ScriptFullName)
XSL = FS.BuildPath(ProgDir, "svn2" & OUTSTYLE & ".xsl")


' Note: colon-separated authors file is not supported.

' find the absolute path of the authors file
TempPath = FS.GetAbsolutePathName(AUTHORSFILE)
If FS.FileExists(TempPath) Then
	AUTHORSFILE = TempPath
Else
	If FS.GetParentFolderName(AUTHORSFILE) = "" Then
		TempPath = FS.BuildPath(ProgDir, AUTHORSFILE)
		If FS.FileExists(TempPath) Then
			AUTHORSFILE = TempPath
		End If
	End If
End If

' if no filename was specified, make one up
If CHANGELOG = "" Then
	CHANGELOG = "ChangeLog"
	If OUTSTYLE <> "cl" Then
		CHANGELOG = CHANGELOG & "." & OUTSTYLE
	Else
		CHANGELOG = CHANGELOG & ".txt"
	End If
End If

' try to determin a prefix to strip from all paths
If STRIPPREFIX = "AUTOMATICALLY-DETERMINED" Then
	DetermineStripPrefix
	If STRIPPREFIX = "AUTOMATICALLY-DETERMINED" Then
		STRIPPREFIX = "/"
	End If
End If

' actually run the command we need
If RunSvn() Then
	Transform
End If

' clean up temporary files
If FS.FileExists(TmpXmlFile) Then FS.DeleteFile TmpXmlFile

' we're done (the previous command could return false)
WScript.Quit 0


Function RunSvn()
	Dim WshShell
	Dim ResultCode
	Set WshShell = WScript.CreateObject("WScript.Shell")
	ResultCode = WshShell.Run("%COMSPEC% /C " & SVNLOGCMD & " > " & TmpXmlFile, 0, True)
	If ResultCode = 0 Then
		RunSvn = True
	Else
		RunSvn = False
	End If
End Function


Sub Transform()
	Dim XmlDocument
	Dim XslStylesheet
	Dim XslTemplate
	Dim XslProcessor
	Dim XmlLoaded
	Dim XslLoaded
	Dim Output

	Set XmlDocument = WScript.CreateObject("MSXML2.FreeThreadedDOMDocument.3.0")
	Set XslStylesheet = WScript.CreateObject("MSXML2.FreeThreadedDOMDocument.3.0")
	Set XslTemplate = WScript.CreateObject("MSXML2.XSLTemplate.3.0")

	XmlDocument.async = False
	XmlDocument.validateOnParse = False
	XmlDocument.preserveWhiteSpace = True
	XmlLoaded = XmlDocument.load(TmpXmlFile)

	If XmlLoaded Then
		XslStylesheet.async = False
		XslStylesheet.validateOnParse = False
		XslStylesheet.preserveWhiteSpace = True
		XslLoaded = XslStylesheet.load(XSL)

		If XslLoaded Then
			XslTemplate.stylesheet = XslStylesheet
			Set XslProcessor = XslTemplate.createProcessor
			XslProcessor.input = XmlDocument

			XslProcessor.addParameter "strip-prefix", STRIPPREFIX
			XslProcessor.addParameter "linelen", LINELEN
			XslProcessor.addParameter "groupbyday", GROUPBYDAY
			XslProcessor.addParameter "separate-daylogs", SEPARATEDAYLOGS
			XslProcessor.addParameter "include-rev", INCLUDEREV
			XslProcessor.addParameter "breakbeforemsg", BREAKBEFOREMSG
			XslProcessor.addParameter "reparagraph", REPARAGRAPH
			XslProcessor.addParameter "authorsfile", AUTHORSFILE
			XslProcessor.addParameter "title", TITLE
			XslProcessor.addParameter "revision-link", REVISION_LINK
			XslProcessor.addParameter "ignore-message-starting", IGNORE_MESSAGE_STARTING

			XslProcessor.transform
			Output = XslProcessor.output

			If CHANGELOG = "-" Then
				OutputToStdOut Output
			Else
				OutputToFile Output
			End If
		Else
			ErrorReport XslStylesheet.parseError
		End If
	Else
		ErrorReport XmlDocument.parseError
	End If
End Sub


Sub OutputToStdOut(text)
	'Note: WScript.exe doesn't support StdOut. Use CScript.exe.
	WScript.StdOut.Write text
End Sub

Sub OutputToFile(text)
	Dim OutFile

	Const TristateFalse = 0		'use ASCII
	Const TristateTrue = -1		'use UNICODE (UTF-16)
	Const TristateUseDefault = -2	'use system-default encoding

	Set OutFile = FS.OpenTextFile(CHANGELOG, 2, True, TristateUseDefault)
	OutFile.Write text
	OutFile.Close
End Sub

Sub DetermineStripPrefix()
	Dim WshShell
	Dim WshEnv
	Dim ResultCode
	Dim InfoFile
	Dim LineStr
	Dim URL
	Dim Root
	Dim Sep

	Const TmpInfoFile = "__svninf.txt"

	Set WshShell = WScript.CreateObject("WScript.Shell")
	Set WshEnv = WshShell.Environment("Process")
	WshEnv("LANG") = "C"	'avoid localized text
	ResultCode = WshShell.Run("%COMSPEC% /C " & SVNINFOCMD & " > " & TmpInfoFile, 0, True)
	If ResultCode = 0 Then
		URL = ""
		Root = ""
		Set InfoFile = FS.OpenTextFile(TmpInfoFile, 1, False, 0)
		Do Until InfoFile.AtEndOfStream
			LineStr = InfoFile.ReadLine
			If Left(LineStr, 5) = "URL: " Then
				URL = Mid(LineStr, 6)
			ElseIf Left(LineStr, 17) = "Repository Root: " Then
				Root = Mid(LineStr, 18)
			End If
		Loop
		InfoFile.Close

		If URL <> "" Then
			If Root <> "" Then
				STRIPPREFIX = Mid(URL, Len(Root) + 2)
			Else
				Sep = InStr(URL, "/")
				If Sep > 0 Then
					STRIPPREFIX = Mid(URL, Sep + 1)
				End If
			End If
		End If
	End If

	If FS.FileExists(TmpInfoFile) Then FS.DeleteFile(TmpInfoFile)
End Sub

Sub ErrorReport(err)
	WScript.Echo err.reason & vbCrLf & _
		"URL: " & err.url & vbCrLf & _
		"Line: " & CStr(err.line) & vbCrLf & _
		"Text: " & err.srcText
End Sub

Sub ShowUsage()
	Dim Msg

	Msg = "Usage: " & Prog & " [OPTION]... [PATH]..." & vbCrLf
	Msg = Msg & "Generate a ChangeLog from a subversion repository." & vbCrLf
	Msg = Msg & vbCrLf
	Msg = Msg & "  --strip-prefix NAME  prefix to strip from all entries, defaults" & vbCrLf
	Msg = Msg & "                       path inside the repository" & vbCrLf
	Msg = Msg & "  --linelen NUM        maximum length of an output line" & vbCrLf
	Msg = Msg & "  --group-by-day       group changelog entries by day" & vbCrLf
	Msg = Msg & "  --separate-daylogs   put a blank line between grouped by day entries" & vbCrLf
	Msg = Msg & "  -i, --include-rev    include revision numbers" & vbCrLf
	Msg = Msg & "  --break-before-msg [NUM] add a line break (or multiple breaks)" & vbCrLf
	Msg = Msg & "                       between the paths and the log message" & vbCrLf
	Msg = Msg & "  --reparagraph        rewrap lines inside a paragraph" & vbCrLf
	Msg = Msg & "  --title TITLE        title used in html file" & vbCrLf
	Msg = Msg & "  --revision-link NAME link revision numbers in html output" & vbCrLf
	Msg = Msg & "  --ignore-message-starting STRING" & vbCrLf
	Msg = Msg & "                       ignore messages starting with the string" & vbCrLf
	Msg = Msg & "  -o, --output FILE    output to FILE instead of ChangeLog" & vbCrLf
	Msg = Msg & "  -f, --file FILE      alias for -o, --output" & vbCrLf
	Msg = Msg & "  --stdout             output to stdout instead of ChangeLog" & vbCrLf
	Msg = Msg & "  --authors FILE       file to read for authors" & vbCrLf
	Msg = Msg & "  --html               output as html instead of plain text" & vbCrLf
	Msg = Msg & "  -h, --help           display this help and exit" & vbCrLf
	Msg = Msg & "  -V, --version        output version information and exit" & vbCrLf
	Msg = Msg & vbCrLf
	Msg = Msg & "PATH arguments and the following options are passed to the svn log" & vbCrLf
	Msg = Msg & "command: -r, --revision, --targets --stop-on-copy, --username," & vbCrLf
	Msg = Msg & "--password, --no-auth-cache, --non-interactive, --config-dir and" & vbCrLf
	Msg = Msg & "--limit (see \`svn help log' for more information)."

	WScript.Echo Msg
End Sub

Function IsInteger(s)
	Dim I
	If Len(s) = 0 Then
		IsInteger = False
		Exit Function
	End If

	For I = 1 To Len(s)
		If InStr("0123456789", Mid(s, I)) = 0 Then
			IsInteger = False
			Exit Function
		End If
	Next

	IsInteger = True
End Function

