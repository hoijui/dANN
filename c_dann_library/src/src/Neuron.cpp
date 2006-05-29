#include <math.h>
#include "Neuron.h"
#include "Synapse.h"
#include "DNA.h"
#include "Layer.h"
#include "NeuralNet.h"



Neuron::Neuron(Layer* OwningLayerToSet, DNA* DNAToSet, double InitialBiasWeight)
{
	if( (OwningLayerToSet == NULL) || (DNAToSet == NULL) )
		throw 0;

	this->ParentLayer = OwningLayerToSet;
	this->OwnedDNA = DNAToSet;
	this->InputSet = false;
	this->DesiredOutputSet = false;
	this->BiasWeight = InitialBiasWeight;

	if( InitialBiasWeight == 0 )
		this->BiasWeight = ((((double)rand()) / ((double)RAND_MAX)) * 2) - 1;
}

Neuron::~Neuron()
{
	//Delete all owned synapses
	this->DisconnectAllSynapses();
}

double Neuron::GetOutput()
{
	return this->Output;
}

DNA* Neuron::GetDNA()
{
	return this->OwnedDNA;
}

double Neuron::GetDeltaTrain()
{
	return this->DeltaTrain;
}

Layer* Neuron::GetParentLayer()
{
	return this->ParentLayer;
}

bool Neuron::ConnectToNeuron(Neuron* ToConnectTo, double InitialWeight)
{
	//Check to make sure parameters are legal
	if( (ToConnectTo == NULL))
		throw 0;

	//Check if we are already connected to the specified neuron
	for(int SynapseLcv = 0; SynapseLcv < this->DestinationSynapses.size(); SynapseLcv++)
	{
		if( this->DestinationSynapses[SynapseLcv]->GetDestinationNeuron() == ToConnectTo )
			return false;
	}

	//Connect neuron
	if( InitialWeight == 0 )
	{
		InitialWeight = ((((double)rand()) / ((double)RAND_MAX)) * 2) - 1;
		if( this->OwnedDNA->UseMinimumWeight == true )
			InitialWeight *= this->OwnedDNA->InitialMaxWeight;
	}

	Synapse* NewSynapse = new Synapse(ToConnectTo, this, InitialWeight);
	if( ToConnectTo->ConnectFromSynapse(NewSynapse) == true )
	{
		this->DestinationSynapses.push_back(NewSynapse);
		return true;
	}
	else
	{
		delete NewSynapse;
		return false;
	}
}

void Neuron::DisconnectAllDestinationSynapses()
{
	while( this->DisconnectAnyDestinationSynapse() );
}

void Neuron::DisconnectAllSourceSynapses()
{
	while( this->DisconnectAnySourceSynapse() );
}

bool Neuron::DisconnectAnyDestinationSynapse()
{
	if( this->DestinationSynapses.empty() )
		return false;

	Synapse* PopedSynapse = this->DestinationSynapses.back();
	PopedSynapse->GetDestinationNeuron()->RemoveSourceSynapse(this->DestinationSynapses[0]);
	this->DestinationSynapses.pop_back();
	delete PopedSynapse;
	return true;
}

bool Neuron::DisconnectAnySourceSynapse()
{
	if( this->SourceSynapses.empty() )
		return false;

	Synapse* PopedSynapse = this->DestinationSynapses.back();
	PopedSynapse->GetDestinationNeuron()->RemoveSourceSynapse(this->DestinationSynapses[0]);
	this->SourceSynapses.pop_back();
	delete PopedSynapse;
	return true;
}

void Neuron::DisconnectAllSynapses()
{
	this->DisconnectAllDestinationSynapses();
	this->DisconnectAllSourceSynapses();
}

bool Neuron::DisconnectSourceSynapse(Synapse* ToDisconnectFrom)
{
	if( ToDisconnectFrom == NULL )
		throw 0;

	for( int SynapseLcv = 0; SynapseLcv < this->SourceSynapses.size(); SynapseLcv++)
	{
		if( this->SourceSynapses[SynapseLcv] == ToDisconnectFrom )
		{
			this->SourceSynapses[SynapseLcv]->GetSourceNeuron()->RemoveDestinationSynapse(ToDisconnectFrom);
			delete this->SourceSynapses[SynapseLcv];
			this->SourceSynapses.erase(this->SourceSynapses.begin() + SynapseLcv);
			return true;
		}
	}

	return false;
}

bool Neuron::DisconnectDestinationSynapse(Synapse* ToDisconnectFrom)
{
	if( ToDisconnectFrom == NULL )
		throw 0;

	for( int SynapseLcv = 0; SynapseLcv < this->DestinationSynapses.size(); SynapseLcv++)
	{
		if( this->DestinationSynapses[SynapseLcv] == ToDisconnectFrom )
		{
			this->DestinationSynapses[SynapseLcv]->GetDestinationNeuron()->RemoveSourceSynapse(ToDisconnectFrom);
			delete this->DestinationSynapses[SynapseLcv];
			this->DestinationSynapses.erase(this->DestinationSynapses.begin() + SynapseLcv);
			return true;
		}
	}

	return false;
}

int Neuron::GetOutgoingConnectionCount()
{
	return this->DestinationSynapses.size();
}

int Neuron::GetIncommingConnectionCount()
{
	return this->SourceSynapses.size();
}

