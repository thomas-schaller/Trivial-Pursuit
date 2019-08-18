
import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;
public class De extends JComponent {
private int valeur=0;
private final int MARGEEXT=2; // la marge � l'exterieur du d�.
private final int MARGEINT=5; // la marge � l'interieur du d�.
public De()
{
	this.setPreferredSize(new Dimension(40,40));
}
public void lancerDe()
{
	valeur=(int)(Math.random()*6+1);
}

public int getValue()
{
	return valeur;
}
public String toString()
{
	return "Le de a fait "+valeur+" !";
}

public void paint(Graphics arg0) {
	// TODO Auto-generated method stub
	super.paint(arg0);
	Graphics2D g2 = (Graphics2D)arg0;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	AffineTransform atCentrer = new AffineTransform();
	atCentrer.translate(this.getWidth()/2, 0);
	AffineTransform at =g2.getTransform();
	//g2.transform(atCentrer);
	
	Dimension taille=this.getSize();
	final int TAILLECARRE= (int)Math.min(taille.getWidth(),taille.getHeight())-MARGEEXT*2;
	
	final int DIAMETRE = TAILLECARRE/5;
	g2.drawRoundRect(1, 1, TAILLECARRE, TAILLECARRE,10,10);
	if (valeur%2 == 1) // si le nombre obtenu par le dé est impair, on dessine le point central
	{
		g2.fillArc( (TAILLECARRE-DIAMETRE)/2+MARGEEXT, (TAILLECARRE-DIAMETRE )/2+MARGEEXT, DIAMETRE , DIAMETRE , 1, 360);
		// on dessine le point central du d� (pour les nombres impaires)
	}
	if (valeur>1)
	{
		g2.fillArc(1+MARGEINT, 1+MARGEINT, DIAMETRE, DIAMETRE, 1, 360);
		//on dessine le point en haut � gauche du d�.
		g2.fillArc( TAILLECARRE-DIAMETRE+1-MARGEINT, TAILLECARRE-DIAMETRE+1-MARGEINT, DIAMETRE, DIAMETRE, 1, 360);
		//on dessine le point en bas � droite du d�.
	}
	if (valeur > 3)
	{
		g2.fillArc( MARGEINT+1, TAILLECARRE-DIAMETRE-MARGEINT+1, DIAMETRE, DIAMETRE, 1, 360);
		g2.fillArc( TAILLECARRE-DIAMETRE-MARGEINT+1,MARGEINT+1 , DIAMETRE,DIAMETRE, 1, 360);
	}
	if (valeur > 5)
	{
		g2.fillArc( MARGEINT+1,(TAILLECARRE-DIAMETRE)/2 +MARGEEXT, DIAMETRE, DIAMETRE, 1, 360);
		g2.fillArc( TAILLECARRE-DIAMETRE-MARGEINT+1,(TAILLECARRE-DIAMETRE)/2+MARGEEXT, DIAMETRE,DIAMETRE, 1, 360);			
	}
	g2.setTransform(at);
}
}
