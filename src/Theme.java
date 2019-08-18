import java.awt.*;


public class Theme {
	/*
	 * BLEU PLANÈTE TERRE
	 * ROSE DIVERTISSEMENTS
	 * JAUNE IL ÉTAIT UNE FOIS
	 * MARRON ARTS ET LITTÉRATURE
	 * VERT VIE ET NATURE
	 * ORANGE SPORTS ET LOISIRS
	 */
	public static final int TERRE=0;
	public static final int HISTOIRE=1;
	public static final int SPORT=2;
	public static final int DIVERTISSEMENTS=3;
	public static final int LETTRES=4;
	public static final int VIE=5;
	public static final int MIN=TERRE;
	public static final int MAX=VIE;
	private static final String [] nomThemes = {"Planète Terre","Il était une fois","Sports et Loisirs","Divertissements","Arts et Littératures","Vie et Nature"};
	private final String [] fichierThemes = {"Terre","Histoire","Sport","Divertissement","Lettres","Vie"};
	private Quizz quizz;
	private int theme;
	
	public static Theme []  getAllThemes()
	{
		Theme [] resultat= new Theme [Theme.getNbMaxTheme()];
		for (int i=0;i<resultat.length;i++)
		{
			resultat[i]=new Theme(i);
		}
		return resultat;
	}
	public static int getNbMaxTheme()
	{
		return Theme.MAX+1;
	}
	public Theme(int theme)
	{
		this.theme=Theme.isTheme(theme) ?theme:Theme.MIN ;
		quizz=Quizz.lireFichier(fichierThemes[this.theme]);
	}
	public String toString()
	{
		return Theme.getName(theme);
	}
	public Question getQuestion()
	{
		return quizz.getQuestion();
	}
	
	public int getValue()
	{
		return theme;
	}
	public static String getName(int theme)
	{
		return isTheme(theme) ? nomThemes[theme]:"theme inconnu !";
	}
	public static boolean isTheme(int theme)
	{
		return theme >=MIN && theme <=MAX; 
	}
	public Color getColor()
	{
		Color resultat=Color.CYAN;
		switch (theme)
		{
		case Theme.TERRE:
			resultat= Color.BLUE;
		break;
		case Theme.HISTOIRE:
			resultat=Color.YELLOW;
			break;
		case Theme.LETTRES:
			resultat= Color.DARK_GRAY;
			break;
		case Theme.DIVERTISSEMENTS:
			resultat=Color.PINK;
			break;
		case Theme.SPORT:
			resultat=Color.ORANGE;
			break;
		case Theme.VIE:
			resultat=Color.GREEN;
			break;
		}
		return resultat;
	}
}
