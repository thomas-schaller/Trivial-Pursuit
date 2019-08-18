import java.io.*;
public class Question {
	/*
	 * Classe permettant de modéliser les questions à choix multiples avec une seule bonne réponse possible
	 */
	protected String libelle;
	protected String[] propositions;
	protected int positionReponse;
	protected int etatReponse=0;
	
	Question (String libelleQuestion, String[] propositions, int positionRep )
	{
		this.libelle=libelleQuestion;
		this.propositions = propositions;
		this.positionReponse = positionRep-1;
	}
	public static Question lireBuffer(BufferedReader bf)
	{
		Question q=null;
		String ligne="";
		String texteQuestion="";
		int posReponse=0;
		int nbQuestions=0;
		int i=0;
		boolean aTrouve=false;
		String propositions [];
		try
		{
			ligne=bf.readLine();
		}
		catch (Exception e)
		{
			return q;
		}
		if (ligne == null)
			return q;
		while (i<ligne.length() && ! aTrouve) // gestion de la premiere ligne contenant la position de la réponse et le nombre de questions
		{
			if (ligne.charAt(i)==' ')
			{
				nbQuestions=Integer.parseInt( ligne.substring(0,i) );
				posReponse=Integer.parseInt( ligne.substring( i+1,ligne.length() ) );
				aTrouve=true;
			}	
			i++;
		}
		try
		{
			texteQuestion=bf.readLine();
			propositions = new String [nbQuestions];
			for (i=0;i<nbQuestions;i++)
			{
				propositions[i]=bf.readLine();
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
			return q;
		}
		q=new Question (texteQuestion,propositions,posReponse); 
		return q;
	}
	
	public String getLibelleQuestion()//	 renvoie le libelle d'une question
	{
		return this.libelle;
	}
	
	public boolean aRepondu()
	{
		return etatReponse != 0;
	}
	
	public int getNbEssais()
	{
		return Math.abs(etatReponse);
	}
	public boolean isCorrect() // renvoie vrai si la question a été répondu et si elle fut répondu juste
	{
		return etatReponse>0; 
	}
	public void donneBonneReponse(boolean aBienRepondu)
	{
		etatReponse=Math.abs(etatReponse)+1;
		if (! aBienRepondu)
			etatReponse=-etatReponse;
	}
	
	public int getNbPropositions()
//	renvoie le nombre de propositions pour la question, 0 si il n'en a pas
	{
		return this.propositions.length;
	}
	public String getProposition(int position)
	// renvoie la i-eme proposition  pour la question, 
	{
		if (position >1 && position <= this.getNbPropositions())
			return propositions[position-1];
		else
			return propositions[0];
	}
	public String getReponse()//renvoie la réponse à la question
	{
		return propositions[positionReponse];
	}
	public boolean isCorrect(String supposition)
//	 compare la réponse donnée en argument sous forme de chaine de caractères à la bonne réponse. Vrai si juste, faux sinon.
	{
		return supposition.equals(this.getReponse());
	}

	public boolean isCorrect(int positionRep)
	//compare la réponse donnée en argument exprimée sous la forme d'un entier
	{
		return this.isCorrect(this.getProposition(positionRep));
	}

	public String toString()//		 affiche la question et les propositions sous la forme 1) 1ere propo 2) 2eme propo...
	{
		 String resultat=this.getLibelleQuestion();
		 if ( resultat.charAt( resultat.length()-1 ) == '?' )
		  resultat += '\n';
		 else
			 resultat += " ?\n";
		 for (int i=1;i<= this.getNbPropositions();i++)
		 {
		  resultat += "\t"+i+". "+this.getProposition(i)+'\n';
		 }
		 return resultat;
	}
}
