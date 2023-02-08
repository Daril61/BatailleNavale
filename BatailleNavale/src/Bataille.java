import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Classe principale du jeu de bataille navale
 *
 * @since 08/02/2023
 * @author Romain Veydarier
 */
public class Bataille {

    /**
     * Liste des tailles des bateaux dans l'ordre par rapport à la variable bateauxNom
     */
    public static final int[] bateauxTaille = new int[]{
            5,
            4,
            3,
            3,
            2
    };
    /**
     * Liste des noms des bateaux
     */
    public static final String[] bateauxNom = new String[] {
            "Porte-avions",
            "Croiseur",
            "Contre-torpilleurs",
            "Sous-marin",
            "Torpilleur"
    };
    /**
     * Liste des colonnes, c'est-à-dire les lettres de la grille
     */
    public static final char[] colonne = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    /**
     * Grille qui contient la carte de l'ordinateur
     */
    public static int[][] grilleOrdi = new int[10][10];
    /**
     * Grille qui contient la carte du joueur
     */
    public static int[][] grilleJeu = new int[10][10];

    /**
     * Variable aléatoire pour pouvoir générer des nombres aléatoires
     */
    public static Random rand = new Random();

    /**
     * Fonction principale qui permet de gérer le programme
     *
     * @since 07/02/2023
     *
     * @param args Arguments par défaut de la fonction main
     */
    public static void main(String[] args) {
        // Initialisation des 2 grilles
        initGrilleOrdi();
        initGrilleJeu();
        AfficherGrille(grilleJeu);

        // Tant qu'il reste entre des bateaux dans les deux grilles alors on joue
        while(!vainqueur(grilleJeu) && !vainqueur(grilleOrdi)) {
            // Tour de l'ordinateur
            int[] position = tirOrdinateur();
            mouvement(grilleJeu, position[0], position[1]);

            // Vérification que l'ordinateur a gagné
            if(vainqueur(grilleJeu)) {
                System.out.println();
                System.out.println("Victoire de l'ordinateur !");
                System.out.println();
                break;
            }

            System.out.println();
            AfficherGrille(grilleJeu);
            // Tour du joueur
            position = demandePosition();
            mouvement(grilleOrdi, position[0], position[1]);

            // Vérification que le joueur a gagné
            if(vainqueur(grilleOrdi)) {
                System.out.println();
                System.out.println("Victoire du joueur !");
                System.out.println();
                break;
            }
        }

        System.out.println("Fin de l'exécution du programme !");
    }

    /**
     * Fonction pour générer un nombre aléatoire par rapport à 2 bornes a et b
     *
     * @since 06/02/2023
     *
     * @param a Première borne inclus
     * @param b Deuxième borne exclu
     * @return Un nombre aléatoire entre a et b-1
     */
    public static int randRange(int a, int b) {
        return rand.nextInt(b-a) + a;
    }

    /**
     * Fonction qui demande à l'utilisateur, un numéro de ligne et un numéro de colonne pour faire une position
     *
     * @since 06/02/2023
     *
     * @return Un tableau d'entier composé de 2 valeurs (0 => numéro de ligne | 1 => numéro de colonne)
     */
    public static int[] demandePosition() {
        Scanner scanner = new Scanner(System.in);

        int l = -1;
        while(l < 1 || l > 10) {
            System.out.print("Entrer le numéro de ligne (1, 2, ..., 10): ");
            if(scanner.hasNextInt())
                l = scanner.nextInt();

            scanner.nextLine();
        }
        // On retire 1 à la variable 'l' car on utilise de 0 à 9 et non de 1 à 10
        l--;

        int c = -1;
        while(c < 0) {
            System.out.print("Entrer le numéro de colonne (A, B, ..., J): ");

            String strColonne = scanner.nextLine();
            if(strColonne.length() > 0) {
                char charColonne = strColonne.charAt(0);
                c = Arrays.binarySearch(colonne, charColonne);
            }
        }

        int[] position = new int[]{l, c};
        return position;
    }