bool Neuron::ConnectFromSynapse(Synapse* ToConnectFrom)
{
	//Check for illegal parameters
	if( ToConnectFrom == NULL )
		throw 0;

	//Check if the neuron is already a source
	for( int SynapseLcv = 0; SynapseLcv < this->SourceSynapses.size(); SynapseLcv++)
	{
		if( this->SourceSynapses[SynapseLcv] == ToConnectFrom )
			return false;
	}

	//Add the synapse as the connect from
	this->SourceSynapses.push_back(ToConnectFrom);
	return true;
}

void Neuron::RemoveDestinationSynapse(Synapse* ToRemove)
{
	if( ToRemove == NULL )
		throw 0;

	for( int SynapseLcv = 0; SynapseLcv < this->DestinationSynapses.size(); SynapseLcv++)
	{
		if( this->DestinationSynapses[SynapseLcv] == ToRemove )
		{
			this->DestinationSynapses.erase(this->DestinationSynapses.begin() + SynapseLcv);

			return;
		}
	}

	throw 0;
}

void Neuron::RemoveSourceSynapse(Synapse* ToRemove)
{
	if( ToRemove == NULL )
		throw 0;

	for( int SynapseLcv = 0; SynapseLcv < this->SourceSynapses.size(); SynapseLcv++)
	{
		if( this->SourceSynapses[SynapseLcv] == ToRemove )
		{
			this->SourceSynapses.erase(this->SourceSynapses.begin() + SynapseLcv);

			return;
		}
	}

	throw 0;
}

double Neuron::Propogate()
{
	//Calculate Activity
	this->Activity = 0;

	//dont do the usual propogati0on if the input was set manually
	if( this->InputSet )
	{
		this->Activity += this->CurrentInput;
		this->Output = this->Activity;
		return this->Output;
	}

	//Add each synapses activity
	for(int SynapseLcv = 0; SynapseLcv < this->SourceSynapses.size(); SynapseLcv++)
	{
		this->Activity += this->SourceSynapses[SynapseLcv]->CalculateOutput();
	}

	//Add the bias to the activity
	this->Activity += this->BiasWeight;

	//Calculate activation and set it as output
	this->Output = this->ActivationFunction(this->Activity);

	//return the newly calculated output
	return this->Output;
}

void Neuron::SetNeuronInput(double InputToSet)
{
	if( !((InputToSet >= -1) && (InputToSet <= 1)) )
		throw 0;

	this->InputSet = true;
	this->CurrentInput = InputToSet;
}

void Neuron::ResetNeuronInput()
{
	this->InputSet = false;
}

double Neuron::ActivationFunction(double Activity)
{
	return ((double)tanh(Activity));
}

void Neuron::BackPropogateWeight()
{
	//calculate this neurons delta train
	this->CalculateDeltaTrain();

	//go thru all the source synapses and make them learn
	for( int SynapseLcv = 0; SynapseLcv < this->SourceSynapses.size(); SynapseLcv++)
	{
		this->SourceSynapses[SynapseLcv]->LearnWeight();
	}

	this->BiasWeight += this->DeltaTrain * this->OwnedDNA->LearningRate;
}

void Neuron::BackPropogateStructure()
{
	this->DropSourceSynapse();
	this->ConnectDestinationSynapse();
}

void Neuron::SetTrainingData(double TrainingData)
{
	this->DesiredOutput = TrainingData;
	this->DesiredOutputSet = true;
}

double Neuron::ActivationFunctionDerivitive(double Activity)
{
	return ((double)1) - pow(((double)tanh(Activity)), ((double)2));
}

double Neuron::CalculateDeltaTrain()
{
	if( this->DesiredOutputSet )
	{
		this->DeltaTrain = this->ActivationFunctionDerivitive(this->Activity) * (this->DesiredOutput - this->Output);
	}
	else
	{
		this->DeltaTrain = 0;
		for( int SynapseLcv = 0; SynapseLcv < this->DestinationSynapses.size(); SynapseLcv++)
		{
			this->DeltaTrain += this->DestinationSynapses[SynapseLcv]->CalculateDifferential();
		}

		this->DeltaTrain *= this->ActivationFunctionDerivitive(this->Activity);
	}

	return this->DeltaTrain;
}

void Neuron::DropSourceSynapse()
{
	if( this->OwnedDNA->UseMinimumWeight )
	{
		for( int SynapseLcv = 0; SynapseLcv < this->SourceSynapses.size(); SynapseLcv++ )
		{
			if( this->SourceSynapses[SynapseLcv]->GetCurrentWeight() < this->OwnedDNA->MinimumWeight )
			{
				this->DisconnectSourceSynapse(this->SourceSynapses[SynapseLcv]);
				SynapseLcv--;
			}
		}
	}
	else if( this->SourceSynapses.size() > this->OwnedDNA->MaximumIncomming )
	{
		throw 0;
	}
}

void Neuron::ConnectDestinationSynapse()
{
	if( (this->DestinationSynapses.size() < this->OwnedDNA->MinimumOutgoing)&&( this->ParentLayer->DestinationLayer != NULL) )
	{
//		for( int ConnectLcv = 0; ConnectLcv < (this->OwnedDNA->MinimumOutgoing - this->DestinationSynapses.GetCount()); ConnectLcv++)
//		{
			//Gets neuron to connect to
			Neuron* ToConnectTo = this->ParentLayer->GetNeuralNet()->GetRandomForwardNeuron(this);
			this->ConnectToNeuron(ToConnectTo);
//		}
	}
}

