#ifndef dann_datatypes_n891u2384ehjiwfh98w23y89ru
#define dann_datatypes_n891u2384ehjiwfh98w23y89ru



#include <vector>
#include <list>

//using namespace std;

class Neuron;
class Synapse;

typedef std::vector<Synapse*> SynapseArray;
typedef std::vector<Neuron*> NeuronArray;
typedef std::list<Synapse*, Synapse*> SynapseList;
typedef std::list<Neuron*, Neuron*> NeuronList;


#endif

