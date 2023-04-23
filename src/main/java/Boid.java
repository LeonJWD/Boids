/*
Eigenschaften:
Sichtweite
Sichtradius
Geschwindigkeit
Richtung

Regeln:
von anderen in Sichtweite entfernen
Richtung mit anderen in Sichtweite anpassen
zum Zentrum aller nahen Boids steuern
 */


import java.awt.*;

import static java.lang.Math.sqrt;

public class Boid {
	private final double dr = 2;
	private double x, y, vx, vy, sichtweite, sichtradius,protectedRange;
	private final boolean neueRichtungGefunden = false;

	public Boid(double x, double y, double vx, double vy, double sichtweite, double sichtradius, double protectedRange) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.sichtweite = sichtweite;
		this.sichtradius = sichtradius;
		this.protectedRange = protectedRange;
	}

	public double getProtectedRange() {
		return protectedRange;
	}

	public void setProtectedRange(double protectedRange) {
		this.protectedRange = protectedRange;
	}

	public void tick() {
		x += vx;
		y += vy;

	}


	public void render(Graphics2D g2d) {

		int line_x, line_y;

		line_x = (int) (x + (vx));
		line_y = (int) (y + (vy));
		g2d.setColor(Color.BLUE);

		g2d.drawLine((int) x, (int) y, line_x, line_y);
		g2d.fillRect((int) (x - 1), (int) (y - 1), 2, 2);
		//g2d.drawOval((int) (x - sichtweite), (int) (y - sichtweite), (int) sichtweite * 2, (int) sichtweite * 2);
		//g2d.setColor(Color.red);
		//g2d.drawOval((int) (x - abstand), (int) (y - abstand), (int) abstand * 2, (int) abstand * 2);

	}


	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getVx() {
		return vx;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	public double getSichtweite() {
		return sichtweite;
	}

	public void setSichtweite(double sichtweite) {
		this.sichtweite = sichtweite;
	}

	public double getSichtradius() {
		return sichtradius;
	}

	public void setSichtradius(double sichtradius) {
		this.sichtradius = sichtradius;
	}
}
