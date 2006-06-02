#ifndef dann_layer_jsdf8912384ukj13n4r8923y4r59o82j
#define dann_layer_jsdf8912384ukj13n4r8923y4r59o82j


#include "DataTypes.h"

class Layer;
class DNA;
class NeuralNet;
class Neuron;

class Layer
{
	//Attributes
public:
	Layer* SourceLayer;
	Layer* DestinationLayer;
private:
	NeuronArray NeuronsOwned;
	DNA* OwnedDNA;
	NeuralNet* OwnedNeuralNet;
	unsigned int Uid;


	//Methods
	//Constructors
public:
	Layer(NeuralNet* OwnedNeuralNetToSet, DNA* OwnedDNAToSet, Layer* DestinationLayerToSet, Layer* SourceLayerToSet);
	~Layer();

	//Access methods
public:
	NeuralNet* GetNeuralNet();

	//Network Structure
public:
	void AddNeurons(unsigned int CountToAdd);
	void AddNeuron(Neuron* NeuronToAdd);
	void ConnectAllToLayer(Layer* LayerToConnectTo);
	void ConnectAllToNextLayer();
	void ConnectAllToForwardLayers(Layer* LastLayerToConnectTo);
	Neuron* GetRandomNeuron();
	Neuron* GetNeuronByUid(unsigned int UidToSearch);
	bool ContainsNeuronByUid(unsigned int UidToSearch);
	int NeuronCount();
	int OutgoingConnectionCount();
	int IncommingConnectionCount();
	int MaximumOutgoingConnectionCount();
	int MinimumOutgoingConnectionCount();
	int MaximumIncommingConnectionCount();
	int MinimumIncommingConnectionCount();


	//Network Propogation
public:
	double* GetOutput();
	void PropogateAll();
	void SetInput(double* InputToSet);

	//Network Backpropogation
public:
	void SetTrainData(double* TrainToSet);
	void BackPropogateWeightAll();
	void BackPropogateStructureAll();
};

#endif

