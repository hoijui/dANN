#include "Synapse.h"
#include "Neuron.h"
#include "DNA.h"

Synapse::Synapse(Neuron* Destination, Neuron* Source, double InitialWeight)
{
	if((Destination == NULL) || (Source == NULL) || (InitialWeight == 0) )
		throw 0;

	this->DestinationNeuron = Destination;
	this->SourceNeuron = Source;
	this->Weight = InitialWeight;
}

Synapse::~Synapse()
{
}

double Synapse::GetCurrentWeight()
{
	return this->Weight;
}

Neuron* Synapse::GetDestinationNeuron()
{
	return this->DestinationNeuron;
}

Neuron* Synapse::GetSourceNeuron()
{
	return this->SourceNeuron;
}

double Synapse::CalculateOutput()
{
	return this->SourceNeuron->GetOutput() * this->Weight;
}

void Synapse::LearnWeight()
{
	this->Weight += this->DestinationNeuron->GetDNA()->LearningRate * this->SourceNeuron->GetOutput() * this->DestinationNeuron->GetDeltaTrain();
	if( this->Weight == 0 )
		this->DestinationNeuron->DisconnectSourceSynapse(this);
}

double Synapse::CalculateDifferential()
{
	return this->Weight * this->DestinationNeuron->GetDeltaTrain();
}

