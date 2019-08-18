import java.util.*;
import java.io.*;
/**
 * @author thomas
 *
 */
public class Quizz {
	protected Vector questions;

	public Quizz ()
	{
		questions=new Vector();
	}

	public static Quizz lireFichier( String nomFichier )
	{// lit un fichier pour construire un Quizz
		Quizz resultat=new Quizz();
			BufferedReader bf=null;
		Question question = null;
		try
		{
			bf = new BufferedReader(new FileReader(nomFichier));
		}
		catch (Exception e)
		{
			System.out.println(e);
			System.exit(0);
		}
		while (( question = Question.lireBuffer(bf)) != null )
			resultat.addQuestion(question);
		try
		{
			bf.close();
		}
		catch (Exception e)
		{
			System.out.println(e);
			System.exit(0);
		}
		return resultat;
	}

	public boolean isEmpty() // indique si il y a des questions dans l'exercice
	{
		return  questions.isEmpty();
	}


// void sauverFichier( Exercice , char * ); //enregistre un Quizz dans un fichier

	public int getNbQuestions ( ) //renvoie le nombre de questions du Quizz
	{
		return questions.size();
	}
	Question getQuestion()
	{
		return getQuestion ( (int)(1 +Math.random()*getNbQuestions() ));
	}
	
	Question getQuestion(int position)  //renvoie la ieme question du QUizz
	{
		return (Question) questions.get(position-1);
	}
	
	public int getnbReponses () //renvoie le nombre de réponses données pour le Quizz
	{
		int nbReponses=0;
		Question temp;
		for (int i=1;i<=this.getNbQuestions();i++)
		{
			temp= getQuestion(i);
			nbReponses=temp.getNbEssais()+nbReponses;
		}
		return nbReponses;
	}
	
	public int getnbReponsesCorrectes () //renvoie le nombre de questions répondues justes
	{
		int nbReponsesJustes=0;
		Question temp;
		for (int i=1;i<=this.getNbQuestions();i++)
		{
			temp= getQuestion(i);
			if ( temp.isCorrect() )
				nbReponsesJustes++; 
		}
		return nbReponsesJustes;
	}

	public void addQuestion (Question q) // ajoute une question au Quizz
	{
		this.questions.add(q);
	}

	public void repondreQuestion(int posQuestion,int posReponse) // propose la j-eme proposition comme réponse à la i-ieme question
	{
		Question temp=getQuestion(posQuestion);
		boolean resultat= temp.isCorrect(posReponse);
		temp.donneBonneReponse(resultat);
		System.out.println("La réponse était :" + temp.getReponse() );
	}
	public void repondreQuestion(int posQuestion) // interroge l'utilisateur sur la i-ieme question
	{
		Question temp = getQuestion(posQuestion);
		System.out.print((char)('A'+posQuestion-1) +") ");
		System.out.println(temp);
		System.out.print("Votre Choix ? ");
		repondreQuestion(posQuestion,Lecture.lireInt());
		
	}
	void repondre() // interroge sur toutes les questions de l'exercice
	{
		for (int i=1;i<=getNbQuestions();i++)
		{
			repondreQuestion(i);
		}
	}
}