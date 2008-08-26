#region Copright 2004 Jeffrey Phillips Freeman

/********************************************************************************/
/*                                                                              */
/*                   dANN: Dynamic Artifical Neural Network                     */
/*               (C) Copyright 2004 - * Jeffrey Phillips Freeman                */
/*                                                                              */
/*               Copyright History:                                             */
/*                  Created: July 28, 2004, Jeffrey Phillips Freeman            */
/*                                                                              */
/********************************************************************************/

#endregion

using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;
using System.Data;
using System.IO;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using dANN;

namespace dANNtest
{
	public class Form1 : System.Windows.Forms.Form
	{
		private System.Windows.Forms.Button TestButton;
		private System.Windows.Forms.Button TrainButton;
		private System.Windows.Forms.TextBox OutText;
		private Brain MyBrain;
		private DNA MyDNA = new DNA();
		private System.Windows.Forms.Button ClsButton;
		private System.Windows.Forms.Button TestSimpleButton;
		private System.ComponentModel.IContainer components;
		private Bitmap[]TestImages = new Bitmap[3];
		private static int SegmentWidth = 16;
		private static int SegmentHeight = 16;
		private static int SegmentTotal = SegmentWidth * SegmentHeight;
		private static int CompressionSize = 32;
		private Layer CompressionLayer = null;
		private bool UseStructureAlgorithm = true;
		private int TrainingCycles = 100000;
		private int TrainStructPer = 1000;
		private int TrainPerTimer = 100;
		private int TrainLeftTillStruct = 0;
		private int TrainingTimerCyclesLeft = 0;
		private int TrainingStartTick = 0;
		private System.Windows.Forms.ProgressBar TrainProgress;
		private System.Windows.Forms.Button StopTrainingButton;
		private System.Windows.Forms.Button LoadButton;
		private System.Windows.Forms.Button SaveButton;
		private System.Windows.Forms.Button StructButton;
		private System.Windows.Forms.Label TrainPercentLabel;
		private System.Windows.Forms.Button WeightStatButton;
		private System.Windows.Forms.Timer TrainingTimer;

        private int TrainCount = 0;

		public Form1()
		{
			MyBrain = new Brain(MyDNA, SegmentTotal, SegmentTotal);
			InitializeComponent();
		}


		protected override void Dispose( bool disposing )
		{
			if( disposing )
			{
				if (components != null) 
				{
					components.Dispose();
				}
			}
			base.Dispose( disposing );
		}