    /**
     * Fonction pour vérifier que l'on puisse placer un bateau par rapport à plusieurs paramètres
     *
     * @since 06/02/2023
     *
     * @param grille Une grille de 10 x 10
     * @param l Un numéro de ligne (compris entre 0 et 9)
     * @param c Un numéro de colonne (compris entre 0 et 9)
     * @param d Une direction (1 => Horizontal | 2 => Vertical)
     * @param t Nombre de cases que prend le bateau
     *
     * @return Retourne vraie (true) si on peut mettre le bateau sur les cases correspondantes
     */
    public static boolean posOk(int[][] grille, int l, int c, int d, int t) {

        // Cas horizontal
        if (d == 1) {

            // Vérification que le bateau puisse rentrer
            if (c - t < -1) {
                return false;
            }

            // Vérification qu'il n'y ait aucun bateau sur les cases analysées
            for (int i = c; i > (c - t); i--) {
                if(grille[l][i] != 0) {
                    return false;
                }
            }

        // Cas vertical
        } else {
            // Vérification que le bateau puisse rentrer
            if(l + t > 10) {
                return false;
            }

            // Vérification qu'il n'y ait aucun bateau sur les cases analysées
            for (int i = l; i < (l + t); i++) {
                if(grille[i][c] != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Fonction pour ajouter un bateau à une grille
     *
     * @since 06/02/2023
     *
     * @param grille Une grille de 10 x 10
     * @param l Un numéro de ligne (compris entre 0 et 9)
     * @param c Un numéro de colonne (compris entre 0 et 9)
     * @param d Une direction (1 => Horizontal | 2 => Vertical)
     * @param t Nombre de cases que prend le bateau
     * @param idBateauGrille Identifiant du bateau à afficher sur la grille
     */
    public static void ajouterBateau(int[][] grille, int l, int c, int d, int t, int idBateauGrille) {
        // Ajout du bateau sur le plateau de l'ordinateur
        if(d == 1) {
            for (int i = c; i > (c - t); i--) {
                grille[l][i] = idBateauGrille;
            }
        } else {
            for (int i = l; i < (l + t); i++) {
                grille[i][c] = idBateauGrille;
            }
        }
    }

    /**
     * Fonction pour initialiser la grille de l'ordinateur avec la mise en place des 5 bateauxTaille sur sa grille
     *
     * @since 06/02/2023
     */
    public static void initGrilleOrdi() {
        // Numéro de ligne ( 0 - 9 )
        int l = randRange(0, 10);
        // Numéro de colonne ( 0 - 9 )
        int c = randRange(0, 10);
        // Numéro de direction
        int d = randRange(1, 3);

        int idBateau = 0;
        int t;

        while(idBateau < bateauxTaille.length) {
            t = bateauxTaille[idBateau];

            // Si on peut placer le bateau
            if(posOk(grilleOrdi, l, c, d, t)) {
                ajouterBateau(grilleOrdi, l, c, d, t, (idBateau+1));
                idBateau++;

            } else {
                l = randRange(0, 10);
                c = randRange(0, 10);
                d = randRange(1, 3);
            }
        }
    }

    /**
     * Fonction pour initialiser la grille du joueur par rapport aux informations que le joueur nous fournit
     *
     * @since 06/02/2023
     */
    public static void initGrilleJeu() {
        AfficherGrille(grilleJeu);

        int idBateau = 0;
        int t;

        while (idBateau < bateauxTaille.length) {
            t = bateauxTaille[idBateau];
            Scanner scanner = new Scanner(System.in);

            System.out.println("Placement d'un " + bateauxNom[idBateau]);
            int[] position = demandePosition();
            int l = position[0];
            int c = position[1];

            int d = -1;
            while(!(d >= 1 && d <= 2)) {
                System.out.print("Entrer la direction (1 : horizontal | 2 : vertical) : ");
                if(scanner.hasNextInt())
                    d = scanner.nextInt();

                scanner.nextLine();
            }

            // Si on peut placer le bateau
            if(posOk(grilleJeu, l, c, d, t)) {
                ajouterBateau(grilleJeu, l, c, d, t, (idBateau + 1));
                AfficherGrille(grilleJeu);
                idBateau++;

            } else {
                System.out.println("Erreur: Le " + bateauxNom[idBateau] + " ne rentre pas dans la grille.");
            }
        }
    }

    /**
     * Fonction qui nous permet de savoir si un bateau est coulé
     *
     * @since 07/02/2023
     *
     * @param grille Une grille de 10 x 10
     * @param idBateau Identifiant d'un bateau
     *
     * @return Retourne vraie (true) si le bateau est coulé (qu'il n'est plus présent dans la grille)
     */
    public static boolean couler(int[][] grille, int idBateau) {
        if(idBateau < 1 || idBateau > 5) {
            System.out.println("Attention, la variable idBateau n'est pas comprise entre 1 et 5");
            return true;
        }

        for (int y = 0; y < grille.length; y++) {
            for (int x = 0; x < grille[y].length; x++) {
                if(grille[y][x] == idBateau) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Fonction qui permet de tirer
     *
     * @since 07/02/2023
     *
     * @param grille Une grille de 10 x 10
     * @param l Un numéro de ligne
     * @param c Un numéro de colonne
     */
    public static void mouvement(int[][] grille, int l, int c) {
        // Vérification que la position touche de l'eau ou un bateau déjà touché
        if(grille[l][c] <= 0 || grille[l][c] >= 6) {
            System.out.println("[" + colonne[c] + " " + (l + 1) + "] À l’eau ");
            return;
        }

        // Récupération de l'id du bateau
        int idBateau = grille[l][c];
        grille[l][c] = 6;
        // Si le bateau est coulé alors on affiche Coulé sinon Touché
        if(couler(grille, idBateau)) {
            System.out.println("[" + colonne[c] + " " + (l + 1) + "] Coulé, il s'agissait d'un " + bateauxNom[idBateau-1]);
        } else {
            System.out.println("[" + colonne[c] + " " + (l + 1) + "] Touché, il s'agit d'un " + bateauxNom[idBateau-1]);
        }
    }

    /**
     * Fonction qui génère une position aléatoire
     *
     * @since 07/02/2023
     *
     * @return Un tableau d'entier composé de 2 valeurs (0 => numéro de ligne | 1 => numéro de colonne)
     */
    public static int[] tirOrdinateur() {
        int l = randRange(0, 10);
        int c = randRange(0, 10);

        int[] position = new int[]{l, c};
        return position;
    }

    /**
     * Fonction qui nous permet de savoir s'il reste des bateaux sur la grille
     *
     * @since 07/02/2023
     *
     * @param grille Une grille de 10 x 10
     *
     * @return Retourne vraie (true) si tous les bateaux de la grille ont été coulés
     */
    public static boolean vainqueur(int[][] grille) {
        for (int y = 0; y < grille.length; y++) {
            for (int x = 0; x < grille[y].length; x++) {
                if(grille[y][x] > 0 && grille[y][x] < 6) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Fonction qui permet d'afficher une grille de jeu mis en paramètre
     *
     * @since 06/02/2023
     *
     * @param grille Une grille de 10 x 10
     */
    public static void AfficherGrille(int[][] grille) {
        System.out.print(" ");
        for (int i = 0; i < colonne.length; i++) {
            System.out.print(" " + colonne[i]);
        }

        System.out.println();
        for (int i = 0; i < grille.length; i++) {
            System.out.print((i+1) + " ");
            for (int j = 0; j < grille[i].length; j++) {
                System.out.print(grille[i][j] + " ");
            }

            System.out.println();
        }

        System.out.println();
    }
}