import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * This class creates a cell in the grid and defines the size of the sell.
 * @author aleksandar_manev
 */
public class CellPane extends JPanel {
	private static final long serialVersionUID = 1L;
	private int size;
	private JLabel _label;
	public CellPane(int x, int y) {
		_label = new JLabel("");
		_label.setHorizontalAlignment(JLabel.CENTER);
		_label.setVerticalAlignment(JLabel.CENTER);
		this.add(_label);
		size = 50;
		if(x > 10 || y > 6)
			size = 30;
		if(x > 30 || y > 20)
			size = 25;
		if(x > 50 || y > 28)
			size = 15;

	}
	
	public void setLabelText(String text)
	{
		_label.setText(text);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(size, size);
	}
}