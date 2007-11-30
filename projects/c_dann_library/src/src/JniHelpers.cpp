#include "JniHelpers.h"

//static prototypes
static int GetNativeId(JNIEnv *Application, jobject JavaObject);

Synapse* GetNativeSynapse(JNIEnv *Application, jobject JavaSynapse)
{
	//if the class already has been constructed then just return the already established native id.
	jint CurrentNativeId = GetNativeId(Application, JavaSynapse);
	if(CurrentNativeId != 0)
		return (Synapse*)CurrentNativeId;

	return 0;
}


Neuron* GetNativeNeuron(JNIEnv *Application, jobject JavaNeuron)
{
	//if the class already has been constructed then just return the already established native id.
	jint CurrentNativeId = GetNativeId(Application, JavaNeuron);
	if(CurrentNativeId != 0)
		return (Neuron*)CurrentNativeId;

	return 0;
}


Layer* GetNativeLayer(JNIEnv *Application, jobject JavaLayer)
{
	//if the class already has been constructed then just return the already established native id.
	jint CurrentNativeId = GetNativeId(Application, JavaLayer);
	if(CurrentNativeId != 0)
		return (Layer*)CurrentNativeId;

	return 0;
}

NeuralNet* GetNativeNeuralNet(JNIEnv *Application, jobject JavaLayer)
{
	//if the class already has been constructed then just return the already established native id.
	jint CurrentNativeId = GetNativeId(Application, JavaLayer);
	if(CurrentNativeId != 0)
		return (NeuralNet*)CurrentNativeId;

	return 0;
}

DNA* GetNativeDNA(JNIEnv *Application, jobject JavaDNA)
{
	//if the class already has been constructed then just return the already established native id.
	jint CurrentNativeId = GetNativeId(Application, JavaDNA);
	if(CurrentNativeId != 0)
		return (DNA*)CurrentNativeId;

	return 0;
}



static int GetNativeId(JNIEnv *Application, jobject JavaObject)
{
	jclass SrcClass = Application->GetObjectClass(JavaObject);
	jmethodID SrcGetNativeIdMethod = Application->GetMethodID(SrcClass, "getNativeId", "()I");
	return (int) Application->CallIntMethod(JavaObject, SrcGetNativeIdMethod);
}