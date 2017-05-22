package trainningData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

public class Prior {
	private ArrayList<Integer> groupDocs = new ArrayList<>();
	private ArrayList<Double> prior = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		Prior a = new Prior();
		a.setGroupDocs();
		System.out.println(a.getPrior());
		System.out.println(a.getGroupDocs());
	}

	public void setGroupDocs() {

		/**
		 * calculate prior for each news group: #of docs in group i/total docs
		 * in the 20groups. Nc/N.
		 */

		String csvFile = "E:\\CS573\\src\\cs573Project1\\train_label.csv";
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

					groupDocs.add(groupCounts);

					groupCounts = 1;
					index++;
				}

			}
			groupDocs.add(groupCounts);
			for (int i = 0; i < groupDocs.size(); i++) {

				prior.add(groupDocs.get(i) * 1.0 / sum);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<Integer> getGroupDocs() {
		return groupDocs;
	}

	public ArrayList<Double> getPrior() {
		return prior;
	}

}