		#region Windows Form Designer generated code
		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
            this.components = new System.ComponentModel.Container();
            this.OutText = new System.Windows.Forms.TextBox();
            this.TestButton = new System.Windows.Forms.Button();
            this.TrainButton = new System.Windows.Forms.Button();
            this.ClsButton = new System.Windows.Forms.Button();
            this.TestSimpleButton = new System.Windows.Forms.Button();
            this.TrainingTimer = new System.Windows.Forms.Timer(this.components);
            this.TrainProgress = new System.Windows.Forms.ProgressBar();
            this.TrainPercentLabel = new System.Windows.Forms.Label();
            this.StopTrainingButton = new System.Windows.Forms.Button();
            this.LoadButton = new System.Windows.Forms.Button();
            this.SaveButton = new System.Windows.Forms.Button();
            this.StructButton = new System.Windows.Forms.Button();
            this.WeightStatButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // OutText
            // 
            this.OutText.Location = new System.Drawing.Point(24, 24);
            this.OutText.Multiline = true;
            this.OutText.Name = "OutText";
            this.OutText.ReadOnly = true;
            this.OutText.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.OutText.Size = new System.Drawing.Size(456, 376);
            this.OutText.TabIndex = 0;
            this.OutText.Text = "OutText";
            // 
            // TestButton
            // 
            this.TestButton.Location = new System.Drawing.Point(16, 424);
            this.TestButton.Name = "TestButton";
            this.TestButton.Size = new System.Drawing.Size(80, 24);
            this.TestButton.TabIndex = 1;
            this.TestButton.Text = "Test";
            this.TestButton.Click += new System.EventHandler(this.TestButton_Click);
            // 
            // TrainButton
            // 
            this.TrainButton.Location = new System.Drawing.Point(248, 424);
            this.TrainButton.Name = "TrainButton";
            this.TrainButton.Size = new System.Drawing.Size(64, 24);
            this.TrainButton.TabIndex = 2;
            this.TrainButton.Text = "Train";
            this.TrainButton.Click += new System.EventHandler(this.TrainButton_Click);
            // 
            // ClsButton
            // 
            this.ClsButton.Location = new System.Drawing.Point(136, 424);
            this.ClsButton.Name = "ClsButton";
            this.ClsButton.Size = new System.Drawing.Size(64, 24);
            this.ClsButton.TabIndex = 3;
            this.ClsButton.Text = "CLS";
            this.ClsButton.Click += new System.EventHandler(this.ClsButton_Click);
            // 
            // TestSimpleButton
            // 
            this.TestSimpleButton.Location = new System.Drawing.Point(16, 464);
            this.TestSimpleButton.Name = "TestSimpleButton";
            this.TestSimpleButton.Size = new System.Drawing.Size(80, 24);
            this.TestSimpleButton.TabIndex = 4;
            this.TestSimpleButton.Text = "Test Simple";
            this.TestSimpleButton.Click += new System.EventHandler(this.TestSimpleButton_Click);
            // 
            // TrainingTimer
            // 
            this.TrainingTimer.Interval = 1;
            this.TrainingTimer.Tick += new System.EventHandler(this.TrainingTimer_Tick);
            // 
            // TrainProgress
            // 
            this.TrainProgress.Location = new System.Drawing.Point(8, 544);
            this.TrainProgress.Name = "TrainProgress";
            this.TrainProgress.Size = new System.Drawing.Size(472, 16);
            this.TrainProgress.Step = 1;
            this.TrainProgress.TabIndex = 5;
            // 
            // TrainPercentLabel
            // 
            this.TrainPercentLabel.Location = new System.Drawing.Point(8, 568);
            this.TrainPercentLabel.Name = "TrainPercentLabel";
            this.TrainPercentLabel.Size = new System.Drawing.Size(472, 16);
            this.TrainPercentLabel.TabIndex = 6;
            this.TrainPercentLabel.Text = "0 %";
            this.TrainPercentLabel.TextAlign = System.Drawing.ContentAlignment.TopCenter;
            // 
            // StopTrainingButton
            // 
            this.StopTrainingButton.Location = new System.Drawing.Point(240, 464);
            this.StopTrainingButton.Name = "StopTrainingButton";
            this.StopTrainingButton.Size = new System.Drawing.Size(80, 24);
            this.StopTrainingButton.TabIndex = 7;
            this.StopTrainingButton.Text = "Stop Training";
            this.StopTrainingButton.Click += new System.EventHandler(this.StopTrainingButton_Click);
            // 
            // LoadButton
            // 
            this.LoadButton.Location = new System.Drawing.Point(360, 424);
            this.LoadButton.Name = "LoadButton";
            this.LoadButton.Size = new System.Drawing.Size(96, 24);
            this.LoadButton.TabIndex = 8;
            this.LoadButton.Text = "Load Network";
            this.LoadButton.Click += new System.EventHandler(this.LoadButton_Click);
            // 
            // SaveButton
            // 
            this.SaveButton.Location = new System.Drawing.Point(360, 464);
            this.SaveButton.Name = "SaveButton";
            this.SaveButton.Size = new System.Drawing.Size(96, 24);
            this.SaveButton.TabIndex = 9;
            this.SaveButton.Text = "Save Network";
            this.SaveButton.Click += new System.EventHandler(this.SaveButton_Click);
            // 
            // StructButton
            // 
            this.StructButton.Location = new System.Drawing.Point(128, 464);
            this.StructButton.Name = "StructButton";
            this.StructButton.Size = new System.Drawing.Size(80, 24);
            this.StructButton.TabIndex = 10;
            this.StructButton.Text = "Toggle Struct";
            this.StructButton.Click += new System.EventHandler(this.StructButton_Click);
            // 
            // WeightStatButton
            // 
            this.WeightStatButton.Location = new System.Drawing.Point(16, 504);
            this.WeightStatButton.Name = "WeightStatButton";
            this.WeightStatButton.Size = new System.Drawing.Size(80, 24);
            this.WeightStatButton.TabIndex = 11;
            this.WeightStatButton.Text = "Weight Stats";
            this.WeightStatButton.Click += new System.EventHandler(this.WeightStatButton_Click);
            // 
            // Form1
            // 
            this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
            this.ClientSize = new System.Drawing.Size(504, 597);
            this.Controls.Add(this.WeightStatButton);
            this.Controls.Add(this.StructButton);
            this.Controls.Add(this.SaveButton);
            this.Controls.Add(this.LoadButton);
            this.Controls.Add(this.StopTrainingButton);
            this.Controls.Add(this.TrainPercentLabel);
            this.Controls.Add(this.TrainProgress);
            this.Controls.Add(this.TestSimpleButton);
            this.Controls.Add(this.ClsButton);
            this.Controls.Add(this.TrainButton);
            this.Controls.Add(this.TestButton);
            this.Controls.Add(this.OutText);
            this.Name = "Form1";
            this.Text = "NCI C# Demo";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

		}
		#endregion

