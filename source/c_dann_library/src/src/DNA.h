#ifndef dann_dna_jw8e8r92n35ytg87gyi4rnhiku23yu4p
#define dann_dna_jw8e8r92n35ytg87gyi4rnhiku23yu4p




class DNA
{
public:
	double LearningRate;

	int MinimumOutgoing;
	int MaximumIncomming;

	double MinimumWeight;
	double InitialMaxWeight;

	bool UseMinimumWeight;

	double IncommingDropFactor;

	bool LayeredForward;


	DNA()
	{
		this->LearningRate = 0.001;
		this->MinimumOutgoing = 50;
		this->MaximumIncomming = 1000000;
		this->MinimumWeight = .0001;
		this->InitialMaxWeight = .00001;
		this->UseMinimumWeight = true;
		this->IncommingDropFactor = 20;
		this->LayeredForward = true;
	}

	DNA(double LearningRateToSet, int MinimumOutgoingToSet, int MaximumIncommingToSet, double MinimumWeightToSet, double InitialMaxWeightToSet, bool UseMinimumWeightToSet, double IncommingDropFactorToSet, bool LayeredForwardToSet)
	{
		this->LearningRate = LearningRateToSet;
		this->MinimumOutgoing = MinimumOutgoingToSet;
		this->MaximumIncomming = MaximumIncommingToSet;
		this->MinimumWeight = MinimumWeightToSet;
		this->InitialMaxWeight = InitialMaxWeightToSet;
		this->UseMinimumWeight = UseMinimumWeightToSet;
		this->IncommingDropFactor = IncommingDropFactorToSet;
		this->LayeredForward = LayeredForwardToSet;
	}
};

#endif

