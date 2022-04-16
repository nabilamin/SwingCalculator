import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class LearningGUI {
	


	public static void main(String[] args) {
		
		// Create listener object
		ButtonHandler listener = new ButtonHandler();

		JButton button1 = new JButton ("CMIS 242");
		// add listener to button 1
		button1.addActionListener(listener);
		JButton button2 = new JButton ("CMIT 202");
		// add listener to button 2
		button2.addActionListener(listener);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,1));
		panel.setBorder(BorderFactory.createRaisedBevelBorder());
		panel.add(button1);
		panel.add(button2);
		
		
		JFrame frame = new JFrame("Assignment Checklist");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(370, 350);
		frame.setLocation(500, 250);
		frame.add(panel);
		
		frame.setVisible(true);
		
		
	}

	public static class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println(e.getActionCommand());
		}
	}
}
