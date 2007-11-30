#ifndef dann_synapse_isd98fu9803jroij23894u
#define dann_synapse_isd98fu9803jroij23894u



class Neuron;

class Synapse
{
	//Attributes
private:
	Neuron* DestinationNeuron;
	Neuron* SourceNeuron;
	double Weight;


	//Methods
public:
	// Constructors and Destructors
	Synapse(Neuron* Destination, Neuron* Source, double InitialWeight);
	~Synapse();

	//Access Methods
public:
	double GetCurrentWeight();
	Neuron* GetDestinationNeuron();
	Neuron* GetSourceNeuron();

	//Propogation
public:
	double CalculateOutput();

	//Backpropogation
public:
	void LearnWeight();
	double CalculateDifferential();
protected:
private:
};


#endif

