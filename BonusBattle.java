/* This programs simulates a REALISTIC battle between 2 pokemons teams
 * Written by Wan Mei TAO #2235016 
 * December 9th 2022
 * 
 * Printing issues with VS Code even when using Run and Debug
 */

//Importing the Scanner and Random classes from the java.util package
import java.util.Scanner;
import java.util.Random;

public class BonusBattle {  
  //Creating a Scanner called scan  
  public static Scanner scan = new Scanner (System.in);
  //Creating a Random called random
  public static Random random = new Random();

  //main method
  public static void main(String[] args)
  {
    //Collecting information about team 1
    System.out.println("Team 1, please answer the following questions:");
    System.out.println();
    String[] pokemonsTeam1      = collectPokemonNames();
      //Creating a variable that stores the length of the array of pokemons, or the number of total Pokemons
      int length = pokemonsTeam1.length;
    System.out.println();
    String[] pokemonTypesTeam1  = collectPokemonTypes(length);
    System.out.println();
    int[]    pokemonLevelsTeam1 = collectPokemonLevels(length);
    System.out.println();
    System.out.println();

    //Collecting information about team 2
    System.out.println("Team 2, please answer the following questions:");
    System.out.println();
    String[] pokemonsTeam2      = collectPokemonNames();
    System.out.println();
    String[] pokemonTypesTeam2  = collectPokemonTypes(length);
    System.out.println();
    int[]    pokemonLevelsTeam2 = collectPokemonLevels(length);
    System.out.println();
    System.out.println();

    //Calculating the initial HP of each team
    double[] team1HP = calculateHP(pokemonLevelsTeam1);
    double[] team2HP = calculateHP(pokemonLevelsTeam2);

    //Calculating the attack ratio of each Pokemon depending on its level
    int[] team1LevelAttackRatios = levelAttackRatio(pokemonLevelsTeam1, pokemonLevelsTeam2);
    int[] team2LevelAttackRatios = levelAttackRatio(pokemonLevelsTeam2, pokemonLevelsTeam1);
  
    //Calculating the attack ratio of each Pokemon depending on its type
    double[] team1TypeAttackRatios  = typeAttackRatio(pokemonTypesTeam1, pokemonTypesTeam2);
    double[] team2TypeAttackRatios  = typeAttackRatio(pokemonTypesTeam2, pokemonTypesTeam1);

    //Creating arrays for each team to store their current HP
    double[] team1CurrentHP = new double[length];
    double[] team2CurrentHP = new double[length];

    //Variables that are useful for the loop
    int numRounds = length;
    int numBattle = 1;
    boolean finishBattle = false;

    //Variables that keeps track of fainted and ready-to-play pokemons
    int team1Fainted = 0;
    int team2Fainted = 0;
    int team1Alive   = 0;
    int team2Alive   = 0;

    //Battle starts
    while (!finishBattle)
    {        
      //Announcing the battle
      System.out.println("Battle #" + numBattle + " starts");
      System.out.println();
      numBattle++;

      //This loop is to go through each round (six rounds maximum)
      for (int index = 0; index < numRounds; index++)
      {            
        //Presenting the pokemons that will battle
        System.out.print("Round " + (index+1) + " starts: ");
        System.out.print(pokemonsTeam1[index] + ", " + pokemonTypesTeam1[index] + ", level " + pokemonLevelsTeam1[index] + ", HP: " + team1HP[index] + " vs ");
        System.out.println(pokemonsTeam2[index] + ", " + pokemonTypesTeam2[index] + ", level " + pokemonLevelsTeam2[index] + ", HP: " + team2HP[index]);
        
        //Printing which team is starting first
        int attackingTeam = random.nextInt(2) + 1;
        System.out.println( (attackingTeam == 1) ? (pokemonsTeam1[index] + " from Team 1 starts the battle!") : (pokemonsTeam2[index] + " from Team 2 starts the battle!") );

        /*
         * There is only one case where both team attatck with 0 HP which makes the loop infinite 
         * until there is a Critical Hit and that's when Ghost and Normal types attack each other
         */

        //initializing the damage from each team's attack
        double team1AttackDamage;
        double team2AttackDamage;

        //assigning each team's HP to the current array
        team1CurrentHP[index] = team1HP[index];
        team2CurrentHP[index] = team2HP[index];

        //This loop is to keep attacking until one Pokemon faints
        while (team1CurrentHP[index] > 0.0 && team2CurrentHP[index] > 0.0)
        {
          //If number is odd, team 1 attacks. Otherwise (even number), team 2 starts
          if (attackingTeam%2 == 1)
          {
            //Calculating attack damage and new HP of opponent making sure numbers are rounded to one decimal
            team1AttackDamage = damageFromAttackMove(team1LevelAttackRatios[index], team1TypeAttackRatios[index], team2HP[index], team2CurrentHP[index]);
            //Sometimes, for example, HP damage is 12.599999999999999, so Math.round is to make it 12.6
            team1AttackDamage = (Math.round(team1AttackDamage*10))/10.0;
            team2CurrentHP[index] = team2CurrentHP[index] - team1AttackDamage;
            team2CurrentHP[index] = (Math.round(team2CurrentHP[index]*10))/10.0;

            //Printing who attacks with how much damage and the opponent's new HP
            System.out.println(pokemonsTeam1[index] + " from Team 1 attacks and causes " + team1AttackDamage + " HP damage on " + pokemonsTeam2[index] + " that is now at an HP of " + team2CurrentHP[index]);
            
            //increasing attackingTeam so that the opponent will play when the loop goes again          
            attackingTeam++;
          }
          else
          {
            //Calculating attack damage and new HP of opponent making sure numbers are rounded to one decimal
            team2AttackDamage = damageFromAttackMove(team2LevelAttackRatios[index], team2TypeAttackRatios[index], team1HP[index], team1CurrentHP[index]);
            //Sometimes, for example, HP damage is 12.599999999999999, so Math.round is to make it 12.6
            team2AttackDamage = (Math.round(team2AttackDamage*10))/10.0;
            team1CurrentHP[index] = team1CurrentHP[index] - team2AttackDamage;
            team1CurrentHP[index] = (Math.round(team1CurrentHP[index]*10))/10.0;
            
            //Printing who attacks with how much damage and the opponent's new HP
            System.out.println(pokemonsTeam2[index] + " from Team 2 attacks and causes " + team2AttackDamage + " HP damage on " + pokemonsTeam1[index] + " that is now at an HP of " + team1CurrentHP[index]);
            
            //increasing attackingTeam so that the opponent will play when the loop goes again          
            attackingTeam++;
          }
        }

        //Printing who fainted and who winned depending on which Pokemon have less than or equal to 0 HP
        if (team1CurrentHP[index] <= 0.0)
        {
          System.out.println("->" + pokemonsTeam1[index] + " fainted!");
          System.out.println("->Victory for " + pokemonsTeam2[index] + " from Team 2 on Round " + (index+1) );
          //Keeping track of every fainted pokemon of team 1
          team1Fainted++;
        }
        else
        {
          System.out.println("->" + pokemonsTeam2[index] + " fainted!");
          System.out.println("->Victory for " + pokemonsTeam1[index] + " from Team 1 on Round " + (index+1) );
          //Keeping track of every fainted pokemon of team 2
          team2Fainted++;
        }

        System.out.println();
      }

      //Calculating how many ready-to-fight pokemons in each team
      team1Alive = length - team1Fainted;
      team2Alive = length - team2Fainted;

      //If one team has no available pokemons, the battle is over
      if (team1Alive == 0 || team2Alive == 0)
      {
        //To end the loop  
        finishBattle = true;
        //Method to print the results of the battle
        determineWinningTeam(team1Alive, team2Alive);
      }
      else
      {
        //If team 1 has 4 pokemons left and team 2 has 2 pokemons left, there will only be two rounds in the next battle
        numRounds = Math.min(team1Alive, team2Alive);
        //Printing how many pokemons are still available for each team
        System.out.println();
        System.out.print("Team 1 still have " + team1Alive + " Pokemons ready to play and ");
        System.out.println("team 2 still have " + team1Alive + " Pokemons ready to play.");
        System.out.println();
        System.out.println();
        
        //Available Pokemons will change indexes to the first index as possible in all of their team's array
        int counterTeam1 = 0;
        int counterTeam2 = 0;
        for (int i = 0; i < length; i++)
        {
          if (team1CurrentHP[i] > 0.0)
          {
            team1HP[counterTeam1] = team1CurrentHP[i];
            pokemonsTeam1[counterTeam1] = pokemonsTeam1[i];
            pokemonTypesTeam1[counterTeam1] = pokemonTypesTeam1[i];
            pokemonLevelsTeam1[counterTeam1] = pokemonLevelsTeam1[i];
            counterTeam1++;
          }

          if (team2CurrentHP[i] > 0.0)
          {
            team2HP[counterTeam2] = team2CurrentHP[i];
            pokemonsTeam2[counterTeam2] = pokemonsTeam2[i];
            pokemonTypesTeam2[counterTeam2] = pokemonTypesTeam2[i];
            pokemonLevelsTeam2[counterTeam2] = pokemonLevelsTeam2[i];
            counterTeam2++;
          }
        }
      }
    }

    scan.close();
  }
  
