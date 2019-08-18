import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;

import javax.swing.*;

import sun.java2d.loops.FillPath;


public class Plateau extends JComponent {
	private Case cases[];
	private double tailleCaseH;
	private double tailleCaseW;
	private double taille;
	static final int NB_CASES_ROUES=42;
	static final int NB_CASES_CENTRALES=31;
	static final int  NB_CASES=NB_CASES_CENTRALES+NB_CASES_ROUES;
	private Color couleurBordure=Color.BLACK;
	private Stroke epaisseurTrait=new BasicStroke(2);
	private Joueur [] joueurs;
	private int tourJoueur=0;
	private int origine=5;
	
	public Joueur getJoueur()
	{
		System.out.println(tourJoueur);
		return joueurs[tourJoueur];
	}
	public void viderChoix()
	{
		for (int i=0;i<NB_CASES;i++)
		{
			cases[i].setSelection(0);
		}	
	}
	public void nouveauTour()
	{
	
		tourJoueur = (tourJoueur+1)%joueurs.length;
	}
	private void creerJoueurs()
	{
		for (int i=0;i<joueurs.length;i++)
			joueurs[i]=new Joueur (Color.GREEN);
	}
	public Plateau (int nbJoueurs)
	{
		// on pr�pare les joueurs
		joueurs =new Joueur[nbJoueurs];
		creerJoueurs();
		
		cases =new Case[NB_CASES];
		for (int i=0;i<Theme.getNbMaxTheme();i++)
			// on s'occupe des cases se trouvant sur le cercle du plateau
		{
			cases[i*7]= new Case(i,true );
			cases[1+i*7]=new Case ((i+3)%Theme.getNbMaxTheme());
			cases[2+i*7]=new Case (Case.REJOUER);
			cases[3+i*7]=new Case ((i+2)%Theme.getNbMaxTheme());
			cases[4+i*7]=new Case ((i+5)%Theme.getNbMaxTheme());
			cases[5+i*7]=new Case (Case.REJOUER,false);
			cases[6+i*7]=new Case ((i+4)%Theme.getNbMaxTheme());
		}

		
		int nb_cases_ligne=(NB_CASES_CENTRALES-1)/Theme.getNbMaxTheme();
		for (int i=0;i<Theme.getNbMaxTheme();i++)
		{
				cases[42+i*5]= new Case((45+i*5)%Theme.getNbMaxTheme());	
				cases[43+i*5]= new Case((45+i*5+5)%Theme.getNbMaxTheme());
				cases[44+i*5]= new Case((45+i*5+1)%Theme.getNbMaxTheme());
				cases[45+i*5]= new Case((45+i*5+2)%Theme.getNbMaxTheme());
				cases[46+i*5]= new Case((45+i*5+4)%Theme.getNbMaxTheme());
	
			
		}
		cases[cases.length-1] =new Case(Case.DEPART);
		this.setPreferredSize(new Dimension(400,400));
	}
	
	public Case getCase (int pos)
	{
		return cases[pos];
	}
	
