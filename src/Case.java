import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;


public class Case extends JComponent{
	int type=0;
	final static int REJOUER = Theme.getNbMaxTheme();
	final static int DEPART = REJOUER+1;
	final static int NBMAXCASES= Case.DEPART+1;
	boolean isSpecial=false; // si la case permet de gagner un camembert
	int selection=0; // si la case peut être selectionner pour un déplacement possible
	Area espace;
	
	public Case(int typeCase, boolean isSpecial)
	{
		super();
		type=typeCase;
		this.isSpecial=isSpecial;
		
	}
	public Case(int typeCase)
	{
		this(typeCase,false);
	}

	public int getType()
	{
		return type;
	}
	
	public void paint (Graphics g)
	{
		Graphics2D g2= (Graphics2D)g;		
		g2.setStroke(new BasicStroke(4));
		g2.setPaint(getColor() );
//		g2.fillRect(1,1,getWidth(),getHeight());
		g2.fill(espace);
		g2.setPaint(Color.BLACK);
		g2.draw(espace);
		if (isSpecial)
		{
			g2.setPaint(Color.RED);
			Ellipse2D.Double e=new Ellipse2D.Double(this.getWidth()/2-this.getWidth()/4,
					this.getHeight()/2-this.getHeight()/4,this.getWidth()/2,this.getHeight()/2);
			g2.fill(e);
			g2.setPaint(Color.BLACK);
			g2.draw(e);
		}
		if (selection>0)
		{
			g2.setPaint(Color.WHITE);
			Ellipse2D.Double e=new Ellipse2D.Double(this.getWidth()/2-this.getWidth()/4,
					this.getHeight()/2-this.getHeight()/4,this.getWidth()/2,this.getHeight()/2);
			g2.fill(e);
			g2.setPaint(Color.BLACK);
			g2.drawString(" "+selection+" ", this.getWidth()/4, this.getHeight()/2);
			
		}
		
	}
	public void setSelection(int selection)
	{
		this.selection=selection;
	}
	public void setEspace(Area a)
	{
		espace=a;
	}
	
	public Color getColor()
	{
		if (type==Case.DEPART ) 
				return Color.CYAN;
		if (type== Case.REJOUER)
				return Color.MAGENTA;
		return (new Theme(type)).getColor();
	}
	
}