		[STAThread]
		static void Main() 
		{
			Application.Run(new Form1());
		}




		#region Image Processing




		private Color[][] BreakupImage(Bitmap ImageToBreakup, int Width, int Height)
		{
			int WorkingBitmapWidth = (int)Math.Floor(((double)ImageToBreakup.Width)/((double)Width))*Width;
			int WorkingBitmapHeight = (int)Math.Floor(((double)ImageToBreakup.Height)/((double)Height))*Height;
			int TotalWidthSegments = WorkingBitmapWidth/Width;
			int TotalHeightSegments = WorkingBitmapHeight/Height;
			Color[][] RetVal = new Color[TotalWidthSegments*TotalHeightSegments][];
			for( int Lcv = 0; Lcv < TotalWidthSegments*TotalHeightSegments; Lcv++)
				RetVal[Lcv] = new Color[Width*Height];

			for( int WidthSegmentLcv = 0; WidthSegmentLcv < TotalWidthSegments; WidthSegmentLcv++)
			{
				for( int HeightSegmentLcv = 0; HeightSegmentLcv < TotalHeightSegments; HeightSegmentLcv++)
				{
					RetVal[(WidthSegmentLcv * TotalHeightSegments) + HeightSegmentLcv] = this.GetImageSegment(ImageToBreakup, WidthSegmentLcv * Width, HeightSegmentLcv * Height, Width, Height);
				}
			}

			return RetVal;
		}

		private double[][] GetRandomImageSection(int Width, int Height)
		{
			int RandomImgIndex = (int) Math.Round(this.MyDNA.RandomGenerator.NextDouble() * 2);
			int RandomXPos = (int) Math.Round((this.TestImages[RandomImgIndex].Width - Width) * this.MyDNA.RandomGenerator.NextDouble());
			int RandomYPos = (int) Math.Round((this.TestImages[RandomImgIndex].Height - Height) * this.MyDNA.RandomGenerator.NextDouble());
			double[][] RetVal = {new double[(Width*Height)], new double[(Width*Height)], new double[(Width*Height)]};
			for(int WidthLcv = 0; WidthLcv < Width; WidthLcv++)
			{
				for(int HeightLcv = 0; HeightLcv < Height; HeightLcv++)
				{
					RetVal[0][(WidthLcv*Height) + HeightLcv] = (((double)this.TestImages[RandomImgIndex].GetPixel(RandomXPos + WidthLcv, RandomYPos + HeightLcv).R)/((double)255))*2-1;
					RetVal[1][(WidthLcv*Height) + HeightLcv] = (((double)this.TestImages[RandomImgIndex].GetPixel(RandomXPos + WidthLcv, RandomYPos + HeightLcv).G)/((double)255))*2-1;
					RetVal[2][(WidthLcv*Height) + HeightLcv] = (((double)this.TestImages[RandomImgIndex].GetPixel(RandomXPos + WidthLcv, RandomYPos + HeightLcv).B)/((double)255))*2-1;
				}
			}

			return RetVal;

		}

		private Color[] GetImageSegment(Bitmap ImageToUse, int X, int Y, int Width, int Height)
		{
			Color[] RetVal = new Color[Width*Height];
			for( int WidthLcv = 0; WidthLcv < Width; WidthLcv++)
			{
				for(int HeightLcv = 0; HeightLcv < Height; HeightLcv++)
				{
					RetVal[(WidthLcv*Height) + HeightLcv] = ImageToUse.GetPixel(X + WidthLcv, Y + HeightLcv);
				}
			}

			return RetVal;
		}