  //Question 1 method
  public static double[] typeAttackRatio(String [] team1, String [] team2)
  {

    double [] attackRatio = new double[team1.length];

    //assigning to each pokemon their attack ratio
    for (int i=0; i < team1.length; i++)
    {
      attackRatio[i] = ratioBetween(team1[i], team2[i]);
    }

    return attackRatio;
  }

  //Question 2 method
  public static int[] levelAttackRatio(int [] levelsTeam1, int [] levelsTeam2)
  {

    int[] attackRatios = new int[levelsTeam1.length];

    //assigning to each pokemon their attack ratio depending on their difference of levels
    for (int i = 0; i<levelsTeam1.length; i++)
    {
      attackRatios[i] = levelsTeam1[i] - levelsTeam2[i];

      if (attackRatios[i]<= -10)
      {
        attackRatios[i] = 1;
      }
      else if (attackRatios[i] > -10 && attackRatios[i] <= 9)
      {
        attackRatios[i] = 10;
      }
      else
      {
        attackRatios[i] = attackRatios[i] + 10;
      }
    }

    return attackRatios;
  }

  //Question 3 method
  public static double[] calculateHP(int[] levelsTeam)
  {

    double[] healthPoint = new double[levelsTeam.length];

    //assigning to each pokemon their HP
    for (int i=0; i<levelsTeam.length; i++)
    {
      healthPoint[i] = levelsTeam[i] * 10.0;
    }

    return healthPoint;
  }

