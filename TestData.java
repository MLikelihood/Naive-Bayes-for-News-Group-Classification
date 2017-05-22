package trainningData;
/**
 * evaluate the test data using the same prior and BE estimator from training data
 * @author shuzhang
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class TestData {
	int[] groupDocsT = new int[] {318, 389, 391, 392, 383, 390, 382, 395, 397, 397, 399, 395, 393, 393, 392, 398, 364, 376, 310, 251};
	double[] prior = new double[] {0.04259472890229834, 0.05155736977549028, 0.05075871860857219, 0.05208980388676901, 0.051024935664211554, 0.052533498979501284, 0.051646108794036735, 0.052533498979501284, 0.052888455053687104, 0.0527109770165942, 0.05306593309078002, 0.0527109770165942, 0.05244475996095483, 0.0527109770165942, 0.052622237998047744, 0.05315467210932647, 0.04836276510781791, 0.05004880646020055, 0.04117490460555506, 0.033365870973467035};
	double[][] NBT = new double[7505][20];
	int[] maxIndexT = new int[7505];
	double[] maxValueT = new double[7505];
	int[] accurateDocsInGroupT = new int[20];
	double[] accuracyOfGroupT = new double[20];
	double overallAccuracyT=0;
	public static void main(String[] args) throws IndexOutOfBoundsException {
		TestData t=new TestData();
		WorProb w=new WorProb();
		w.setWodProb();
		//System.out.println(Arrays.deepToString(w.getPMLE()));
		t.initialNBTWithPrior();
		//System.out.println(Arrays.deepToString(t.getNBT()));
		t.setClassifier();
		System.out.println(Arrays.deepToString(t.getNBT()));
		t.maxIndexT();
		/**System.out.println(Arrays.toString(t.getMaxValue()));
		System.out.println(Arrays.toString(t.getMaxIndex()));
		t.accuracy();
		System.out.println(Arrays.toString(t.getAccurateDocsInGroup()));
		System.out.println(Arrays.toString(t.getAccuracyOfGroup()));
		t.overallAccuracy();
		System.out.println(t.getOverallAccuracy());*/
	}
	
	public void initialNBTWithPrior() {
          
		for (int i = 0; i < 7505; i++) {

			for (int j = 0; j < 20; j++) {
				// if(j<a.getPrior().size())
				if (j < prior.length)
					NBT[i][j] = Math.log( prior[j]);
			}
		}
	}
	public void setClassifier() {
		String csvFile1 = "E:\\coms227\\TextClassifier0\\src\\trainningData\\test_data.csv";
		String line1 = "";
		String[] s2;
		WorProb wb=new WorProb();
		int i = 0;
		

		try (BufferedReader br1 = new BufferedReader(new FileReader(csvFile1))) {

			while ((line1 = br1.readLine()) != null) {
				s2 = line1.split(",");

				if (Integer.parseInt(s2[0]) > i + 1) {
					i++;
				}

				// NB for doc i in class j
				for (int j = 0; j < 20; j++) {
					if (i < NBT.length && Integer.parseInt(s2[1]) <= wb.getPBE().length && j < wb.getPBE()[0].length)
						NBT[i][j] += Math.log(wb.getPBE()[Integer.parseInt(s2[1]) - 1][j]);
					
					
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public double[][] getNBT() {
		return NBT;
	}

	public int[] maxIndexT() {
		Arrays.fill(maxValueT, -Double.MAX_VALUE);
		for (int i = 0; i < NBT.length; i++) {
			for (int j = 0; j < NBT[0].length; j++) {
				if (NBT[i][j] > maxValueT[i]) {
					maxValueT[i] = NBT[i][j];
					maxIndexT[i] = j;
				}
			}
		}
		return maxIndexT;
	}

	public double[] getMaxValue() {
		return maxValueT;
	}

	public int[] getMaxIndex() {
		return maxIndexT;
	}

	
	
	public void accuracy() {
		
		
		int docIDMax= groupDocsT[0];
		int k = 0;

		for (int i = 0; i < maxIndexT.length; i++) {
			if (i < docIDMax) {
				if (maxIndexT[i] == k && k < accurateDocsInGroupT.length)
					accurateDocsInGroupT[k]++;
			} else if (i>docIDMax&&k < accuracyOfGroupT.length && k < groupDocsT.length&& k <accurateDocsInGroupT.length) {
				accuracyOfGroupT[k] = accurateDocsInGroupT[k] * 1.0 / groupDocsT[k];
				k++;
				if (k <groupDocsT.length) {
					docIDMax +=  groupDocsT[k];
				}

			}

		}
		if(k < accuracyOfGroupT.length && k < groupDocsT.length && k < accurateDocsInGroupT.length)
			accuracyOfGroupT[k]=accurateDocsInGroupT[k] * 1.0 /groupDocsT[k];

	
	}
	public int[] getAccurateDocsInGroup() {
		return accurateDocsInGroupT;
	}

	public double[] getAccuracyOfGroup() {
		return accuracyOfGroupT;
	}
	
	public double overallAccuracy(){
		int totalAccurateDocs=0;
		for(int i=0;i<accurateDocsInGroupT.length;i++){
			totalAccurateDocs+=accurateDocsInGroupT[i];
		}
		overallAccuracyT=totalAccurateDocs*1.0/7505;
		return overallAccuracyT;
	}
	
	public double getOverallAccuracy() {
		return overallAccuracyT;
	}

}