	private void dimensionnerCasesInterieure()
	{		 
		 // le point d'origine pour le placement des cases
		double angles [] ={
				29*Math.PI/30,	14*Math.PI/15,	15/15*Math.PI,
				19*Math.PI/30,	9*Math.PI/15,	2*Math.PI/3,
				9*Math.PI/30,	4*Math.PI/15,	Math.PI/3,
				59*Math.PI/30,	29*Math.PI/15,	2*Math.PI,
				49*Math.PI/30,	24*Math.PI/15,	5*Math.PI/3,
				39*Math.PI/30,	19*Math.PI/15,	4*Math.PI/3
				
				
				
				
		};
		GeneralPath caseCentrale = new GeneralPath();
		/* les angles sont stockés tel quel :
		à i%3: on a l'angle de dérivation de la droite
		à (i+1)%3: l'angle d'origine pour le point en haut à gauche
		à (i+2)%3:l'angle d'origine pour le point en haut à droite
		*/
		//on dimensionne les cases intérieures:
		for (int i=0;i<6;i++)
		{
			int j=0;
			Point2D coin=null;
			Point2D temp=null;
			for (j=0;j<5;j++)
			{
				int caseCourante=42+j+(i*5);
				GeneralPath caseC = new GeneralPath();
				
				temp= calculerCoordCasesInterieur(angles[1+3*i], angles[3*i], j);
				caseC.moveTo(temp.getX(), temp.getY());
				
				temp=calculerCoordCasesInterieur(angles[2+3*i], angles[3*i], j);
				caseC.lineTo(temp.getX(), temp.getY());
				
				coin=calculerCoordCasesInterieur(angles[2+3*i], angles[3*i], j+1);
				caseC.lineTo(coin.getX(), coin.getY());
				
				temp=calculerCoordCasesInterieur(angles[1+3*i], angles[3*i], j+1);
				caseC.lineTo(temp.getX(), temp.getY());
				
				caseC.closePath();
				Area a= new Area(caseC);
				cases[caseCourante].setBounds(a.getBounds());
				a.transform(
						AffineTransform.getTranslateInstance( (double)(-cases[caseCourante].getX()),(double) -cases[caseCourante].getY() )
						);
				cases[caseCourante].setEspace(a);
			}
			if (i==0)
				caseCentrale.moveTo(temp.getX(), temp.getY());
			else
				caseCentrale.lineTo(temp.getX(), temp.getY());
			caseCentrale.lineTo(coin.getX(), coin.getY());
			
		}
		
		caseCentrale.closePath();
		Area a = new Area (caseCentrale);
		cases[72].setBounds(a.getBounds());
		a.transform(
				AffineTransform.getTranslateInstance( (double)(-cases[72].getX()),(double) -cases[72].getY() )
				);
		cases[72].setEspace(a);
		
	}
	
	private void dimensionnerCasesRoue()
	{
		// on trace les cases contenues dans le cercle.
		//on remplie les angles d'un disque selon les couleurs des cases.
		// Ayant 42 cases dans la bordure du disque, une case est s�par� de 6/45�
		
		Area espaceCase;
		Area espaceCase2=new Area(
				new Ellipse2D.Double(origine+tailleCaseH, origine+tailleCaseH, 
						taille-tailleCaseH*2, taille-tailleCaseH*2)
				);
		for (int i=0;i<42;i++)
		{
			
			Arc2D.Double arcCase;
			if ( i%7 == 0 )
			{
				arcCase= new Arc2D.Double(origine, origine, taille, taille, i*8+i/7*4,12,Arc2D.PIE );
			}
			else
			{
				arcCase= new Arc2D.Double(origine, origine, taille, taille, i*8+i/7*4+4, 8,Arc2D.PIE);
			}
			espaceCase=new Area (arcCase);
			
			espaceCase.subtract(espaceCase2);
			cases[i].setBounds(espaceCase.getBounds());
			espaceCase.transform(AffineTransform.getTranslateInstance(-cases[i].getX(),-cases[i].getY()));
			cases[i].setEspace(espaceCase);
			
			
		
		}
	}
	
	// fonction redimensionnant la taille des pions des joueurs 
	private void dimensionnerPion(Graphics2D g2)
	{
		
		for (int i=0;i<joueurs.length;i++)
		{
		Case c= cases[this.joueurs[i].getPosition()];
		joueurs[i].setSize( (int)(2*tailleCaseH/3), (int)( 2*tailleCaseH/3));
		joueurs[i].setLocation((int)(c.getX()+tailleCaseH/3),(int)(c.getY()+tailleCaseH/3 ));
		this.joueurs[i].paint(g2.create(this.joueurs[i].getX(), this.joueurs[i].getY(), this.joueurs[i].getWidth(), this.joueurs[i].getHeight() ) );
		}
		
	}
	
