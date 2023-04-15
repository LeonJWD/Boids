import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Physics Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new BoidSimulation());
		frame.pack();
		frame.setVisible(true);
	}
}
