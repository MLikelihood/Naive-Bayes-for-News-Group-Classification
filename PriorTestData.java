package trainningData;

	import java.io.BufferedReader;
	import java.io.FileReader;
	import java.io.IOException;

	import java.util.ArrayList;

	public class PriorTestData {
		private ArrayList<Integer> groupDocsTest = new ArrayList<>();
		private ArrayList<Double> priorTest = new ArrayList<>();

		public static void main(String[] args) throws IOException {
			PriorTestData a = new PriorTestData();
			a.setGroupDocs();
			System.out.println(a.getPriorTest());
			System.out.println(a.getGroupDocsTest());
		}

		public void setGroupDocs() {

			/**
			 * calculate prior for each news group: #of docs in group i/total docs
			 * in the 20groups. Nc/N.
			 */

			String csvFile = "E:\\coms227\\TextClassifier0\\src\\trainningData\\test_label.csv";
			String line = "";

			int index = 1;
			int groupCounts = 0;
			int sum = 0;
			try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

				while ((line = br.readLine()) != null) {
					String[] s1 = line.split(" ");
					sum++;
					if (Integer.parseInt(s1[0]) == index) {
						groupCounts++;
					} else {

						groupDocsTest.add(groupCounts);

						groupCounts = 1;
						index++;
					}

				}
				groupDocsTest.add(groupCounts);
				for (int i = 0; i < groupDocsTest.size(); i++) {

					priorTest.add(groupDocsTest.get(i) * 1.0 / sum);

				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public ArrayList<Integer> getGroupDocsTest() {
			return groupDocsTest;
		}

		public ArrayList<Double> getPriorTest() {
			return priorTest;
		}

	}




