import java.awt.*;
import java.io.*;
import java.lang.*;
import java.awt.event.*;
import java.awt.print.*;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;

public class MyWindow extends Frame implements ActionListener, Printable {
	Plotter plotting;
	int nColns, nLines, xIndex, nSkips;
	double[][] Table;
	boolean beforePlot = true;
	
	public static void main(String args[]) {
		MyWindow demo = new MyWindow();
		demo.show();
	}
	
	public MyWindow() {
		super();
		setTitle("MyWindowDemo");
		
		nColns = 5;
		xIndex = 1;
		nSkips = 4;
		
		Font font = new Font("Dialog", Font.BOLD, 13);
		setFont(font);
		
		plotting = new Plotter(this);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);} 
			});
		
		addMenus();
		
		Panel mypanel = new Panel();
		mypanel.setLayout(new BorderLayout());
		mypanel.add(plotting, BorderLayout.CENTER);
		add(mypanel);
		
		pack();
		
		setSize(new Dimension(500, 500));
	}
	
	private void addMenus() {
		MenuBar mymenubar = new MenuBar();
		
		Menu myfile = new Menu("File");
		myfile.add("Open");
		myfile.add("Save");
		myfile.addSeparator();
		myfile.add("Quit");
		
		Menu format = new Menu("Format");
		format.add("Import");
		
		Menu operate = new Menu("View");
		operate.add("Plot");
		operate.add("Print");
		
		myfile.addActionListener(this);
		format.addActionListener(this);
		operate.addActionListener(this);
		
		mymenubar.add(myfile);
		mymenubar.add(format);
		mymenubar.add(operate);
		setMenuBar(mymenubar);
	}
	
	public void actionPerformed(ActionEvent e) {
		String action_is = e.getActionCommand();
		
		if (action_is.equals("Quit")) {
			System.exit(0);
		} else if (action_is.equals("Open")) {
			FileDialog opendlg = new FileDialog(this, "Open File", FileDialog.LOAD);
			opendlg.show();
			String infile = opendlg.getDirectory() + opendlg.getFile();
			if(opendlg.getFile() != null) {
				Message readingBox = new Message(this, "My Window", "Reading file...");
				readingBox.show();
				loadData(infile);
				readingBox.dispose();
			}
 		} else if (action_is.equals("Import")) {
 			FDialog formatdlg = new FDialog(this, "Format Dialog");
 			formatdlg.show();
 		} else if (action_is.equals("Save")) {
 			FileDialog savedlg = new FileDialog(this, "Save File As...", FileDialog.SAVE);
 			savedlg.show();
 			String outfile = savedlg.getDirectory() + savedlg.getFile();
 			Message savingBox = new Message(this, "MyWindow", "Saving file...");
 			savingBox.show();
 			writeData(outfile);
 			savingBox.dispose();
 		} else if (action_is.equals("Plot")) {
 			assignArrays();
 			Message plottingBox = new Message(this, "MyWindow", "Plotting file...");
 			plottingBox.show();
 			beforePlot = false;
 			plotting.repaint();
 			plottingBox.dispose();
 		} else if (action_is.equals("Print")) {
 			PrinterJob printJob = PrinterJob.getPrinterJob();
 			PageFormat pf = new PageFormat();
 			pf.setOrientation(pf.PORTRAIT);
 			printJob.setPrintable(this, pf);
 			if (printJob.printDialog()) {
 				try {
 					printJob.print();
 				} catch (Exception ex) {
 					ex.printStackTrace();
 				}
 			}
 		}
	}
	
	public int print(Graphics g, PageFormat pf, int pi) {
		if (pi >= 1) return Printable.NO_SUCH_PAGE;
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(pf.getImageableX(), pf.getImageableY());
		plotting.paint(g2);
		return Printable.PAGE_EXISTS;
	}
	
	public void assignArrays() {
		int j = 0;
		
		plotting.x = Table[xIndex-1];
		plotting.y = new double[nColns-1][];
		
		for (int i=0; i<nColns; i++) {
			if (i != (xIndex-1)) {
				plotting.y[j] = Table[i];
				j += 1;
			}
		}
	}
	
	public void loadData(String infile) {
		int itmp;
		String stmp;
		
		nLines = 0;
		try {
			FileInputStream fis = new FileInputStream(infile);
			InputStreamReader br = new InputStreamReader(fis);
			LineNumberReader re = new LineNumberReader(br);
			
			itmp = -1;
			while (itmp != nLines) {
				itmp = nLines;
				stmp = re.readLine();
				nLines = re.getLineNumber();
			}
			fis.close();
		} catch (IOException e) {
			System.out.println("IOException: "+e.getMessage());
		}
		
		Table = new double[nColns][nLines-nSkips];
		
		try {
			FileInputStream fis = new FileInputStream(infile);
			InputStreamReader br = new InputStreamReader(fis);
			BufferedReader re = new BufferedReader(br);
			StreamTokenizer sto = new StreamTokenizer(re);
			
			for (int i=0; i<nSkips; i++) stmp = re.readLine();
			
			for (int i=0; i<(nLines-nSkips); i++) {
				for (int j=0; j<nColns; j++) {
					Table[j][i] = readNumber(sto);
				}
			}
			fis.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println("IOException: "+fnfe.getMessage());
		} catch (IOException e) {
			System.out.println("IOException: "+e.getMessage());
		}
	}
	
	public double readNumber(StreamTokenizer sto) throws IOException {
		double output;
		Integer integer;
		
		sto.nextToken();
		output = sto.nval;
		sto.nextToken();
		if (sto.ttype == StreamTokenizer.TT_WORD) {
			if (sto.sval.length() > 1 && sto.sval.substring(0,1).equalsIgnoreCase("E")) {
				integer = new Integer(sto.sval.substring(2));
				if (sto.sval.substring(1,2).equals("-")) {
					output /= Math.pow(10.0, integer.doubleValue());
				} else {
					output *= Math.pow(10.0, integer.doubleValue());
				}
			} else if (sto.sval.length() == 1 && sto.sval.substring(0,1).equalsIgnoreCase("E")) {
				sto.nextToken();
				sto.nextToken();
				output *= Math.pow(10.0, sto.nval);
			} else  System.out.println("Error in the number format");
		} else
			sto.pushBack();
	
		return output;
	}
	
	public void writeData(String outfile) {
		try {
			FileOutputStream ostream = new FileOutputStream(outfile);
			PrintWriter pw = new PrintWriter(ostream);
			
			DecimalFormat df1 = new DecimalFormat("0000");
			DecimalFormat df2 = new DecimalFormat("0.0000");
			
			String fm = " ";
			
			for (int i=0; i<Table[0].length; i++) {
				for (int k=0; k<Table.length; k++) {
					if (k==0) fm = df1.format(Table[k][i]);
					else fm = df2.format(Table[k][i]);
					pw.print(fm+" ");
				}
				pw.println(" ");
			}
			
			pw.flush();
			ostream.close();
		} catch (IOException ee) {
			System.out.println("IOException: "+ee.getMessage());
		}
	}
}