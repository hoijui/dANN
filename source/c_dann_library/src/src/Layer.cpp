#include <math.h>
#include <stdlib.h>
#include <time.h>
#include "Layer.h"
#include "Neuron.h"



Layer::Layer(NeuralNet* OwnedNeuralNetToSet, DNA* OwnedDNAToSet, Layer* DestinationLayerToSet, Layer* SourceLayerToSet)
{
	if( (OwnedNeuralNetToSet == NULL)||(OwnedDNAToSet == NULL) )
		throw 0;

	this->OwnedNeuralNet = OwnedNeuralNetToSet;
	this->OwnedDNA = OwnedDNAToSet;
	this->DestinationLayer = DestinationLayerToSet;
	this->SourceLayer = SourceLayerToSet;
}

Layer::~Layer()
{
	//Delete all the owned neurons.
	for( int NeuronLcv = 0; NeuronLcv < this->NeuronsOwned.size(); NeuronLcv++)
	{
		delete this->NeuronsOwned[NeuronLcv];
	}

	//remove all the neurons that were just deleted.
	this->NeuronsOwned.clear();
}

NeuralNet* Layer::GetNeuralNet()
{
	return this->OwnedNeuralNet;
}

void Layer::AddNeurons(unsigned int CountToAdd)
{
	if( CountToAdd == 0 )
		throw 0;

	for( unsigned int AddLcv = 0; AddLcv < CountToAdd; AddLcv++)
	{
		Neuron* NewNeuron = new Neuron(this, this->OwnedDNA);
		this->NeuronsOwned.push_back(NewNeuron);
	}
}

void Layer::AddNeuron(Neuron* NeuronToAdd)
{
	if( NeuronToAdd == NULL )
		throw 0;

	this->NeuronsOwned.push_back(NeuronToAdd);
}

void Layer::ConnectAllToLayer(Layer* LayerToConnectTo)
{
	if( LayerToConnectTo == NULL )
		throw 0;

	for( int OutLcv = 0; OutLcv < this->NeuronsOwned.size(); OutLcv++)
	{
		for( int InLcv = 0; InLcv < LayerToConnectTo->NeuronsOwned.size(); InLcv++ )
		{
			this->NeuronsOwned[OutLcv]->ConnectToNeuron(LayerToConnectTo->NeuronsOwned[InLcv]);
		}
	}
}

void Layer::ConnectAllToNextLayer()
{
	this->ConnectAllToLayer(this->DestinationLayer);
}

void Layer::ConnectAllToForwardLayers(Layer* LastLayerToConnectTo)
{
	Layer* CurrentLayer = this->DestinationLayer;
	while( CurrentLayer != LastLayerToConnectTo )
	{
		this->ConnectAllToLayer(CurrentLayer);

		CurrentLayer = CurrentLayer->DestinationLayer;
	}

	if( LastLayerToConnectTo != NULL )
		this->ConnectAllToLayer(LastLayerToConnectTo);
}

Neuron* Layer::GetRandomNeuron()
{
	int NeuronIndex = floor(   (((double)rand() + 1) / ((double)RAND_MAX + 2)) * this->NeuronsOwned.size() );
	return this->NeuronsOwned[NeuronIndex];
}

Neuron* Layer::GetNeuronByUid(unsigned int UidToSearch)
{
	return NULL;
}

bool Layer::ContainsNeuronByUid(unsigned int UidToSearch)
{
	return false;
}

int Layer::NeuronCount()
{
	return this->NeuronsOwned.size();
}

int Layer::OutgoingConnectionCount()
{
	int RetVal = 0;
	for( int NeuronLcv = 0; NeuronLcv < this->NeuronCount(); NeuronLcv++)
	{
		RetVal += this->NeuronsOwned[NeuronLcv]->GetOutgoingConnectionCount();
	}

	return RetVal;
}

