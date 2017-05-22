package trainningData;
/**
 * build Naive bayes model with training data, and evaluate the training data
 * @author shuzhang
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
//import java.util.Arrays;

public class WorProb {
	int[][] wordCounts = new int[61188][20];
	int[] groupTotalWords = new int[20];
	double[][] PMLE = new double[61188][20];
	double[][] PBE = new double[61188][20];
	int[] groupDocs = new int[] { 480, 581, 572, 587, 575, 592, 582, 592, 596, 594, 598, 594, 591, 594, 593, 599, 545,
			564, 464,376};
	double[][] NB = new double[11269][20];
	double[] prior = new double[] {0.04259472890229834, 0.05155736977549028, 0.05075871860857219, 0.05208980388676901, 0.051024935664211554, 0.052533498979501284, 0.051646108794036735, 0.052533498979501284, 0.052888455053687104, 0.0527109770165942, 0.05306593309078002, 0.0527109770165942, 0.05244475996095483, 0.0527109770165942, 0.052622237998047744, 0.05315467210932647, 0.04836276510781791, 0.05004880646020055, 0.04117490460555506, 0.033365870973467035};
	int[] maxIndex = new int[11269];
	double[] maxValue = new double[11269];
	int[] accurateDocsInGroup = new int[20];
	double[] accuracyOfGroup = new double[20];
	double overallAccuracy=0;

	// ArrayList<Integer> groupTotalWords= new ArrayList<>();

	public static void main(String[] args) throws IndexOutOfBoundsException {
		WorProb wp = new WorProb();
		wp.setWodProb();
		wp.initialNBWithPrior();
		 System.out.println(Arrays.deepToString(wp.getNB()));
	//	System.out.println(Arrays.toString(wp.getGroupTotalWords()));

		// System.out.println(Arrays.deepToString(wp.getWordCounts()));
		// System.out.println(Arrays.toString(wp.getGroupTotalWords()));
		
		/**  for(int i=0;i<5;i++){ for(int j=0;j<20;j++){
		  System.out.println("PMLE: " + wp.getPMLE()[i][j] + " PBE: "+
		  wp.getPBE()[i][j]); } }
		  
		  for(int i=60000;i<60005;i++){ for(int j=0;j<20;j++){
		  System.out.println("PMLE: " + wp.getPMLE()[i][j] + " PBE: "+
		  wp.getPBE()[i][j]);
		  
		  } }	  
		  }*/
		 
		/**wp.initialNBWithPrior();
		wp.setClassifier();
		 for(int i=0;i<5;i++){
			 for(int j=0;j<20;j++){
				 System.out.println(wp.getNB()[i][j]);
			 }
		 }
		 for(int i=11260;i<11269;i++){
			 for(int j=0;j<20;j++){
				 System.out.println(wp.getNB()[i][j]);
			 }
		 }*/
	
	
		/**wp.initialNBWithPrior();
		wp.setClassifier();
		for(int i=11267;i<11269;i++){
			 for(int j=0;j<20;j++){
				 System.out.println(wp.getNB()[i][j]);
			 }
		 }*/

	 //System.out.println(Arrays.deepToString(wp.getNB()));
		wp.setClassifier();
		wp.maxIndex();
		// System.out.println(Arrays.toString(wp.getMaxValue()));
		System.out.println(Arrays.toString(wp.getMaxIndex()));
		wp.accuracy();
		System.out.println(Arrays.toString(wp.getAccurateDocsInGroup()));
		System.out.println(Arrays.toString(wp.getAccuracyOfGroup()));
		wp.overallAccuracy();
		System.out.println(wp.getOverallAccuracy());
	}

	public int[][] getWordCounts() {
		return wordCounts;
	}

	public int[] getGroupTotalWords() {
		return groupTotalWords;
	}

	public double[][] getPMLE() {
		return PMLE;
	}

	public double[][] getPBE() {
		return PBE;
	}

	/**
	 * calculate total words n in each class
	 */

	public void setWodProb() {
		String csvFile1 = "E:\\CS573\\src\\cs573Project1\\train_data.csv";
		String line1 = "";
		String[] s2;

		int docIDMax = groupDocs[0];
		int i = 0;
		int accumulatedWordsInGroup = 0;

		try (BufferedReader br1 = new BufferedReader(new FileReader(csvFile1))) {

			while ((line1 = br1.readLine()) != null) {
				s2 = line1.split(",");

				if (Integer.parseInt(s2[0]) <= docIDMax) {
					accumulatedWordsInGroup += Integer.parseInt(s2[2]);

				} else {
					if (i < groupTotalWords.length)
						groupTotalWords[i] = accumulatedWordsInGroup;
					accumulatedWordsInGroup = Integer.parseInt(s2[2]);
					i++;
					if (i < groupDocs.length) {
						docIDMax += groupDocs[i];
					}

				}
				if (Integer.parseInt(s2[1]) <= wordCounts.length && i < wordCounts[0].length)
					wordCounts[Integer.parseInt(s2[1]) - 1][i] += Integer.parseInt(s2[2]);

			}
			if(i<groupTotalWords.length)
			groupTotalWords[i]=accumulatedWordsInGroup;
			
			for (int k = 0; k < wordCounts[0].length; k++) {
				for (int s = 0; s < wordCounts.length; s++) {

					PMLE[s][k] = wordCounts[s][k] * 1.0 / groupTotalWords[k];
					PBE[s][k] = (wordCounts[s][k] + 1) * 1.0 / (groupTotalWords[k] + 61188);

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void initialNBWithPrior() {

		for (int i = 0; i < 11269; i++) {

			for (int j = 0; j < 20; j++) {
				// if(j<a.getPrior().size())
				if (j < prior.length)
					NB[i][j] = Math.log(prior[j]);
			}
		}
	}

	public void setClassifier() {
		String csvFile1 = "E:\\CS573\\src\\cs573Project1\\train_data.csv";
		String line1 = "";
		String[] s2;

		int i = 0;
		initialNBWithPrior();

		try (BufferedReader br1 = new BufferedReader(new FileReader(csvFile1))) {

			while ((line1 = br1.readLine()) != null) {
				s2 = line1.split(",");

				if (Integer.parseInt(s2[0]) > i + 1) {
					i++;
				}

				// NB for doc i in class j
				for (int j = 0; j < 20; j++) {
					if (i < NB.length && Integer.parseInt(s2[1]) <= PBE.length && j < PBE[0].length)
						NB[i][j] += Math.log(PBE[Integer.parseInt(s2[1]) - 1][j]);
					// System.out.println(NB[i][j]);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public double[][] getNB() {
		return NB;
	}

	public int[] maxIndex() {
		Arrays.fill(maxValue, -Double.MAX_VALUE);
		for (int i = 0; i < NB.length; i++) {
			for (int j = 0; j < NB[0].length; j++) {
				if (NB[i][j] > maxValue[i]) {
					maxValue[i] = NB[i][j];
					maxIndex[i] = j;
				}
			}
		}
		return maxIndex;
	}

	public double[] getMaxValue() {
		return maxValue;
	}

	public int[] getMaxIndex() {
		return maxIndex;
	}

	public void accuracy() {
		int docIDMax = groupDocs[0];
		int k = 0;

		for (int i = 0; i < maxIndex.length; i++) {
			if (i < docIDMax) {
				if (maxIndex[i] == k && k < accurateDocsInGroup.length)
					accurateDocsInGroup[k]++;
			} else if (i>docIDMax&&k < accuracyOfGroup.length && k < groupDocs.length && k < accurateDocsInGroup.length) {
				accuracyOfGroup[k] = accurateDocsInGroup[k] * 1.0 / groupDocs[k];
				k++;
				if (k < groupDocs.length) {
					docIDMax += groupDocs[k];
				}

			}

		}
		if(k < accuracyOfGroup.length && k < groupDocs.length && k < accurateDocsInGroup.length)
			accuracyOfGroup[k]=accurateDocsInGroup[k] * 1.0 / groupDocs[k];

	}

	public int[] getAccurateDocsInGroup() {
		return accurateDocsInGroup;
	}

	public double[] getAccuracyOfGroup() {
		return accuracyOfGroup;
	}
	
	public double overallAccuracy(){
		int totalAccurateDocs=0;
		for(int i=0;i<accurateDocsInGroup.length;i++){
			totalAccurateDocs+=accurateDocsInGroup[i];
		}
		overallAccuracy=totalAccurateDocs*1.0/11269;
		return overallAccuracy;
	}
	
	public double getOverallAccuracy() {
		return overallAccuracy;
	}

}