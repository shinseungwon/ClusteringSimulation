import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.SecureRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainUI extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JPanel canvas;
	
	static int numCluster = 0;
	static int numNode = 0;	
	static final int tic = 100;
	
	Clustering C;
	
	JTextField GenerateNode;
	JTextField GenerateCluster;
	
	Label[] nodes;
	
	Label[] clusters;
	
	MainUI(String title){
		super(title);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		init();
		super.setSize(700, 540);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frm = super.getSize();
		int xpos = (int) (screen.getWidth() / 2 - frm.getWidth() / 2);
		int ypos = (int) (screen.getHeight() / 2 - frm.getWidth() / 2);
		super.setLocation(xpos, ypos);
		super.setVisible(true);	
	}
	
	private void init(){
		this.setLayout(null);
		
		
		canvas = new JPanel();
		canvas.setBackground(Color.white);	
		canvas.setLayout(null);
		
		canvas.setBounds(10,10,480,480);
		this.add(canvas);
		

		JPanel ButtonPanel = new JPanel();
		JLabel GenerateNodeLabel = new JLabel("Number of Nodes : ");
		GenerateNode = new JTextField(5);
		
		JButton Generate = new JButton("Generate");
		JLabel GenerateClusterLabel = new JLabel("Number of Clusters : ");
		GenerateCluster = new JTextField(2);
		JButton StartClustering = new JButton("Start Clustering");
		ButtonPanel.setLayout(null);
		
		ButtonPanel.add(GenerateNodeLabel);
		GenerateNodeLabel.setBounds(0,10,110,20);
		ButtonPanel.add(GenerateNode);
		GenerateNode.setBounds(110,10,60,20);		
		ButtonPanel.add(Generate);
		Generate.setBounds(0,40,170,30);
		
		ButtonPanel.add(GenerateClusterLabel);
		GenerateClusterLabel.setBounds(0,400,120,20);
		ButtonPanel.add(GenerateCluster);
		GenerateCluster.setBounds(120,400,50,20);
		ButtonPanel.add(StartClustering);
		StartClustering.setBounds(0,430,180,50);
		
		this.add(ButtonPanel);
		ButtonPanel.setBounds(500,10,180,480);
		
		Generate.addActionListener(new onClickGenerate());
		StartClustering.addActionListener(new onClickStartClustering());
		
	}
	
	class onClickGenerate implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			C = new Clustering("Test",2,0);

			 double[] input;			 
			 SecureRandom R= new SecureRandom(); 
			 for(int i=0;i<Integer.parseInt(GenerateNode.getText());i++){ 
				 input=new double[2];
				 for(int j=0;j<input.length;j++)
					 input[j]=R.nextDouble()*480;		 
			 C.addNode("Node "+i, input);
			 }
			 
			 nodes = C.getLabels();
			 
			 for(int i=0;i<nodes.length;i++)
				 canvas.add(nodes[i]);		
			 
			  
		}
		
	}
	
	class onClickStartClustering implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			C.setCluster(Integer.parseInt(GenerateCluster.getText()));			
			clusters = C.getCLabels();
			
			 for(int i=0;i<clusters.length;i++)
				 canvas.add(clusters[i]);
			 
			 C.DoClustering();
		}
		
	}
}