int Layer::IncommingConnectionCount()
{
	int RetVal = 0;
	for( int NeuronLcv = 0; NeuronLcv < this->NeuronCount(); NeuronLcv++)
	{
		RetVal += this->NeuronsOwned[NeuronLcv]->GetIncommingConnectionCount();
	}

	return RetVal;
}

int Layer::MaximumOutgoingConnectionCount()
{
	int RetVal = 0;
	for( int NeuronLcv = 0; NeuronLcv < this->NeuronCount(); NeuronLcv++)
	{
		if( RetVal < this->NeuronsOwned[NeuronLcv]->GetOutgoingConnectionCount() )
			RetVal = this->NeuronsOwned[NeuronLcv]->GetOutgoingConnectionCount();
	}

	return RetVal;
}

int Layer::MinimumOutgoingConnectionCount()
{
	if( this->NeuronCount() == 0 )
		return 0;

	int RetVal = this->NeuronsOwned[0]->GetOutgoingConnectionCount();
	for( int NeuronLcv = 1; NeuronLcv < this->NeuronCount(); NeuronLcv++)
	{
		if( RetVal > this->NeuronsOwned[NeuronLcv]->GetOutgoingConnectionCount() )
			RetVal = this->NeuronsOwned[NeuronLcv]->GetOutgoingConnectionCount();
	}

	return RetVal;
}

int Layer::MaximumIncommingConnectionCount()
{
	int RetVal = 0;
	for( int NeuronLcv = 0; NeuronLcv < this->NeuronCount(); NeuronLcv++)
	{
		if( RetVal < this->NeuronsOwned[NeuronLcv]->GetIncommingConnectionCount() )
			RetVal = this->NeuronsOwned[NeuronLcv]->GetIncommingConnectionCount();
	}

	return RetVal;
}

int Layer::MinimumIncommingConnectionCount()
{
	if( this->NeuronCount() == 0 )
		return 0;

	int RetVal = this->NeuronsOwned[0]->GetIncommingConnectionCount();
	for( int NeuronLcv = 1; NeuronLcv < this->NeuronCount(); NeuronLcv++)
	{
		if( RetVal > this->NeuronsOwned[NeuronLcv]->GetIncommingConnectionCount() )
			RetVal = this->NeuronsOwned[NeuronLcv]->GetIncommingConnectionCount();
	}

	return RetVal;
}

double* Layer::GetOutput()
{
	double* RetVal = new double[this->NeuronCount()];
	for( int NeuronLcv = 0; NeuronLcv < this->NeuronCount(); NeuronLcv++)
	{
		RetVal[NeuronLcv] = this->NeuronsOwned[NeuronLcv]->GetOutput();
	}

	return RetVal;
}

void Layer::PropogateAll()
{
	for( int NeuronLcv = 0; NeuronLcv < this->NeuronCount(); NeuronLcv++)
	{
		this->NeuronsOwned[NeuronLcv]->Propogate();
	}
}

void Layer::SetInput(double* InputToSet)
{
	for( int NeuronLcv = 0; NeuronLcv < this->NeuronCount(); NeuronLcv++)
	{
		this->NeuronsOwned[NeuronLcv]->SetNeuronInput(InputToSet[NeuronLcv]);
	}
}

void Layer::SetTrainData(double* TrainToSet)
{
	for( int NeuronLcv = 0; NeuronLcv < this->NeuronCount(); NeuronLcv++)
	{
		this->NeuronsOwned[NeuronLcv]->SetTrainingData(TrainToSet[NeuronLcv]);
	}
}

void Layer::BackPropogateWeightAll()
{
	for( int NeuronLcv = 0; NeuronLcv < this->NeuronCount(); NeuronLcv++)
	{
		this->NeuronsOwned[NeuronLcv]->BackPropogateWeight();
	}
}

void Layer::BackPropogateStructureAll()
{
	for( int NeuronLcv = 0; NeuronLcv < this->NeuronCount(); NeuronLcv++)
	{
		this->NeuronsOwned[NeuronLcv]->BackPropogateStructure();
	}
}