  //Question 4.1 method
  public static String[] collectPokemonNames()
  {
    //Collecting the number of pokemons
    System.out.println("What is the size of your Pokemon team:");
    int size = scan.nextInt();
    scan.nextLine();
    System.out.println();

    //Collecting the names
    String [] pokemonNames = new String[size];
    System.out.println("Please enter each of your Pokemon's name one at a time");
    for (int i = 0; i<size; i++)
    {
      pokemonNames[i] = scan.nextLine();
    }

    return pokemonNames;
  }

  //Question 4.2 method
  public static String[] collectPokemonTypes(int length)
  {
    System.out.println("Please enter the corresponding Pokemon types for the team:");
    String[] pokemonTypes = new String[length];
    for (int i=0; i<length; i++)
    {
      pokemonTypes[i] = scan.nextLine();
    }

    return pokemonTypes;
  }

  //Question 4.3 method
  public static int[] collectPokemonLevels(int length)
  {
    System.out.println("Please enter the corresponding Pokemon levels for the team:");
    int [] pokemonLevels = new int[length];
    for (int i=0; i<length;i++)
    {
      pokemonLevels[i] = scan.nextInt();
    }
    
    return pokemonLevels;
  }

  //Question 5 method
  public static double damageFromAttackMove(int attackLevelRatio, double attackTypeRatio, double opponentInitialHP, double opponentCurrentHP)
  {
    double attackDamage;
    
    //42 is the critical hit number among 1 to 100
    if ((random.nextInt(100) + 1) == 42)
    {
      attackDamage = opponentCurrentHP;
      System.out.println("Critical hit!");
    }
    else
    {
      attackDamage = (attackLevelRatio/100.0*opponentInitialHP)*attackTypeRatio;
    }

    return attackDamage;
  }

