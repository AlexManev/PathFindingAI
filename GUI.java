import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
/**
 * Create a GUI visual representation of the search.
 * @author aleksandar_manev
 *
 */
public class GUI {

	public GUI(String fileName) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex) {
		}
		String input = "";
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			input = scanner.nextLine();
			scanner.close();
		} catch (Exception e) {
			System.out.println("Error: Couldn't locate file '" + fileName + "'.");
		}
		String[] NxM = input.substring(1, input.length() - 1).split(",");
		int n = Integer.parseInt(NxM[0]);
		int m = Integer.parseInt(NxM[1]);

		JFrame frame = new JFrame("Robot Navigation (" + NxM[1]+"x"+NxM[0]+")");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(new Pane(n, m));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public List<CellPane> cellPanes = new ArrayList<CellPane>();

	public class Pane extends JPanel {
		private static final long serialVersionUID = 1L;
		/**
		 * Create a grid with size NxM by creating cells and adding them to a list.
		 * @param n
		 * @param m
		 */
		public Pane(int n, int m) {
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			for (int row = 0; row < n; row++) {
				for (int col = 0; col < m; col++) {
					gbc.gridx = col;
					gbc.gridy = row;
					CellPane cp = new CellPane(m,n);
					cellPanes.add(cp);

					Border border = null;
					if (row < 4) {
						if (col < 4) {
							border = new MatteBorder(1, 1, 0, 0, Color.BLACK);
						} else {
							border = new MatteBorder(1, 1, 0, 1, Color.BLACK);
						}
					} else {
						if (col < 4) {
							border = new MatteBorder(1, 1, 1, 0, Color.BLACK);
						} else {
							border = new MatteBorder(1, 1, 1, 1, Color.BLACK);
						}
					}
					
					cp.setBorder(border);
					add(cp, gbc);
					
				}
			}
		}
	}
}