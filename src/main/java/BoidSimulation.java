import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.OptionalDouble;
import java.util.Random;

public class BoidSimulation extends JPanel implements Runnable {


	private static final int DELAY = 10; // in milliseconds
	static int width = 1900;
	static int height = 1000;
	ArrayList<Boid> objectArraylist = new ArrayList<>();
	private boolean running;


	public BoidSimulation() {
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.white);
		setDoubleBuffered(true);
		Random rand = new Random();
		for (int i = 0; i < 50; i++)objectArraylist.add(new Boid(rand.nextInt(width), rand.nextInt(height), 1, 40, 270, rand.nextInt(360), 1));

	}

	@Override
	public void addNotify() {
		super.addNotify();
		Thread simulationThread = new Thread(this);
		running = true;
		simulationThread.start();
	}

	@Override
	public void removeNotify() {
		running = false;
		super.removeNotify();
	}

	@Override
	public void run() {
		//double tvor,tnach;
		while (running) {
			//tvor=System.currentTimeMillis();
			tick();
			repaint();
			//tnach=System.currentTimeMillis();
			//System.out.println("Durchlaufszeit: " + (tnach-tvor)+"ms");
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void tick() {

		;
		//objectArraylist.forEach(Boid::avoidBorder);

		objectArraylist.parallelStream().forEach(o -> {
			ArrayList<Double> x = new ArrayList<>();
			ArrayList<Double> y = new ArrayList<>();

			objectArraylist.forEach(o2 -> {
				double a = o2.getX() - o.getX();
				double c = o2.getY() - o.getY();
				double entfernung = Math.sqrt(a * a + c * c);
				if(entfernung!=0&&entfernung<=o.getSichtweite()){
					x.add(o2.getX());
					y.add(o2.getY());
				}
				if(x.size()!=0) {
					double zielX = (double) x.stream().mapToDouble(z -> z).average().getAsDouble();
					double zielY = y.stream().mapToDouble(z -> z).average().getAsDouble();
					double a2 = zielX - o.getX();
					double c2 = zielX - o.getY();
					double w = Math.toDegrees(Math.atan(a2 / c2));
					if (w > o.getRichtung()) o.setRichtung(o.getRichtung() + 1);
					else if (w < o.getRichtung()) o.setRichtung(o.getRichtung() - 1);
				}

			});
		});

		objectArraylist.parallelStream().forEach(o -> objectArraylist.forEach(o2 -> o2.richtungAngleichen(o)));
		objectArraylist.parallelStream().forEach(o -> objectArraylist.forEach(o2 -> o2.abstandHalten(o)));

		objectArraylist.parallelStream().forEach(Boid::tick);


	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		objectArraylist.parallelStream().forEach(o -> o.render(g2d));


	}


}