		private Bitmap RecombineImage(Color[][] DataToRecombine, int Width, int Height, int SegmentsWidth, int SegmentsHeight)
		{
			Bitmap RetVal = new Bitmap(Width * SegmentsWidth, Height * SegmentsHeight);
			for(int SegmentsWidthLcv = 0; SegmentsWidthLcv < SegmentsWidth; SegmentsWidthLcv++)
			{
				for( int SegmentsHeightLcv = 0; SegmentsHeightLcv < SegmentsHeight; SegmentsHeightLcv++)
				{
					int CurrentSegment = SegmentsWidthLcv * SegmentsHeight + SegmentsHeightLcv;
					for( int WidthLcv = 0; WidthLcv < Width; WidthLcv++)
					{
						for( int HeightLcv = 0; HeightLcv<Height; HeightLcv++)
						{
							int CurrentBitmapX = SegmentsWidthLcv * Width + WidthLcv;
							int CurrentBitmapY = SegmentsHeightLcv * Height + HeightLcv;
							int CurrentPixleIndex = WidthLcv * Height + HeightLcv;
							RetVal.SetPixel(CurrentBitmapX, CurrentBitmapY, DataToRecombine[CurrentSegment][CurrentPixleIndex]);
						}
					}
				}
			}
			return RetVal;
		}

		private double[][] BreakupColors(Color[] ColorToBreakup)
		{
			double[][] RetVal = new double[3][];
			for( int ColorLcv = 0; ColorLcv < 3; ColorLcv++)
			{
				RetVal[ColorLcv] = new double[ColorToBreakup.Length];
				for( int PixleLcv = 0; PixleLcv < ColorToBreakup.Length; PixleLcv++)
				{
					if( ColorLcv == 0 )
						RetVal[ColorLcv][PixleLcv] = ((((double)(ColorToBreakup[PixleLcv].R))/255)*2)-1;
					if( ColorLcv == 1 )
						RetVal[ColorLcv][PixleLcv] = ((((double)(ColorToBreakup[PixleLcv].G))/255)*2)-1;
					if( ColorLcv == 2 )
						RetVal[ColorLcv][PixleLcv] = ((((double)(ColorToBreakup[PixleLcv].B))/255)*2)-1;
				}
			}
			return RetVal;
		}

		private Color[] RecombineColors(double[][] DividedColors)
		{
			Color[] RetVal = new Color[DividedColors[0].Length];
			for( int PixleLcv = 0; PixleLcv < RetVal.Length; PixleLcv++)
			{
				RetVal[PixleLcv] = Color.FromArgb((int) Math.Round((((DividedColors[0][PixleLcv])+ 1)/2)*255), (int) Math.Round((((DividedColors[1][PixleLcv])+ 1)/2)*255), (int) Math.Round((((DividedColors[2][PixleLcv])+ 1)/2)*255));
			}
			return RetVal;
		}

		private void OutputImage(int Width, int Height, double[][] Data, String FileName)
		{
			Bitmap OutputBitmap = new Bitmap(Width, Height);
			for(int WidthLcv = 0; WidthLcv < Width; WidthLcv++)
			{
				for(int HeightLcv = 0; HeightLcv < Height; HeightLcv++)
				{
					OutputBitmap.SetPixel(WidthLcv, HeightLcv, Color.FromArgb((int) (((Data[0][(WidthLcv*Height) + HeightLcv]+1)/2)*255), (int) (((Data[1][(WidthLcv*Height) + HeightLcv]+1)/2)*255), (int) (((Data[2][(WidthLcv*Height) + HeightLcv]+1)/2)*255)  ));
				}
			}
			OutputBitmap.Save(FileName);
		}




		#endregion

		#region Training & Testing




		private double TestError(int SampleRate)
		{
			double ErrorTotal = 0;
			int ErrorQty = 0;
			for(int Lcv = 0; Lcv < SampleRate; Lcv++)
			{
				double[][] NeuralInput = this.GetRandomImageSection(SegmentWidth, SegmentHeight);
				MyBrain.SetCurrentInput(NeuralInput[0]);
				MyBrain.PropogateOutput();
				ErrorTotal += this.CalculatePercentageError(MyBrain.GetCurrentOutput(), NeuralInput[0]);
				ErrorQty++;
			}
			return ErrorTotal / ErrorQty;
		}

