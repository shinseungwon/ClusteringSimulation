

import java.awt.Color;
import java.awt.Label;
import java.security.SecureRandom;
import java.util.ArrayList;

public class Clustering {
	// 클러스터 모체 클래스
	private String tag;
	private int numberOfPoint = 0;
	private ArrayList<Node> Nodes;
	private ArrayList<Cluster> Clusters;
	private int numberOfElement = 0;
	private int numberOfCluster = 0;
	private double changeRate = 10;
	private double limit;
	
	private Label[] NodeLabel;
	private Label[] ClusterLabel;

	Clustering(String tag, int Element, int Cluster) {
		this.tag = tag;
		this.numberOfCluster = Cluster;
		this.numberOfElement = Element;
		Nodes = new ArrayList<>();
		Clusters = new ArrayList<>();
	}
	
	public Label[] getLabels(){
		NodeLabel = new Label[Nodes.size()];
		
		
		for(int i=0;i<NodeLabel.length;i++){
			Nodes.get(i).L = new Label("");
			Nodes.get(i).L.setBounds((int)Nodes.get(i).Elements[0]-5,(int)Nodes.get(i).Elements[1]-5,10,10);
			Nodes.get(i).L.setBackground(Color.gray);
			
			NodeLabel[i] = Nodes.get(i).L; 
						
		}
		
		return NodeLabel;
	}
	
	public Label[] getCLabels(){
		ClusterLabel = new Label[Clusters.size()];
		
		SecureRandom R = new SecureRandom();
		for(int i=0;i<ClusterLabel.length;i++){
			Clusters.get(i).L = new Label(""+i);
			Clusters.get(i).L.setBounds((int)Clusters.get(i).Address[0]-1,(int)Clusters.get(i).Address[1]-1,3,3);
			Clusters.get(i).L.setBackground(new Color(R.nextInt(255),R.nextInt(255),R.nextInt(255)));
			
			ClusterLabel[i] = Clusters.get(i).L; 

		}
		
		return ClusterLabel;
	}
	
	public void setColors(){
		for(int i=0;i<Nodes.size();i++)
			Nodes.get(i).L.setBackground(Nodes.get(i).master.L.getBackground());		
	}
	
	public void setLocs(){
		for(int i=0;i<Clusters.size();i++)
			Clusters.get(i).L.setBounds((int)Clusters.get(i).Address[0]-1, (int)Clusters.get(i).Address[1]-1,3,3);
			
		
	}

	public void addNode(String tag, double[] Elements) {
		if (numberOfElement == 0)
			return;
		if (numberOfElement != Elements.length)
			return;

		Nodes.add(new Node(tag, Elements));
		numberOfPoint++;
	}
	
	public void setCluster(int cluster){
		
		numberOfCluster = cluster;
		
		limit = 0.0;

		double[] mid = new double[numberOfElement];

		for (int i = 0; i < Nodes.size(); i++)
			for (int j = 0; j < numberOfElement; j++)
				mid[j] += Nodes.get(i).Elements[j];

		for (int j = 0; j < numberOfElement; j++)
			mid[j] /= Nodes.size();
		
		for (int i = 0; i < Nodes.size(); i++)
			limit += getDist(mid, Nodes.get(i).Elements);

		limit /= Nodes.size() * changeRate;
		
		SecureRandom R = new SecureRandom();
		double[] clusteraddr;
		for (int i = 0; i < numberOfCluster; i++) {
			clusteraddr = new double[numberOfElement];
			for (int j = 0; j < numberOfElement; j++)
				clusteraddr[j] = R.nextDouble() * limit * (100 / changeRate) + mid[j];

			Clusters.add(new Cluster("Cluster " + i, clusteraddr));
		}
	}

	public void DoClustering() {
		if (numberOfCluster <= 0 || numberOfElement <= 0)
			return;

		double change = limit + 1.0;
		Cluster tempc;
		while (change > limit) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
			change = 0.0;
			// Find Nearest Cluster
			for (int i = 0; i < numberOfCluster; i++)
				Clusters.get(i).slaves.clear();

			for (int i = 0; i < Nodes.size(); i++) {
				tempc = getNearest(Nodes.get(i));
				Nodes.get(i).master = tempc;
				tempc.slaves.add(Nodes.get(i));
			}

			for (int i = 0; i < numberOfCluster; i++)
				change += Clusters.get(i).setAddress();
			
			setColors();
			setLocs();

		}

	}


	private Cluster getNearest(Node n) {
		if (numberOfCluster == 0)
			return null;
		Cluster C = Clusters.get(0);
		double value = getDist(Clusters.get(0).Address, n.Elements);

		double temp;
		for (int i = 0; i < numberOfCluster; i++) {
			if ((temp = getDist(Clusters.get(i).Address, n.Elements)) < value) {
				C = Clusters.get(i);
				value = temp;
			}
		}

		return C;
	}

	private double getDist(double[] a, double[] b) {
		double sum = 0;

		for (int i = 0; i < numberOfElement; i++)
			sum += (a[i] - b[i]) * (a[i] - b[i]);

		return Math.sqrt(sum);
	}

	public void printAllNodes() {
		System.out.println(tag + " Nodes");

		for (int i = 0; i < numberOfPoint; i++) {
			System.out.print(Nodes.get(i).tag + " : ");
			for (int j = 0; j < numberOfElement; j++)
				System.out.print(
						"( " + j + " : " + Double.parseDouble(String.format("%.2f", Nodes.get(i).Elements[j])) + ") ");

			System.out.println();
		}
	}

	public void printAllCluster() {
		System.out.println(tag + " Clusters");

		for (int i = 0; i < numberOfCluster; i++) {
			System.out.println("Cluster " + i + " : " + Clusters.get(i).slaves.size());
			for (int j = 0; j < Clusters.get(i).slaves.size(); j++) {
				System.out.print(Clusters.get(i).slaves.get(j).tag + ",");
				if ((j + 1) % 5 == 0)
					System.out.println();
			}
			System.out.println();
		}
	}

	// inner classes

	// node
	private class Node {
		Cluster master;
		String tag;
		double[] Elements;
		Label L;
		Node(String tag, double[] Elements) {
			this.tag = tag;
			this.Elements = Elements;
		}
		
	}

	// cluster
	private class Cluster {
		ArrayList<Node> slaves;
		String tag;
		double[] Address;
		Label L;
		Cluster(String tag, double[] Address) {
			this.tag = tag;
			this.Address = Address;
			slaves = new ArrayList<>();
		}

		double[] temp;

		public double setAddress() {
			temp = new double[numberOfElement];

			for (int i = 0; i < numberOfElement; i++)
				temp[i] = 0.0;

			for (int i = 0; i < slaves.size(); i++)
				for (int j = 0; j < numberOfElement; j++)
					temp[j] += slaves.get(i).Elements[j];

			for (int i = 0; i < numberOfElement; i++)
				temp[i] /= slaves.size();

			double diff = getDist(Address, temp);

			Address = temp;

			return diff;
		}
	}
}
