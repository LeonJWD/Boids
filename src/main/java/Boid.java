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
import java.util.Random;

import static java.lang.Math.*;

public class Boid {
	private final double dr = 2;
	private double x, y, v, sichtweite, sichtradius, richtung, xneu, yneu, dx, dy, fWand, abstand;
	private boolean neueRichtungGefunden = false;


	public Boid(double x, double y, double v, double sichtweite, double sichtradius, double richtung, double abstand) {
		this.x = x;
		this.y = y;
		this.v = v;
		this.sichtweite = sichtweite;
		this.sichtradius = sichtradius;
		this.richtung = richtung;
		this.abstand = abstand;
	}

	private double capWinkel(double richtung) {
		if (richtung > 360) richtung = 1;
		if (richtung < 0) richtung = 360;
		return richtung;
	}

	public void tick() {
		//sinus: Gegenkathete durch Hypotenuse
		//kosinus: Ankathete durch Hypotenus
		//
		//
		//
		//
		//
		//
		// von Rand weg
		richtung+= fWand;
		//if(fWand!=0)
		//richtung += fkohaesion * 0.1;
		//if (fkohaesion != 0) System.out.println(fkohaesion);
		Random rand = new Random();
		richtung+= rand.nextDouble(0.1)-0.05;
		if (richtung > 360) richtung = 360 - richtung;
		dx = cos(richtung) * v;
		dy = sin(richtung) * v;
		x += dx;
		y += dy;
		if (x > BoidSimulation.width) x = 0;
		if (x < 0) x = BoidSimulation.width;
		if (y < 0) y = BoidSimulation.height;
		if (y > BoidSimulation.height) y = 0;

	}

	public void avoidBorder() {
		double neueRichtung;
		neueRichtungGefunden = false;
		fWand = 0;
		while (!neueRichtungGefunden) {
			neueRichtung = capWinkel(richtung + fWand);
			dx = Math.cos(neueRichtung) * v;
			dy = Math.sin(neueRichtung) * v;
			xneu = x + dx ;
			yneu = y + dy ;
			if (yneu < 0 && y - sichtweite < 0) {
				if (neueRichtung < 180) {
					fWand += dr;
				} else {
					fWand -= dr;
				}
			}
			if (yneu > BoidSimulation.height && y + sichtweite > BoidSimulation.height - sichtweite) {
				if (neueRichtung < 180) {
					fWand += dr;
				} else {
					richtung -= dr;
				}
			}
			if (xneu > BoidSimulation.width && x + sichtweite > BoidSimulation.width - sichtweite) {
				if (neueRichtung >= 90) {
					fWand -= dr;
				} else {
					fWand += dr;
				}
			}
			if (xneu < 0 && x - sichtweite < 0) {
				if (neueRichtung >= 90) {
					fWand += dr;
				} else {
					fWand -= dr;
				}
			}
			neueRichtungGefunden = true;
		}
	}



	public void richtungAngleichen(Boid b) {

		if (richtung != b.getRichtung()) {
			double a = b.getX() - x;
			double c = b.getY() - y;
			double entfernung = sqrt(a * a + c * c);

			if (entfernung <= sichtweite && entfernung != 0) {
				double w= toDegrees(atan(a/c));
				//double dw= (richtung-w);
				//if(Math.abs(dw)<=sichtradius/2) {
					double r1 = 0.5 * richtung + 0.5 * b.getRichtung();
					if (r1 > 360) r1 = 360 - r1;
					richtung = r1;
				//	System.out.println(dw);
				//}
			}
		}


	}
	public void abstandHalten(Boid b){
		double a = b.getX() - x;
		double c = b.getY() - y;
		double entfernung = sqrt(a * a + c * c);
		if(entfernung!=0&&entfernung<abstand){
			double w= toDegrees(atan(a/c));
			double dw= abs(richtung-w);
			if(dw-sichtradius/2<sichtradius/2)	if(dw>180)richtung+=1/(entfernung*2); else richtung-=1/(entfernung*entfernung);


		}


	}

	public void render(Graphics2D g2d) {

		int line_x, line_y;
		line_x = (int) (x + cos(richtung) * v*5);
		line_y = (int) (y + sin(richtung) * v*5);
		g2d.setColor(Color.BLUE);
		g2d.drawLine((int) x, (int) y, line_x, line_y);
		g2d.fillRect((int) (x - 5), (int) (y - 5), 10, 10);
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

	public double getV() {
		return v;
	}

	public void setV(double v) {
		this.v = v;
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

	public double getRichtung() {
		return richtung;
	}

	public void setRichtung(double richtung) {
		this.richtung = richtung;
	}
}
