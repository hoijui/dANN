#ifndef dann_jnihelpers_lskd82u4oij238ru982r
#define dann_jnihelpers_lskd82u4oij238ru982r

#include "Synapse.h"
#include "Neuron.h"
#include "Layer.h"
#include <jni.h>

Synapse* GetNativeSynapse(JNIEnv *Application, jobject LocalThis);
Neuron* GetNativeNeuron(JNIEnv *Application, jobject JavaNeuron);
Layer* GetNativeLayer(JNIEnv *Application, jobject JavaLayer);
NeuralNet* GetNativeNeuralNet(JNIEnv *Application, jobject JavaLayer);
DNA* GetNativeDNA(JNIEnv *Application, jobject JavaDNA);

#endif