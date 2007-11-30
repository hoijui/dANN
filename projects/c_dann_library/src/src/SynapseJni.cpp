#include "SynapseJni.h"
#include "JniHelpers.h"
#include "Synapse.h"
#include <jni.h>


JNIEXPORT jint JNICALL Java_syncleus_dann_Synapse_nativeConstructor(JNIEnv *Application, jobject SrcObject, jobject SourceNeuron, jobject DestinationNeuron, jdouble InitialWeight)
{
	//if the class already has been constructed then just return the already established native id.
	Synapse *NativeThis = GetNativeSynapse(Application, SrcObject);
	if( NativeThis != 0 )
		return (jint) NativeThis;
	
	//this is truely a new class, generate a global reference
	NativeThis = new Synapse(GetNativeNeuron(Application, SourceNeuron), GetNativeNeuron(Application, DestinationNeuron), InitialWeight);
	return (jint) NativeThis;
}



JNIEXPORT void JNICALL Java_syncleus_dann_Synapse_nativeDestructor(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	Synapse* NativeThis = GetNativeSynapse(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	delete NativeThis;
}


JNIEXPORT jdouble JNICALL Java_syncleus_dann_Synapse_getCurrentWeight(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	Synapse* NativeThis = GetNativeSynapse(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	return NativeThis->GetCurrentWeight();
}



JNIEXPORT jobject JNICALL Java_syncleus_dann_Synapse_getDestinationNeuron(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	Synapse* NativeThis = GetNativeSynapse(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	Neuron* NewJavaObject = NativeThis->GetDestinationNeuron();

	jclass NewJavaClass = Application->FindClass("syncleus/dann/Neuron");
	jmethodID NewJavaConstructor = Application->GetMethodID(NewJavaClass, "<init>", "(I)V");
	return Application->NewObject(NewJavaClass, NewJavaConstructor, (jint)NewJavaObject);
}



JNIEXPORT jobject JNICALL Java_syncleus_dann_Synapse_getSourceNeuron(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	Synapse* NativeThis = GetNativeSynapse(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	Neuron* NewJavaObject = NativeThis->GetSourceNeuron();

	jclass NewJavaClass = Application->FindClass("syncleus/dann/Neuron");
	jmethodID NewJavaConstructor = Application->GetMethodID(NewJavaClass, "<init>", "(I)V");
	return Application->NewObject(NewJavaClass, NewJavaConstructor, (jint)NewJavaObject);
}



JNIEXPORT jdouble JNICALL Java_syncleus_dann_Synapse_calculateOutput(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	Synapse* NativeThis = GetNativeSynapse(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	return NativeThis->CalculateOutput();
}



JNIEXPORT void JNICALL Java_syncleus_dann_Synapse_learnWeight(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	Synapse* NativeThis = GetNativeSynapse(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	NativeThis->LearnWeight();
}



JNIEXPORT jdouble JNICALL Java_syncleus_dann_Synapse_calculateDifferential(JNIEnv *Application, jobject SrcObject)
{
	//obtain the native this for the src object
	Synapse* NativeThis = GetNativeSynapse(Application, SrcObject);
	if(NativeThis == 0)
		throw 0;

	return NativeThis->CalculateDifferential();
}