	//fonction renvoyant un point correspondant à la position d'un point d'une ième case 
	// selon le point d'origine et l'angle de la droite
	public Point2D calculerCoordCasesInterieur(double angleOrigine,double angleDroite, int posCase)
	{
		double rayon=taille/2-tailleCaseH;
		final double CONSTANT = origine+tailleCaseH+rayon;
		return new Point2D.Double (CONSTANT-rayon*Math.cos(angleOrigine)+Math.cos(angleDroite)*tailleCaseH*posCase,
				CONSTANT-rayon*Math.sin(angleOrigine)+Math.sin(angleDroite)*tailleCaseH*posCase);
	}
/*	origine+tailleCaseH+rayon-rayon*Math.cos(4*Math.PI/15)+Math.cos(9*Math.PI/30)*tailleCaseH*i
	origine+tailleCaseH+rayon-rayon*Math.sin(4*Math.PI/15)+Math.sin(9*Math.PI/30)*tailleCaseH*i
	*/
	public void paint (Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		taille=Math.min(this.getSize().getHeight(),this.getSize().getWidth())-origine*2;
		this.setSize((int)(taille+origine*2),(int)(taille+origine*2));
		tailleCaseW = (int)(Math.PI*(taille-tailleCaseW*2)/30);
		tailleCaseH = taille/13;
		dimensionnerCasesRoue();
		dimensionnerCasesInterieure();
		for (int i=0;i< 73;i++)
		{
			this.cases[i].paint(
				g2.create(cases[i].getX(),cases[i].getY(), cases[i].getWidth(), cases[i].getHeight() ));
		}
		dimensionnerPion(g2);
		
		/*
		 //lignes de rep�re pour les cases int�rieures 
		
		g.setColor(Color.RED);
		// ligne horizontale
		g2.draw(new Line2D.Double(origine+tailleCaseH+rayon-rayon*Math.cos(Math.PI),origine+tailleCaseH+rayon-rayon*Math.sin(Math.PI),origine+tailleCaseH+rayon-rayon*Math.cos(29*Math.PI/15),origine+tailleCaseH+rayon-rayon*Math.sin(29*Math.PI/15)));
		g2.draw(new Line2D.Double(origine+tailleCaseH+rayon-rayon*Math.cos(14*Math.PI/15),origine+tailleCaseH+rayon-rayon*Math.sin(14*Math.PI/15), origine+tailleCaseH+rayon-rayon*Math.cos(2*Math.PI),origine+tailleCaseH+rayon-rayon*Math.sin(2*Math.PI)));
		
		//ligne de haut droite a bas gauche
		g2.draw(new Line2D.Double(origine+tailleCaseH+rayon-rayon*Math.cos(3*Math.PI/5),origine+tailleCaseH+rayon-rayon*Math.sin(3*Math.PI/5), origine+tailleCaseH+(int)(rayon-rayon*Math.cos(5*Math.PI/3)),origine+tailleCaseH+(int)(rayon-rayon*Math.sin(5*Math.PI/3))));	
		g2.draw(new Line2D.Double(origine+tailleCaseH+rayon-rayon*Math.cos(2*Math.PI/3),origine+tailleCaseH+rayon-rayon*Math.sin(2*Math.PI/3),origine+tailleCaseH+(int)(rayon-rayon*Math.cos(24*Math.PI/15)),origine+tailleCaseH+rayon-rayon*Math.sin(24*Math.PI/15)));
		
		//ligne de haut gauche a bas droite
		g2.draw(new Line2D.Double(origine+tailleCaseH+rayon-rayon*Math.cos(4*Math.PI/15),origine+tailleCaseH+rayon-rayon*Math.sin(4*Math.PI/15),origine+tailleCaseH+(int)(rayon-rayon*Math.cos(4*Math.PI/3)),origine+tailleCaseH+rayon-rayon*Math.cos(5*Math.PI/6)));
		g2.draw(new Line2D.Double(origine+tailleCaseH+rayon-rayon*Math.cos(Math.PI/3),origine+tailleCaseH+rayon-rayon*Math.cos(Math.PI/6), origine+tailleCaseH+(int)(rayon-rayon*Math.cos(19*Math.PI/15)),origine+tailleCaseH+rayon-rayon*Math.sin(19*Math.PI/15)));
		*/
		
		


	}
		
	}
