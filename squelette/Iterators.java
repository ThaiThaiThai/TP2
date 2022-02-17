//import java.lang.Iterable;
//import java.util.Iterator;

import java.util.ArrayList;
import java.util.List;


/**
 * Sous ensemble de l'interface List<T> .
 */
interface List2<T> {
    /**
     * Renvoie l'élément d'indice [i].
     */
    T get(int i);

    /**
     * Ajoute l'élément [elt] à la fin de la liste.
     */
    void add(T elt);

    /**
     * Renvoi le nombre d'élements de la liste.
     */
    int size();
}


/**
 * Interface désignant une itération en cours.
 */
interface Iterator<T> {
    // Renvoie vrai s'il existe un prochain élément.
    boolean hasNext();

    // Donne le prochain élément et prépare le passage au suivant.
    T next();
}


/**
 * Interface des collections sur les éléments desquelles on peut itérer.
 */
interface Iterable<T> {
    Iterator<T> iterator();
}


public class Iterators {
    public static void printIterator(Iterator<Object> i) {
        while (i.hasNext()) {
            System.out.println(i.next());
        }
    }

    public static void main(String[] args) {
        ArrayList<String> l = new ArrayList();

        l.add("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        l.add("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        l.add("cccccccc");
        l.add("ddddddddddddddddddddd");
        for (int i = 0; i < 4; i++)
            System.out.println(l.get(i));

    }

}

/**
 * Première version : tableau non redimensionable, non paramétré.
 */
class FixedCapacityList implements List2<Object>, Iterable<Object> {
    // La capacité est immuable
    private final int capacity;
    private Object[] elements;
    // Le champ [size] est déclaré visible dans le paquet, pour que les classes
    // associées [AscendingIterator] et [DescendingIterator] puissent y avoir
    // accès. Ce ne serait plus nécessaire si on définissait ces dernières
    // comme des classes internes.
    protected int size = 0;

    public FixedCapacityList(int c) {
        // Création d'un tableau de la capacité demandée.
        this.capacity = c;
        this.elements = new Object[c];
    }

    // Renvoyer l'élément, sans vérifier l'indice.
    public Object get(int i) { /*a completer*/
        return null;
    }

    // Ajouter l'élément si la capacité n'est pas atteinte, et
    // ne rien faire sinon.
    public void add(Object elt) {/*a completer*/
    }

    public int size() {
        return size;
    }

    // Création d'itérateurs
    public Iterator<Object> iterator() {
        // La liste est passée elle-même en paramètre, car elle va servir
        // de base à l'itérateur.
        return new AscendingIterator(this);
    }
}

/**
 * Deuxième version : liste chaînée, paramétrée par un type [T] de contenu.
 */

/**
 * On définit d'abord une classe pour les blocs de la liste chaînée.
 * Note : on pourrait faire de cette classe une classe interne de [LinkedList].
 */
class Block<T> {
    // Le contenu et le pointeur vers le bloc suivant sont visibles dans le
    // paquet, car la classe principale [LinkedList] y fera référence.
    protected T contents;
    protected Block<T> nextBlock;

    // Le constructeur prend en paramètres l'élément à placer dans le bloc
    public Block(T elt) {
        this.contents = elt;
        this.nextBlock = null;
    }
}

class LinkedList<T> implements List2<T> {
    // On maintient des références vers les premier et dernier blocs.
    private Block<T> firstBlock, lastBlock;

    // À l'origine on crée une liste vide : il n'y a ni premier ni dernier bloc
    public LinkedList() {
        this.firstBlock = null;
        this.lastBlock = null;
    }

    // Ajout d'un élément
    public void add(T elt) {
        /* a completer */
    }

    @Override
    public int size() {
        Block<T> currentBlock = this.firstBlock;
        int j = 0;
        for (; currentBlock != null; j++) currentBlock = currentBlock.nextBlock;
        return j;
    }

    // Accès à l'élément à un indice [i] donné
    public T get(int i) { /* a completer*/
        return null;
    }


}


/**
 * Itérateur ascendant.
 */
class AscendingIterator<T> implements Iterator<T> {
    // On garde une référence sur la liste sur laquelle on itère
    private final List2<T> list;
    // On retient en plus la position courante
    private int currentIndex = 0;

    // La liste sur laquelle itérer est passée en paramètre à la construction
    public AscendingIterator(List2<T> l) {
        this.list = l;
    }

    // Il reste des éléments tant que la position courante n'a pas atteint
    // la fin de la liste
    public boolean hasNext() {
        return this.currentIndex < list.size();
    }

    // Extrait l'élément à la position courante, puis incrémente la position
    public T next() {
        return this.list.get(currentIndex++);
    }
}


