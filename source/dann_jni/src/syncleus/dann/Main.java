package syncleus.dann;

public class Main
{
	protected static NeuralNet Brain = null;
	protected static DNA CoreDna = null;
	
	protected static NeuralNet RewardBrain = null;
	
	public static void main(String[] args)
	{
		Main.CoreDna = new DNA();
		Main.Brain = new NeuralNet(Main.CoreDna, 2, 2);
		Brain.AddLayerAfterInput(4);
		Brain.ConnectLayeredFeedForward();

		Main.RewardBrain = new NeuralNet(Main.CoreDna, 3, 2);
		Main.RewardBrain.AddLayerAfterInput(4);
		Main.RewardBrain.AddLayerAfterInput(4);
		Main.RewardBrain.ConnectLayeredFeedForward();
		
		for(int Lcv = 0; Lcv < 200; Lcv++)
		{
			System.out.println("Testing network:");
			System.out.println("1, 1   -> " + Main.test(1, 1)[0] + ", " + Main.test(1, 1)[1]);
			System.out.println("1, -1  -> " + Main.test(1, -1)[0] + ", " + Main.test(1, -1)[1]);
			System.out.println("-1, 1  -> " + Main.test(-1, 1)[0] + ", " + Main.test(-1, 1)[1]);
			System.out.println("-1, -1 -> " + Main.test(-1, -1)[0] + ", " + Main.test(-1, -1)[1]);
			System.out.println("");
			System.out.print("Training 10000 times...");
			for(int Lcv2 = 0; Lcv2 <  10000; Lcv2++)
				Main.train();
			System.out.println("Training Done");
		}
	}

	public static double[] test(double Input1, double Input2)
	{
		double[] Inputs = new double[2];
		Inputs[0] = Input1;
		Inputs[1] = Input2;
		
		Main.Brain.SetCurrentInput(Inputs);
		Main.Brain.PropogateOutput();
		
		double[] CurrentOutput = Main.Brain.GetCurrentOutput();
		
		return CurrentOutput;
	}
	
	public static void train()
	{
		double[] TrainArray = new double[2];
		double[] TestResult;

		TestResult = test(-1,-1);		
		TrainArray[0] = -1;
		TrainArray[1] = -1;
		Main.Brain.SetCurrentTraining(Main.calculateTrain(TestResult, Main.calculateReward(TestResult, TrainArray)));
		Main.Brain.BackPropogateWeightTraining();
		Main.RewardBrain.SetCurrentTraining(TrainArray);
		Main.RewardBrain.BackPropogateWeightTraining();

		test(-1,1);
		TrainArray[0] = 1;
		TrainArray[1] = 1;
		Main.Brain.SetCurrentTraining(Main.calculateTrain(TestResult, Main.calculateReward(TestResult, TrainArray)));
		Main.Brain.BackPropogateWeightTraining();
		Main.RewardBrain.SetCurrentTraining(TrainArray);
		Main.RewardBrain.BackPropogateWeightTraining();
		
		test(1,-1);
		TrainArray[0] = 1;
		TrainArray[1] = 1;
		Main.Brain.SetCurrentTraining(Main.calculateTrain(TestResult, Main.calculateReward(TestResult, TrainArray)));
		Main.Brain.BackPropogateWeightTraining();
		Main.RewardBrain.SetCurrentTraining(TrainArray);
		Main.RewardBrain.BackPropogateWeightTraining();

		test(1,1);
		TrainArray[0] = -1;
		TrainArray[1] = 1;
		Main.Brain.SetCurrentTraining(Main.calculateTrain(TestResult, Main.calculateReward(TestResult, TrainArray)));
		Main.Brain.BackPropogateWeightTraining();
		Main.RewardBrain.SetCurrentTraining(TrainArray);
		Main.RewardBrain.BackPropogateWeightTraining();
	}
	
	public static double[] calculateTrain(double[] output, double reward)
	{
		double[] Inputs = new double[3];
		Inputs[0] = reward;
		Inputs[1] = output[0];
		Inputs[2] = output[1];
		
		Main.RewardBrain.SetCurrentInput(Inputs);
		
		Main.RewardBrain.PropogateOutput();
		double[] Outputs = Main.RewardBrain.GetCurrentOutput();
		
		for(int outIdx = 0; outIdx < Outputs.length; outIdx++)
		{
			if( Outputs[outIdx] > 0 )
				Outputs[outIdx] = 1;
			else if( Outputs[outIdx] < 0 )
				Outputs[outIdx] = -1;
			else
				Outputs[outIdx] = 1;
		}
		
		return Outputs;
	}
	
	public static double calculateReward(double[] output, double[] desiredOutput)
	{
		double rewardSum = 0;
		for(int outputIndex = 0; outputIndex < output.length; outputIndex++)
		{
			if( ((desiredOutput[outputIndex] > 0)&&(output[outputIndex] > 0))||((desiredOutput[outputIndex] < 0)&&(output[outputIndex] < 0)))
				rewardSum += 1;
			else
				rewardSum += -1;
			//rewardSum += 1 - Math.abs(desiredOutput[outputIndex] - output[outputIndex]);
		}
		
		return rewardSum/((double)output.length);
	}
	
}
