#ifndef dann_neuralnet_jiw98e8r9j23rh9erhgueh789y2r59hj29u
#define dann_neuralnet_jiw98e8r9j23rh9erhgueh789y2r59hj29u




class Layer;
class DNA;
class Neuron;


class NeuralNet
{
private:
	static bool Randomized;
	DNA* OwnedDNA;
	int LayerCount;
	//UidFactory LayerUidFactory;
	//UidFactory NeuronUidFacotry;

public:
	Layer* InLayer;
	Layer* OutLayer;


	//Methods
	//Constructors & Destructors
public:
	NeuralNet(DNA* OwnedDNAToSet, int InputCount, int OutputCount);
	~NeuralNet();

	//Access methods
public:
	//void SetDNA(DNA OwnedDNAToSet);

	//Network Structure
public:
	Layer* AddLayerAfterInput(int NeuronCount);
	Layer* AddLayerBeforeOutput(int NeuronCount);
	Layer* AddLayerBefore(int NeuronCount, Layer* LayerToAddBefore);
	Layer* AddLayerAfter(int NeuronCount, Layer* LayerToAddAfter);
	void ConnectAllFeedForward();
	void ConnectLayeredFeedForward();
	Neuron* GetRandomForwardNeuron(Neuron* StartNeuron);
	int GetNeuronCount();
	int GetOutgoingConnectionCount();
	int GetIncommingConnectionCount();
	int GetMaximumOutgoingConnectionCount();
	int GetMinimumOutgoingConnectionCount();
	int GetMaximumIncommingConnectionCount();
	int GetMinimumIncommingConnectionCount();

	//Unique ID assignment
public:
	unsigned int GetNextNeuronId();
	void FreeNeuronId(unsigned int IdToFree);
	unsigned int GetNextLayerId();
	void FreeLayerId(unsigned int IdToFree);

	//Propogation
public:
	double* GetCurrentOutput();
	void SetCurrentInput(double* InputToSet);
	void PropogateOutput();

	//Backpropogation
public:
	void SetCurrentTraining(double* TrainingToSet);
	void BackPropogateWeightTraining();
	void BackPropogateStructureTraining();
};

#endif

