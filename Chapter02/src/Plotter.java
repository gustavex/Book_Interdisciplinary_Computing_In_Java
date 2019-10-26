import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.font.*;
import java.text.DecimalFormat;

class Plotter extends Canvas {
	MyWindow parent;
	
	double xmin, xmax, ymin, ymax;
	double topborder, sideborder;
	static int bottom, right;
	int rectWidth = 6, rectHeight = 6;
	
	double x[];
	double y[][];
	
	final static int maxCharHeight = 20;
	final static int minFontSize = 8;
	
	public Plotter(MyWindow parent) {
		super();
		this.parent = parent;
	}
	
	public void paint(Graphics g) {
		final Color bg = Color.white;
		final Color fg = Color.black;
		final Color red = Color.red;
		final Color white = Color.white;
		
		final BasicStroke stroke = new BasicStroke(1.0f);
		final BasicStroke wideStroke = new BasicStroke(8.0f);
		
		final float dash1[] = {10.0f};
		final BasicStroke dashed1 = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
		final float dash2[] = {2.0f};
		final BasicStroke dashed2 = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash2, 0.0f);
		FontMetrics fontMetrics;
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Dimension d = getSize();
		
		fontMetrics = pickFont(g2, "WELCOME", d.width);
		
		int rectWidth = 6, rectHeight = 6;
		int j,x0,y0,x1,y1;
		
		if ((d.width != right) || (d.height != bottom)) {
			bottom = d.height;
			right = d.width;
			SetScreenSize(right, bottom);
		}
		