		public void TrainOnce()
		{
            this.TrainCount++;
			double[][] NeuralInput = this.GetRandomImageSection(SegmentWidth, SegmentHeight);
			int RandomColorIndex = (int) Math.Round(this.MyDNA.RandomGenerator.NextDouble() * 2);
			MyBrain.SetCurrentInput(NeuralInput[RandomColorIndex]);
			MyBrain.PropogateOutput();
			MyBrain.SetCurrentTraining(NeuralInput[RandomColorIndex]);
			MyBrain.BackPropogateWeightTraining();

			if( (this.UseStructureAlgorithm)&&(this.TrainLeftTillStruct <= 0) )
			{
				this.TrainLeftTillStruct = this.TrainStructPer;
				MyBrain.BackPropogateStructureTraining();
			}

			this.TrainLeftTillStruct--;
		}

		private double CalculatePercentageError(double[] Actual, double[] Desired)
		{
			int TotalToCalc = 0;
			if( Actual.Length < Desired.Length )
				TotalToCalc = Actual.Length;
			else
				TotalToCalc = Desired.Length;

			double ErrorTotal = 0;
			for( int Lcv = 0; Lcv < TotalToCalc; Lcv++)
			{
				ErrorTotal += Math.Abs((Desired[Lcv] - Actual[Lcv])/2);
			}

			return (ErrorTotal/TotalToCalc) * 100;
		}




		#endregion

		#region Events





		private void Form1_Load(object sender, System.EventArgs e)
		{
			this.OutText.Text = "NCI Test\r\n";

			this.TestImages[0] = new Bitmap("C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\Sample Pictures\\In1.BMP");
            this.TestImages[1] = new Bitmap("C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\Sample Pictures\\In2.BMP");
            this.TestImages[2] = new Bitmap("C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\Sample Pictures\\In3.BMP");
//            this.TestImages[2] = new Bitmap("C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\Sample Pictures\\In4.bmp");

			//MyBrain.AddLayerAfterInput(48, false); //.ConnectAllToForwardLayers(null);
//			MyBrain.AddLayerAfterInput(256, false);//.ConnectAllToForwardLayers(null);
			this.CompressionLayer = MyBrain.AddLayerAfterInput(CompressionSize, true); 
//			MyBrain.AddLayerAfterInput(256, false);//.ConnectAllToForwardLayers(this.CompressionLayer);
			//MyBrain.AddLayerAfterInput(48, false); //.ConnectAllToForwardLayers(this.CompressionLayer);


			//this.CompressionLayer.ConnectAllToForwardLayers(null);
			//MyBrain.InLayer.ConnectAllToForwardLayers(this.CompressionLayer);

			//for( int Lcv = 0; Lcv < SegmentTotal; Lcv++)
			//	MyBrain.BackPropogateStructureTraining();


			//MyBrain.ConnectAllFeedForward();
			MyBrain.ConnectLayeredFeedForward();
		}