  //Question 6 method (modified to fit the bonus)
  public static void determineWinningTeam(int team1Alive, int team2Alive)
  {
    System.out.println();
    System.out.println("The battle is over!");
    System.out.println((team1Alive > team2Alive ? "Team 1 wins with " + team1Alive : "Team 2 wins with " + team2Alive) + " Pokemon(s) still standing up!");
  }

  /*
   * DO NOT MODIFY ANYTHING BELOW THESE COMMENTS
   */
  
  enum PokemonType {
    NORMAL, FIRE, WATER, ELECTRIC, GRASS, ICE, FIGHTING, POISON, GROUND, FLYING, PSYCHIC, BUG, ROCK, GHOST, DRAGON,
    DARK, STEEL, FAIRY
  }

  public static double ratioBetween(String pokemon1, String pokemon2) {
    int pokemonAttack = PokemonType.valueOf(pokemon1.toUpperCase()).ordinal();
    int pokemonDefense = PokemonType.valueOf(pokemon2.toUpperCase()).ordinal();
    double[][] attackRatio = {
                 // nor  fir  wat  ele  gra  ice  fig  poi  gro  fly  psy  bug  roc  gho  dra  dar  ste  fai
        /* nor */ { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5, 0.0, 1.0, 1.0, 0.5, 1.0 },
        /* fir */ { 1.0, 0.5, 0.5, 1.0, 2.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 0.5, 1.0, 0.5, 1.0, 2.0, 1.0 },
        /* wat */ { 1.0, 2.0, 0.5, 1.0, 0.5, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 1.0, 2.0, 1.0, 0.5, 1.0, 1.0, 1.0 },
        /* ele */ { 1.0, 1.0, 2.0, 0.5, 0.5, 1.0, 1.0, 1.0, 0.0, 2.0, 1.0, 1.0, 1.0, 1.0, 0.5, 1.0, 1.0, 1.0 },
        /* gra */ { 1.0, 0.5, 2.0, 1.0, 0.5, 1.0, 1.0, 0.5, 2.0, 0.5, 1.0, 0.5, 2.0, 1.0, 0.5, 1.0, 0.5, 1.0 },
        /* ice */ { 1.0, 0.5, 0.5, 1.0, 2.0, 0.5, 1.0, 1.0, 2.0, 2.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 0.5, 1.0 },
        /* fig */ { 2.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 0.5, 1.0, 0.5, 0.5, 0.5, 2.0, 0.0, 1.0, 2.0, 2.0, 0.5 },
        /* poi */ { 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 0.5, 0.5, 1.0, 1.0, 1.0, 0.5, 0.5, 1.0, 1.0, 0.0, 2.0 },
        /* gro */ { 1.0, 2.0, 1.0, 2.0, 0.5, 1.0, 1.0, 2.0, 1.0, 0.0, 1.0, 0.5, 2.0, 1.0, 1.0, 1.0, 2.0, 1.0 },
        /* fly */ { 1.0, 1.0, 1.0, 0.5, 2.0, 1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 2.0, 0.5, 1.0, 1.0, 1.0, 0.5, 1.0 },
        /* psy */ { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 2.0, 1.0, 1.0, 0.5, 1.0, 1.0, 1.0, 1.0, 0.0, 0.5, 1.0 },
        /* bug */ { 1.0, 0.5, 1.0, 1.0, 2.0, 1.0, 0.5, 0.5, 1.0, 0.5, 2.0, 1.0, 1.0, 0.5, 1.0, 2.0, 0.5, 0.5 },
        /* roc */ { 1.0, 2.0, 1.0, 1.0, 1.0, 2.0, 0.5, 1.0, 0.5, 2.0, 1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 0.5, 1.0 },
        /* gho */ { 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 2.0, 1.0, 0.5, 1.0, 1.0 },
        /* dra */ { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 0.5, 0.0 },
        /* dar */ { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 2.0, 1.0, 0.5, 1.0, 0.5 },
        /* ste */ { 1.0, 0.5, 0.5, 0.5, 1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 1.0, 0.5, 2.0 },
        /* fai */ { 1.0, 0.5, 1.0, 1.0, 1.0, 1.0, 2.0, 0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 2.0, 0.5, 1.0 } };
    return attackRatio[pokemonAttack][pokemonDefense];
  }
}