		if (parent.beforePlot == false) {
			setBackground(Color.white);
			SetPlottingLimits();
			SetBorderSize(0.15,0.15);
			
			fontMetrics = pickFont(g2, "Vth = 200 mV", (d.width/6));
			DrawXAxis(g2);
			DrawYAxis(g2);
			putAxisTitles(g2,stroke,d,"efficiency",20,"plane H.V. (Volts)",-50);
			
			drawPieces(g2,x,y[0],stroke,stroke,Color.green,1);
			drawPieces(g2,x,y[1],stroke,dashed1,Color.red,2);
			drawPieces(g2,x,y[2],stroke,dashed2,Color.black,3);
			drawPieces(g2,x,y[3],stroke,stroke,Color.blue,4);
			
			putCaption(g2,"100 mV (dense stack)", 1400., 0.2, Color.blue, 4);
			putCaption(g2,"200 mV (3-module)", 1400., 0.15, Color.green, 1);
			putCaption(g2,"350 mV (3-module)", 1400., 0.10, Color.red, 2);
			putCaption(g2,"500 mV (3-module)", 1400., 0.05, Color.black, 3);
		} else g2.drawString("WELCOME", d.width/2, d.height/2);
	}
	
	private void putCaption(Graphics2D g2, String text, double x, double y, Color color, int symbol) {
		int i0, j0;
		g2.setPaint(color);
		i0 = GetXCoordinate(x);
		j0 = GetYCoordinate(y);
		
		i0 -= rectWidth/2;
		j0 -= rectHeight/2;
		switch (symbol) {
			case 1: g2.draw(new Rectangle2D.Double(i0-10, j0-5, rectWidth, rectHeight));
					break;
			case 2: g2.draw(new Ellipse2D.Double(i0-10, j0-5, rectWidth, rectHeight));
					break;
			case 3: g2.fill(new Rectangle2D.Double(i0-10, j0-5, rectWidth, rectHeight));
					break;
			case 4: g2.fill(new Ellipse2D.Double(i0-10, j0-5, rectWidth, rectHeight));
			default:
		}
		g2.drawString(text, i0, j0);
	}
	
	private void putAxisTitles(Graphics2D g2, BasicStroke stroke, Dimension d, String xTitle, int yoffset, String yTitle, int xoffset) {
		g2.setPaint(Color.black);
		g2.setStroke(stroke);
		g2.rotate(Math.toRadians(-90));
		g2.translate(-d.width/2, d.height/20);
		g2.drawString(xTitle, yoffset, 0);
		
		g2.translate(d.width/2, -d.height/20);
		g2.rotate(Math.toRadians(90));
		g2.drawString(yTitle, d.width/2 + xoffset, d.height*29/30);
	}
	
	private void drawPieces(Graphics2D g2, double[] x, double[] y, BasicStroke stroke, BasicStroke dashed, Color color, int symbol) {
		int i0, j0;
		
		g2.setStroke(stroke);
		GeneralPath brokenLine = new GeneralPath(GeneralPath.WIND_EVEN_ODD, y.length);
		brokenLine.moveTo(GetXCoordinate(x[0]), GetYCoordinate(y[0]));
		if (y.length > 0) {
			g2.setPaint(color);
			for(int j=0;j<y.length;j++) {
				i0 = GetXCoordinate(x[j]);
				j0 = GetYCoordinate(y[j]);
				if (j != 0) brokenLine.lineTo(i0, j0);
				i0 -= rectWidth/2;
				j0 -= rectHeight/2;
				switch (symbol) {
					case 1: g2.draw(new Rectangle2D.Double(i0, j0, rectWidth, rectHeight));
							break;
					case 2: g2.draw(new Ellipse2D.Double(i0, j0, rectWidth, rectHeight));
							break;
					case 3: g2.fill(new Rectangle2D.Double(i0, j0, rectWidth, rectHeight));
							break;
					case 4: g2.fill(new Ellipse2D.Double(i0, j0, rectWidth, rectHeight));
					default:
				}
			}
			g2.setStroke(dashed);
			g2.draw(brokenLine);
		}
	}
	
	FontMetrics pickFont(Graphics2D g2, String longString, int xSpace) {
		boolean fontFits = false;
		Font font = g2.getFont();
		FontMetrics fontMetrics = g2.getFontMetrics();
		int size = font.getSize();
		String name = font.getName();
		int style = font.getStyle();
		
		while (!fontFits) {
			if ((fontMetrics.getHeight() <= maxCharHeight) && (fontMetrics.stringWidth(longString) <= xSpace)) {
				fontFits = true;
			} else {
				if (size <= minFontSize) {
					fontFits = true;
				} else {
					g2.setFont(font = new Font(name, style, --size));
					fontMetrics = g2.getFontMetrics();
				}
			}
		}
		return fontMetrics;
	}
	
	public void DrawXAxis(Graphics2D g2) {
		int x0, x1, yofaxis, yoftick, yText;
		double xTickInterval, dValue;
		int tshift=11;
		
		x0 = (int) (sideborder*right);
		x1 = (int) ((1.0-sideborder)*right);
		if (ymin < 0) yofaxis = (int) ((1.0-topborder)*bottom + (1.0-(2*topborder))*bottom*ymin/(ymax-ymin));
		else yofaxis = (int) ((1.0-topborder)*bottom);
		g2.draw(new Line2D.Double(x0, yofaxis, x1, yofaxis));
		
		xTickInterval = FindTicks(xmin, xmax);
		yoftick = yofaxis + (int) (topborder*bottom/10);
		yText = yofaxis + (int) (topborder*bottom/3);
		dValue = xmin;
		while (dValue <= xmax) {
			x0 = (int) (right*((1-2.*sideborder)*(dValue-xmin)/(xmax - xmin))+right*sideborder);
			g2.draw(new Line2D.Double(x0, yofaxis, x0, yoftick));
			String fs = "0";
			DecimalFormat df = new DecimalFormat(fs);
			String sz = df.format(dValue);
			g2.drawString(sz, x0-tshift, yText);
			dValue += xTickInterval;
		}
	}
	
	public void DrawYAxis(Graphics2D g2) {
		int y0, y1, xofyaxis, xoftick, xText;
		double yTickInterval, dValue;
		
		yTickInterval = FindTicks(ymin, ymax);
		y0 = (int) (topborder*bottom);
		y1 = (int) ((1-topborder)*bottom);
		
		if (xmin < 0) xofyaxis = (int)((1.0-2*sideborder)*right*(-xmin/(xmax-xmin))+sideborder*right);
		else xofyaxis =(int) (sideborder*right);
		
		g2.draw(new Line2D.Double(xofyaxis, y0, xofyaxis, y1));
		xText = 5;
		xoftick = xofyaxis-(int) (sideborder*right/10);
		dValue = ymin;
		
		while (dValue <= ymax) {
			y0 = (int) ((1-topborder)*bottom - (1.0-2*topborder)*bottom*(dValue-ymin)/(ymax-ymin));
			g2.draw(new Line2D.Double(xofyaxis, y0, xoftick, y0));
			String fs = "0.0";
			DecimalFormat df = new DecimalFormat(fs);
			String sz = df.format(dValue);
			g2.drawString(sz, xText+30, y0+3);
			dValue += yTickInterval;
		}
	}
	
	public int GetYCoordinate(double dValue) {
		int y = (int) ((1-topborder)*bottom-(1.0-2*topborder)*bottom*(dValue-ymin)/(ymax-ymin));
		return y;
	}
	
	public int GetXCoordinate(double dValue) {
		int x = (int) (right*((1-2.*sideborder)*(dValue-xmin)/(xmax-xmin))+right*sideborder);
		return x;
	}
	
	public void SetScreenSize(int x, int y) {
		right = x;
		bottom = y;
	}
	
	public void SetPlottingLimits() {
		if (parent.beforePlot == false) {
			if (x.length > 0 && y[0].length > 0) {
				if ((GetXMin() == GetXMax()) || (GetYMin() == GetYMax()));
				else {
					xmin = GetXMin();
					xmax = GetXMax();
					ymin = GetYMin();
					ymax = GetYMax();
				}
			}
		}
	}
	
	public void SetBorderSize(double fraction_of_x, double fraction_of_y) {
		if ((fraction_of_x <= 0) || (fraction_of_y <= 0));
		else {
			topborder = fraction_of_y;
			sideborder = fraction_of_x;
		}
	}
	
	private double FindTicks(double AxisMin, double AxisMax) {
		double fSpan = 0;
		double multiplier = 1;
		double span, fInitialSpan;
		long lSpan, quot, rem;
		span = AxisMax - AxisMin;
		boolean b;
		
		if (AxisMax <= AxisMin) 
			System.out.println("Error in axis data range");
		fInitialSpan = span;
		if (fInitialSpan < 10.0) {
			while (span<10) {
				multiplier *= 10;
				span *= 10;
			}
		} else {
			while (span > 1.0e9) {
				multiplier /= 10;
				span /= 10;
			}
		}
		lSpan = (long) span;
		b = false;
		for (int i=10; i>=2; i--) {
			quot = lSpan/i;
			rem = lSpan - quot*i;
			if (rem == 0) {
				fSpan = (double) quot;
				fSpan = fSpan/multiplier;
				b = true;
			}
			if (b == true) break;
		}
		if (b == false)
		fSpan = (span/(2*multiplier));
		
		return fSpan;
	}
	
	public double GetXMin() {
		double dmin = x[0];
		for (int i=1; i<x.length; i++) if (x[i] < dmin) dmin = x[i];
		xmin = dmin;
		return xmin;
	}
	
	public double GetYMin() {
		double dmin = y[0][0];
		for (int j=0; j<y.length; j++) {
			for (int i=0; i<y[j].length;i++)
				if (y[j][i] < dmin) dmin = y[j][i];
		}
		ymin = dmin;
		return ymin;
	}
	
	public double GetXMax() {
		double dmax = x[0];
		for (int i=1; i<x.length; i++) if (x[i] > dmax) dmax = x[i];
		xmax = dmax;
		return xmax;
	}
	
	public double GetYMax() {
		double dmax = y[0][0];
		for (int j=0; j<y.length; j++) {
			for (int i=0; i<y[j].length; i++)
				if (y[j][i] > dmax) dmax = y[j][i];
		}
		ymax = dmax;
		return ymax;
	}	
}