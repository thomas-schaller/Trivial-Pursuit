import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;

import javax.swing.*;
/*

 */

public class Joueur extends JComponent{
  private final int NBTHEMES = 6;
	private boolean themesValides [] = new boolean [NBTHEMES] ;
	private int position = 0;
	private Color vide = Color.black;
	private Color couleur;
	
	Joueur()
	{
		this(Color.red );
	}
	
	Joueur( Color couleur)
	{
		this.couleur=couleur;
		for (int i=0;i<themesValides.length ; i++)
			themesValides[i]=false;
	}
	
	Color getColor()
	{
		return couleur;
	}
	public boolean estToutValide()
	{
		boolean result=true;
		for (int i=0;i<themesValides.length;i++)
			result=result && getStateTheme(i);
		return result;
	}
	public void valideTheme(int theme)
	{
		if ( Theme.isTheme(theme) )
			this.themesValides[theme]=true;
	}
	
	public boolean getStateTheme (int theme)
	{
		return this.themesValides[theme];
	}
	public void setPosition(int caseP)
	{
		this.position= caseP;
	}
	public void deplacer(int valeurDeplacement)
	{
		this.position = (this.position+valeurDeplacement)% Plateau.NB_CASES_ROUES;
	}

	public int getPosition()
	{
		return position;
	}
	
	public Vector choisirPlacement( int deplacementRestant )
	{
	/*
	 * Fonction qui renvoie un vector contenant les entiers représentant les positions possibles
	 *  à partir d'une case de départ et un déplacement à effectuer
	 * */
		Vector result=new Vector();
	if (deplacementRestant ==0)
	{
		result.add(new Integer(position));
		System.out.println("fin");
	}
	else
	{
		if ( (position >= Plateau.NB_CASES_ROUES || position%7 !=0) && position != Plateau.NB_CASES-1 )
		{
			avancerPion(position,deplacementRestant,result);
			avancerPion(position,-deplacementRestant,result);
		}
		else		
			avancerPion(position,deplacementRestant,result);
	}
	return result;
	}
	// fonction récursive qui renvoie les positions ou le pion peut atterrir en avançant sur la roue du plateau
	private static void avancerPion (int positionDep, int casesRestantes,Vector resu)
	{
		if (casesRestantes == 0)
			resu.add(new Integer(positionDep));
		else if (positionDep==Plateau.NB_CASES-1)
					// si on se trouve sur la case centrale
					//on vérifie chaque section centrale
		{
			for (int i=0;i<6;i++)
			avancerPion(46+i*5,-(Math.abs(casesRestantes)-1),resu);
		}
		else if (positionDep <42 && positionDep %7==0) 
				// si on se trouve sur un croisement situé dans la roue du plateau
		{
			//on avance et on recule d'une case sur la roue
			avancerPion((positionDep+1)%Plateau.NB_CASES_ROUES,Math.abs(casesRestantes)-1,resu);
			avancerPion((positionDep+Plateau.NB_CASES_ROUES-1)%Plateau.NB_CASES_ROUES,-(Math.abs(casesRestantes)-1),resu);
			//on entre dans les cases intérieures
			avancerPion(positionDep/7*5+42,Math.abs(casesRestantes)-1,resu);
		}
		else if (positionDep >=Plateau.NB_CASES_ROUES 
				&& ((positionDep-Plateau.NB_CASES_ROUES)%5==4) && casesRestantes>0)
			// si on se trouve sur la plus grande case d'une section intérieure et si on avance
			// on se place sur la case centrale.
			avancerPion(Plateau.NB_CASES-1,casesRestantes-1,resu);
		else if (positionDep >=Plateau.NB_CASES_ROUES  && casesRestantes<0 
				&& ((positionDep-Plateau.NB_CASES_ROUES)%5==0) )
			// si on se trouve sur la plus petite case d'une section intérieure et si on recule
			// on se place sur le croisement de la roue correspondant.
			avancerPion( (positionDep - Plateau.NB_CASES_ROUES)*7/5 ,Math.abs(casesRestantes)-1,resu);
		else if (casesRestantes<0)
				// sinon on continue le mouvement
			avancerPion(positionDep-1,casesRestantes+1,resu); // on continue de reculer
		else
			avancerPion(positionDep+1,casesRestantes-1,resu); // on continue d'avancer
	}
	

		
	
	public String toString()
	{
		return "Le pion de couleur "+couleur+" est à la case "+position;
	}
	
	public void paint (Graphics g)
	{
		Graphics2D g2= (Graphics2D)g;
		Arc2D camembert []= new Arc2D [NBTHEMES];
		for (int i=0;i<NBTHEMES;i++)
		{
			camembert[i]=new Arc2D.Double(1,1,getWidth()-3,getHeight()-3,i*60,60,Arc2D.PIE);
			Theme t= new Theme(i);
			if (themesValides[i])
				g2.setPaint(t.getColor());
			// on colorie la case � la couleur du theme si le joueur a valid� le theme
			else
				g2.setPaint(vide); 
			// on colorie la case � la couleur vide si le joueur n'a pas gagn� le camembert
			g2.fill(camembert[i]);
			g2.setStroke(new BasicStroke(2));
			g2.setPaint(couleur);
			g2.draw( camembert[i] );
		}

			
		
	}
	
}
