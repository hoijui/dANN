#include "DNAJni.h"
#include "JniHelpers.h"
#include "DNA.h"
#include <jni.h>


JNIEXPORT jint JNICALL Java_syncleus_dann_DNA_nativeConstructor__(JNIEnv *Application, jobject SrcObject)
{
	//if the class already has been constructed then just return the already established native id.
	DNA *NativeThis = GetNativeDNA(Application, SrcObject);
	if( NativeThis != 0 )
		return (jint) NativeThis;
	
	//this is truely a new class, generate a global reference
	NativeThis = new DNA();
	return (jint) NativeThis;
}

JNIEXPORT jint JNICALL Java_syncleus_dann_DNA_nativeConstructor__DIIDDZDZ(JNIEnv *Application, jobject SrcObject, jdouble LearningRateToSet, jint MinimumOutgoingToSet, jint MaximumIncommingToSet, jdouble MinimumWeightToSet, jdouble InitialMaxWeightToSet, jboolean UseMinimumWeightToSet, jdouble IncommingDropFactorToSet, jboolean LayeredForwardToSet)
{
	//if the class already has been constructed then just return the already established native id.
	DNA *NativeThis = GetNativeDNA(Application, SrcObject);
	if( NativeThis != 0 )
		return (jint) NativeThis;
	
	//this is truely a new class, generate a global reference
	NativeThis = new DNA(LearningRateToSet, MinimumOutgoingToSet, MaximumIncommingToSet, MinimumWeightToSet, InitialMaxWeightToSet, UseMinimumWeightToSet, IncommingDropFactorToSet, LayeredForwardToSet);
	return (jint) NativeThis;
}

JNIEXPORT void JNICALL Java_syncleus_dann_DNA_nativeDestructor(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	DNA* NativeThis = GetNativeDNA(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	delete NativeThis;
}

JNIEXPORT jdouble JNICALL Java_syncleus_dann_DNA_getLearningRate(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	DNA* NativeThis = GetNativeDNA(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	return NativeThis->LearningRate;
}

JNIEXPORT jint JNICALL Java_syncleus_dann_DNA_getMinimumOutgoing(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	DNA* NativeThis = GetNativeDNA(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	return NativeThis->MinimumOutgoing;
}

JNIEXPORT jint JNICALL Java_syncleus_dann_DNA_getMaximumIncomming(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	DNA* NativeThis = GetNativeDNA(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	return NativeThis->MaximumIncomming;
}

JNIEXPORT jdouble JNICALL Java_syncleus_dann_DNA_getMinimumWeight(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	DNA* NativeThis = GetNativeDNA(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	return NativeThis->MinimumWeight;
}

JNIEXPORT jdouble JNICALL Java_syncleus_dann_DNA_getInitialMaxWeight(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	DNA* NativeThis = GetNativeDNA(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	return NativeThis->InitialMaxWeight;
}

JNIEXPORT jboolean JNICALL Java_syncleus_dann_DNA_getUseMinimumWeight(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	DNA* NativeThis = GetNativeDNA(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	return NativeThis->UseMinimumWeight;
}

JNIEXPORT jdouble JNICALL Java_syncleus_dann_DNA_getIncommingDropFactor(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	DNA* NativeThis = GetNativeDNA(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	return NativeThis->IncommingDropFactor;
}

JNIEXPORT jboolean JNICALL Java_syncleus_dann_DNA_getLayerdForward(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	DNA* NativeThis = GetNativeDNA(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	return NativeThis->LayeredForward;
}

