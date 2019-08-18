import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;


public class TrivialPursuit extends JFrame {
//penser Ã  l'affichage d'une fenetre pour les questions
	JButton jbLancerDe;
	De de=new De();
	JRadioButton choixPositions [];
	Plateau p;
	JPanel afficheurPosition=new JPanel();
	JPanel afficheurJeu=new JPanel(new FlowLayout() );
	JPanel choixPosition =new JPanel();
	JPanel jpDirection = new JPanel();
	JPanel jpAfficheurQuestion = new JPanel();
	JLabel jlQuestion= new JLabel();
	JRadioButton choixQuestions [];
	Theme themes  []= Theme.getAllThemes();
	
	public TrivialPursuit(int nbJoueurs)
	{
		super("Trivial Pursuit");		
		p=new Plateau(nbJoueurs);	
		// affichage du dé
		jbLancerDe= new JButton ("Lancer le de");
		jbLancerDe.addActionListener(new LancementDe(this));
		JPanel resultatDe = new JPanel(new FlowLayout() );
		resultatDe.add(jbLancerDe);
		resultatDe.add(de);
		afficheurPosition.add(resultatDe,BorderLayout.NORTH);
		jpAfficheurQuestion.add(jlQuestion,BorderLayout.NORTH);
		
		this.getContentPane().add(afficheurPosition, BorderLayout.NORTH);
		this.getContentPane().add(p, BorderLayout.CENTER);
		this.getContentPane().add(jpAfficheurQuestion, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}
	
	public void afficherListeChoix(Vector vals)
	{
		JLabel position = new JLabel("Position :");
		jpDirection.add(position,BorderLayout.NORTH);
		afficheurPosition.add(jpDirection,BorderLayout.SOUTH);
		choixPositions  = new JRadioButton [vals.size()];
		ButtonGroup bgPositions=new ButtonGroup();
		JPanel jpchoixDirection = new JPanel(new FlowLayout() );
		EffectuerChoix ec=new EffectuerChoix(this);
		for (int i=0;i<vals.size();i++)
		{
			choixPositions[i]= new JRadioButton(""+(i+1));
			choixPositions[i].setName(""+vals.get(i));
			choixPositions[i].addActionListener(ec);
			bgPositions.add(choixPositions[i]);
			jpchoixDirection.add(choixPositions[i]);
		}
		jpDirection.add(jpchoixDirection,BorderLayout.CENTER);
		jpDirection.updateUI();
	}
	
	public void effacerListeChoix()
	{
		jpDirection.removeAll();
		
	}
	
	public void gestionCase(int position)
	{
		Case c= p.getCase(position);
		int typeCase=c.getType();
		if (  typeCase == Case.REJOUER) // si on tombe sur une case où l'on doit rejouer:
		{
			deALancer();// on peut relancer le dé.
		}
		else if (  typeCase == Case.DEPART) // si on tombe sur la case centrale
		{
			int random= (int)(Math.random()*themes.length);
			Question q = themes[random].getQuestion();
			afficherQuestion(q, position);
		}
		else
		{

			Question q = themes[typeCase].getQuestion();
			afficherQuestion(q, position);
		}
	}
	
	public void afficherQuestion(Question q, int posCase)
	{
		Case c= p.getCase(posCase);
		int typeCase=c.getType();
		jlQuestion.setText(Theme.getName(typeCase)+": "+ q.getLibelleQuestion());
		choixQuestions = new JRadioButton [q.getNbPropositions()];
		ButtonGroup bgQuestions=new ButtonGroup();
		RepondreQuestion rq = new RepondreQuestion(this,q,posCase);
		for (int i=0;i<choixQuestions.length;i++)
		{
			choixQuestions[i]=new JRadioButton (q.getProposition(i+1));
			choixQuestions[i].setName(""+i);
			bgQuestions.add(choixQuestions[i]);
			choixQuestions[i].addActionListener(rq);
			jpAfficheurQuestion.add(choixQuestions[i],BorderLayout.SOUTH);
		}
		jpAfficheurQuestion.updateUI();
	}
	public void repondreQuestion( int posCase, boolean donneBonneReponse)
	{

		
		effacerQuestion();
		
		if (donneBonneReponse )
		{
			Case c= p.getCase(posCase);
			if ( c.getType() != Case.DEPART && p.getJoueur().estToutValide() )
			{
				jlQuestion.setText("Tu as Gagné");
			}
			else if ( c.isSpecial )
			{
				Joueur j= p.getJoueur();
				j.valideTheme(c.getType());
			}
			jlQuestion.setText("La réponse est correcte");
			deALancer();
			
		}
		else
		{
			jlQuestion.setText("La réponse est incorrecte");
			finirTour();
		}
		
	}
	
	public void effacerQuestion()
	{
		jpAfficheurQuestion.removeAll();
		jpAfficheurQuestion.add(jlQuestion,BorderLayout.NORTH);
	}
	
	public static void main (String [] args)
	{
		TrivialPursuit tp= new TrivialPursuit(1);
		tp.setVisible(true);
		tp.setSize(600,600);
	}
	
	public void deALancer()
	{
		jbLancerDe.setEnabled(true);
		p.viderChoix();
		repaint();
	}

	void lancerDe()
	{
		de.lancerDe();
		de.repaint();
		Vector positionsPossibles=(p.getJoueur()).choisirPlacement( de.getValue());
		for (int i=0;i< positionsPossibles.size();i++)
		{
			System.out.print(positionsPossibles.get(i)+" ");
			Integer temp=(Integer) positionsPossibles.get(i);
			Case c= p.getCase(temp.intValue());
			c.setSelection(i+1);
		}
		System.out.println(" fin !");
		afficherListeChoix(positionsPossibles);
		jbLancerDe.setEnabled(false);
		p.repaint();
	}
	void directionChoisie(int pos)
	{
		p.getJoueur().setPosition(pos);
		effacerListeChoix();
		gestionCase(pos);
	}
	
	void finirTour()
	{
		p.nouveauTour();
		deALancer();
	}
}
 class EffectuerChoix implements ActionListener
	{
		TrivialPursuit parent;
		EffectuerChoix(TrivialPursuit tp)
		{
			parent=tp;
		}
		public void actionPerformed( ActionEvent ae)
		{
			int position = Integer.parseInt(((JRadioButton)ae.getSource()).getName() );
			this.parent.directionChoisie(position);

		}
		
	}
 class RepondreQuestion implements ActionListener
	{
		TrivialPursuit parent;
		int posCase;
		Question question;
		RepondreQuestion (TrivialPursuit tp,Question q,int posCase)
		{
			parent=tp;
			this.posCase=posCase;
			question=q;
		}
		public void actionPerformed( ActionEvent ae)
		{
			int position = Integer.parseInt(((JRadioButton)ae.getSource()).getName() );
			this.parent.repondreQuestion(posCase, question.isCorrect(position+1));
		}
		
	}
class LancementDe implements ActionListener
	{
		TrivialPursuit parent;
		LancementDe(TrivialPursuit tp)
		{
			this.parent=tp;
		}
		public void actionPerformed( ActionEvent ae)
		{
			parent.lancerDe();
		}
	}