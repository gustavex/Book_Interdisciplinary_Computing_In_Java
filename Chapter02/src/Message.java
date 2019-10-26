import java.awt.*;

class Message extends Dialog {
	public Message(Frame parent, String title, String message) {
		super(parent, message, false);
		setBackground(Color.white);
		setLayout(new GridLayout(1,1));
		add(new Label(title, Label.LEFT));
		pack();
		setSize(new Dimension(200,50));
	}
	
	public void show() {
		super.show();
		super.toFront();
	}
}
