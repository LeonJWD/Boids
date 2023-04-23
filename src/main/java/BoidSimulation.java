import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.sqrt;

public class BoidSimulation extends JPanel implements Runnable {


	private static final int DELAY = 40; // in milliseconds
	static int width = 1200;
	static int height = 1000;
	ArrayList<Boid> objectArraylist = new ArrayList<>();
	double avoidfactor = 0.03;
	double matchfactor = 0.01;
	double centeringfactor = 0.001;
	double maxspeed = 5;
	double minspeed = 3;
	double turnfactor = 0.5;
	double leftmargin = 300;
	double topmargin = leftmargin;
	double bottommargin = height - 2 * topmargin;
	double rightmargin = width - 2 * leftmargin;
	private boolean running;

	public BoidSimulation() {
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.white);
		setDoubleBuffered(true);
		Random rand = new Random();
		for (int i = 0; i < 1000; i++)
			objectArraylist.add(new Boid(rand.nextInt(width), rand.nextInt(height), maxspeed, maxspeed, 30, 360, 5));

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

		objectArraylist.forEach(b -> {
			AtomicReference<Double> xvelAverage = new AtomicReference<>((double) 0);
			AtomicReference<Double> yvelAverage = new AtomicReference<>((double) 0);
			AtomicReference<Double> close_dx = new AtomicReference<>((double) 0);
			AtomicReference<Double> close_dy = new AtomicReference<>((double) 0);
			AtomicReference<Double> neigbours = new AtomicReference<>((double) 0);
			AtomicReference<Double> xposAverage = new AtomicReference<>((double) 0);
			AtomicReference<Double> yposAverage = new AtomicReference<>((double) 0);

			objectArraylist.forEach(b2 -> {
				double dx = b.getX() - b2.getX();
				double dy = b.getY() - b2.getY();
				double distance = sqrt(dx * dx + dy * dy);

				if (distance <= b.getSichtweite()) {

					if (distance <= b.getProtectedRange()) {
						close_dx.updateAndGet(v -> ((v + b.getX() - b2.getX())));
						close_dy.updateAndGet(v -> ((v + b.getY() - b2.getY())));
					}
					xvelAverage.updateAndGet(v -> ((v + b2.getVx())));
					yvelAverage.updateAndGet(v -> ((v + b2.getVy())));
					neigbours.getAndSet(((neigbours.get() + 1)));
					xposAverage.updateAndGet(v -> ((v + b2.getX())));
					yposAverage.updateAndGet(v -> ((v + b2.getY())));
				}
			});
			b.setVx(b.getVx() + close_dx.get() * avoidfactor);
			b.setVy(b.getVy() + close_dy.get() * avoidfactor);
			if (neigbours.get() > 0) {
				xvelAverage.updateAndGet(v -> ((v / neigbours.get())));
				yvelAverage.updateAndGet(v -> ((v / neigbours.get())));
				xposAverage.updateAndGet(v -> ((v / neigbours.get())));
				yvelAverage.updateAndGet(v -> ((v / neigbours.get())));
				b.setVx(b.getVx() + (xvelAverage.get() - b.getVx()) * matchfactor);
				b.setVy(b.getVy() + (yvelAverage.get() - b.getVy()) * matchfactor);
				b.setVx(b.getVx() + (xposAverage.get() - b.getX()) * centeringfactor);
				b.setVy(b.getVy() + (xposAverage.get() - b.getY()) * centeringfactor);
			}

			if (b.getX() < leftmargin) {
				b.setVx(b.getVx() + turnfactor);
			} else if (b.getX() > rightmargin) {
				b.setVx(b.getVx() - turnfactor);
			}
			if (b.getY() > bottommargin) {
				b.setVy(b.getVy() - turnfactor);
			} else if (b.getY() < topmargin) {
				b.setVy(b.getVy() + turnfactor);
			}


			//check the Speed
			double speed = sqrt(b.getVx() * b.getVx() + b.getVy() + b.getVy());
			if (speed > maxspeed) {
				b.setVx((b.getVx() / speed) * maxspeed);
				b.setVy((b.getVy() / speed) * maxspeed);
			}
			if (speed < minspeed) {
				b.setVx((b.getVx() / speed) * minspeed);
				b.setVy((b.getVy() / speed) * minspeed);

			}



		});
		objectArraylist.parallelStream().forEach(Boid::tick);


	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawRect((int) leftmargin, (int) topmargin, (int) rightmargin, (int) bottommargin);
		objectArraylist.parallelStream().forEach(o -> o.render(g2d));


	}


}