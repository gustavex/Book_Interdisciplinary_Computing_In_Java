import java.lang.*;
import java.awt.*;
import java.awt.event.*;

class FDialog extends Dialog implements ActionListener {
	TextField nheadTF, colnxTF, ncolnTF;
	Integer nheadI, colnxI, ncolnI;
	int nhead, colnx, ncoln;
	
	MyWindow parent;
	
	public FDialog(MyWindow parent, String text) {
		super(parent, text, true);
		setBackground(Color.white);
		this.parent = parent;
		
		setLayout(new GridLayout(4,1));
		
		nhead = 4;
		colnx = 1;
		ncoln = 5;
		
		Panel nheadP = new Panel();
		Panel colnxP = new Panel();
		Panel ncolnP = new Panel();
		
		nheadI = new Integer(nhead);
		colnxI = new Integer(colnx);
		ncolnI = new Integer(ncoln);
		
		nheadTF = new TextField(nheadI.toString(),6);
		colnxTF = new TextField(colnxI.toString(),6);
		ncolnTF = new TextField(ncolnI.toString(),6);
		
		nheadP.add(new Label("# of header lines to ignore:", Label.LEFT));
		nheadP.add(nheadTF);
		add(nheadP);
		
		ncolnP.add(new Label("Total # of columns:     ", Label.LEFT));
		ncolnP.add(ncolnTF);
		add(ncolnP);
		
		colnxP.add(new Label("Column   ", Label.LEFT));
		colnxP.add(colnxTF);
		colnxP.add(new Label("  is x coordinates", Label.LEFT));
		add(colnxP);
		
		Panel bbP = new Panel();
		Button button1 = new Button("  OK     ");
		Button button2 = new Button(" Cancel  ");
		button1.addActionListener(this);
		button2.addActionListener(this);
		
		bbP.add(button1);
		bbP.add(button2);
		
		add(bbP);
		setSize(new Dimension(320,150));
	}

	public void actionPerformed(ActionEvent e) {
		if("  OK     ".equals(e.getActionCommand())) {
			nhead = nheadI.parseInt(nheadTF.getText());
			ncoln = ncolnI.parseInt(ncolnTF.getText());
			colnx = ncolnI.parseInt(colnxTF.getText());
			parent.nSkips = nhead;
			parent.nColns = ncoln;
			parent.xIndex = colnx;
			dispose();
		}
		if (" Cancel  ".contentEquals(e.getActionCommand())) {
			dispose();
		}
	}
}
