import java.io.*;
// Méthodes de lecture au clavier
public class Lecture
{
  // Lecture d'une chaîne
  public static String lireString()
  {
    String ligne_lue = null;
    try
    {
      InputStreamReader lecteur = new InputStreamReader(System.in);
      BufferedReader entree = new BufferedReader(lecteur);
      ligne_lue = entree.readLine();
    }
    catch (IOException err)
    {
      System.exit(0);
    }
    return ligne_lue;
  }

  // Lecture d'un réel double
  public static double lireDouble()
  {
    double x = 0;
    try
    {
       String ligne_lue = lireString();
       x = Double.parseDouble(ligne_lue);
    }
    catch (NumberFormatException err)
    {
       System.out.print("Erreur de donnée : ");
       lireDouble();
    }
    return x;
  }
  // Lecture d'un entier
  public static int lireInt()
  {
    int n = 0;
    try
    {
       String ligne_lue = lireString();
       n = Integer.parseInt(ligne_lue);
    }
    catch (NumberFormatException err)
    {
       System.out.print ("Erreur de donnée : ");
       lireInt();
    }
    return n;
  }

  // Programme test de la classe Lecture
  public static void main (String[] args)
  {
    System.out.print("Donner un double: ");
    double x;
    x = Lecture.lireDouble();
    System.out.println("Résultat " + x);
    System.out.print("Donner un entier: ");
    int n;
    n = Lecture.lireInt();
    System.out.println("Résultat " + n);
  }
}