		private void TestButton_Click(object sender, System.EventArgs e)
		{
			for( int Lcv = 0; Lcv < 3; Lcv++)
			{
				Color[][] DividedImage = this.BreakupImage(this.TestImages[Lcv], SegmentWidth, SegmentHeight);
				Color[][] SegmentedImage = new Color[DividedImage.Length][];
				SerializedCompressedImage CompressedImageObj = new SerializedCompressedImage();
				CompressedImageObj.CompressedImage = new byte[CompressionSize * DividedImage.Length * 3];
				int CompressedImageIndex = 0;
				CompressedImageIndex = 0;
				for( int SegmentLcv = 0; SegmentLcv < DividedImage.Length; SegmentLcv++)
				{
					double[][] ThisSegment = this.BreakupColors(DividedImage[SegmentLcv]);

					double[][] CurrentOut = new Double[3][];
					MyBrain.SetCurrentInput(ThisSegment[0]);
					MyBrain.PropogateOutput();
					CurrentOut[0] = MyBrain.GetCurrentOutput();
					double[] CompressedOut = this.CompressionLayer.GetOutput();
					for( int CompressLcv = 0; CompressLcv < CompressedOut.Length; CompressLcv++)
					{
						if( CompressedOut[CompressLcv] > 0 )
							CompressedImageObj.CompressedImage[CompressedImageIndex] = (byte)((CompressedOut[CompressLcv] - Math.Floor(CompressedOut[CompressLcv])) * 128);
						else
							CompressedImageObj.CompressedImage[CompressedImageIndex] = (byte)((CompressedOut[CompressLcv] - Math.Floor(CompressedOut[CompressLcv])) * 127);
						CompressedImageIndex++;
					}

					MyBrain.SetCurrentInput(ThisSegment[1]);
					MyBrain.PropogateOutput();
					CurrentOut[1] = MyBrain.GetCurrentOutput();
					CompressedOut = this.CompressionLayer.GetOutput();
					for( int CompressLcv = 0; CompressLcv < CompressedOut.Length; CompressLcv++)
					{
						if( CompressedOut[CompressLcv] > 0 )
							CompressedImageObj.CompressedImage[CompressedImageIndex] = (byte)((CompressedOut[CompressLcv] - Math.Floor(CompressedOut[CompressLcv])) * 128);
						else
							CompressedImageObj.CompressedImage[CompressedImageIndex] = (byte)((CompressedOut[CompressLcv] - Math.Floor(CompressedOut[CompressLcv])) * 127);
						CompressedImageIndex++;
					}

					MyBrain.SetCurrentInput(ThisSegment[2]);
					MyBrain.PropogateOutput();
					CurrentOut[2] = MyBrain.GetCurrentOutput();
					CompressedOut = this.CompressionLayer.GetOutput();
					for( int CompressLcv = 0; CompressLcv < CompressedOut.Length; CompressLcv++)
					{
						if( CompressedOut[CompressLcv] > 0 )
							CompressedImageObj.CompressedImage[CompressedImageIndex] = (byte)((CompressedOut[CompressLcv] - Math.Floor(CompressedOut[CompressLcv])) * 128);
						else
							CompressedImageObj.CompressedImage[CompressedImageIndex] = (byte)((CompressedOut[CompressLcv] - Math.Floor(CompressedOut[CompressLcv])) * 127);
						CompressedImageIndex++;
					}

					SegmentedImage[SegmentLcv] = this.RecombineColors(CurrentOut);
				}

				int WorkingBitmapWidth = (int)Math.Floor(((double)this.TestImages[Lcv].Width)/((double)SegmentWidth))*SegmentWidth;
				int WorkingBitmapHeight = (int)Math.Floor(((double)this.TestImages[Lcv].Height)/((double)SegmentHeight))*SegmentHeight;
				int TotalWidthSegments = WorkingBitmapWidth/SegmentWidth;
				int TotalHeightSegments = WorkingBitmapHeight/SegmentHeight;
				Bitmap ResultImage = this.RecombineImage(SegmentedImage, SegmentWidth, SegmentHeight, TotalWidthSegments, TotalHeightSegments);

				FileStream CompressFileStream = null;
				CompressFileStream = File.Create("C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\Sample Pictures\\Compressed" + Lcv + ".bmp");
				IFormatter Serializer = new BinaryFormatter();
				Serializer.Serialize(CompressFileStream, CompressedImageObj);
				CompressFileStream.Flush();
				CompressFileStream.Close();
                ResultImage.Save("C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\Sample Pictures\\Out" + Lcv + ".bmp");
			}
			this.OutText.Text += "Image saved\r\n";
		}

		private void TrainButton_Click(object sender, System.EventArgs e)
		{
			this.TrainingStartTick = System.Environment.TickCount;

			this.TrainingTimerCyclesLeft = this.TrainingCycles / this.TrainPerTimer;
			this.TrainProgress.Maximum = this.TrainingCycles / this.TrainPerTimer;
			this.TrainLeftTillStruct = this.TrainStructPer;
			this.TrainProgress.Value = 0;
			this.TrainingTimer.Enabled = true;

			this.OutText.Text += "Training has begun\r\n";
		}

		private void ClsButton_Click(object sender, System.EventArgs e)
		{
			this.OutText.Text = "NCI Test\r\n";
		}

		private void TestSimpleButton_Click(object sender, System.EventArgs e)
		{
			this.OutText.Text += this.TestError(10000) + "\r\n";		
		}

		private void TrainingTimer_Tick(object sender, System.EventArgs e)
		{
			for(int Lcv2 = 0; Lcv2 < this.TrainPerTimer; Lcv2++)
			{
				this.TrainOnce();
			}

			this.TrainingTimerCyclesLeft--;
			this.TrainProgress.Value++;
			double TrainPercent = (((double)this.TrainProgress.Value) / ((double)this.TrainProgress.Maximum))*((double)100);
			this.TrainPercentLabel.Text = TrainPercent + " %";
			if( this.TrainingTimerCyclesLeft <= 0 )
			{
				this.TrainingTimer.Enabled = false;

				int EndTick = Environment.TickCount;
				int ElapsedTick = EndTick - this.TrainingStartTick;
				int ElapsedMinutes = (int) Math.Floor((ElapsedTick / 1000.0)/60.0);
				int ElapsedSeconds = (ElapsedTick/1000) - (ElapsedMinutes * 60);
				int ElapsedSecondsFraction = ElapsedTick - (((int)Math.Floor(ElapsedTick/1000.0)) * 1000);

				this.OutText.Text += "Finished Training, Cycles: " + this.TrainCount + " Time: " + ElapsedMinutes + ":" + ElapsedSeconds + "." + ElapsedSecondsFraction + " Error: " + this.TestError(10000) + "\r\n";
			}
		}

		private void StopTrainingButton_Click(object sender, System.EventArgs e)
		{
			this.TrainingTimerCyclesLeft = 1;
		}

		private void SaveButton_Click(object sender, System.EventArgs e)
		{
			SaveFileDialog SaveWindow = new SaveFileDialog();

			SaveWindow.Filter = "dANN files (*.dnn)|*.dnn";
			SaveWindow.RestoreDirectory = true;

			if( SaveWindow.ShowDialog() == DialogResult.OK )
			{
				Stream SaveStream = SaveWindow.OpenFile();
				IFormatter Serializer = new BinaryFormatter();
				Serializer.Serialize(SaveStream, this.MyBrain);
				SaveStream.Flush();
				SaveStream.Close();
			}

			this.OutText.Text += "Network saved\r\n";
		}

		private void LoadButton_Click(object sender, System.EventArgs e)
		{
			OpenFileDialog OpenWindow = new OpenFileDialog();

			OpenWindow.Filter = "dANN files (*.dnn)|*.dnn";
			OpenWindow.RestoreDirectory = true;

			if( OpenWindow.ShowDialog() == DialogResult.OK )
			{
				Stream OpenStream = OpenWindow.OpenFile();
				IFormatter Deserializer = new BinaryFormatter();
				this.MyBrain = (Brain) Deserializer.Deserialize(OpenStream);
				OpenStream.Close();
				this.MyBrain.SetDNA(this.MyDNA);
				this.CompressionLayer = this.MyBrain.InLayer.DestinationLayer.DestinationLayer;
			}

			this.OutText.Text += "Network loaded\r\n";
		}

		private void StructButton_Click(object sender, System.EventArgs e)
		{
			if( this.UseStructureAlgorithm == true )
			{
				this.UseStructureAlgorithm = false;
				this.OutText.Text += "Structure algorithm no longer being used\r\n";
			}
			else
			{
				this.UseStructureAlgorithm = true;
				this.OutText.Text += "Structure algorithm now being used\r\n";
			}
		}




		#endregion

		private void WeightStatButton_Click(object sender, System.EventArgs e)
		{
			//First get the stats fromt he network
			ArrayList BrainStats = MyBrain.GetCurrentWeights();

			IEnumerator BrainEnum = BrainStats.GetEnumerator();
			while( BrainEnum.MoveNext() )
			{
				if( BrainEnum.Current is ArrayList )
				{
					ArrayList CurrentLayerStats = BrainEnum.Current as ArrayList;
					this.OutText.Text += "--Begin Layer-- : " + CurrentLayerStats.Count + "\r\n";

					IEnumerator LayerEnum = CurrentLayerStats.GetEnumerator();
					while( LayerEnum.MoveNext() )
					{
						if( LayerEnum.Current is Array )
						{
							double[] CurrentNeuronStats = LayerEnum.Current as double[];
							this.OutText.Text += "--Begin Neuron-- : " + CurrentNeuronStats.Length + "\r\n";


							for( int SynapseLcv = 0; SynapseLcv < CurrentNeuronStats.Length; SynapseLcv++)
							{
								this.OutText.Text += CurrentNeuronStats[SynapseLcv] + "\r\n";
							}

						}
						else
							throw new Exception("Unexpected item in ArrayList");
					}
				}
				else
					throw new Exception("Unexpected item in ArrayList");

				this.OutText.Text += "\r\n";
			}
		}
	}

	[Serializable]
	class SerializedCompressedImage
	{
		public byte[] CompressedImage;
	}
}