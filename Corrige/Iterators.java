//import java.lang.Iterable;
//import java.util.Iterator;

import java.util.ArrayList;
import java.util.List;


/**
 * Interface des listes (extrait).
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

    public static void printIterator2(Iterator<Object> i) {
        while (i.hasNext()) {
            Object o = i.next();
            System.out.println(o);
            System.out.println(o);
        }
    }

    public static void printIterator3(Iterator<Object> i) {
        while (i.hasNext()) {
            System.out.println(i.next());
            if (i.hasNext()) i.next();
        }
    }

    public static void main(String[] args) {

        LinkedList<Object> l = new LinkedList<>();
        l.add("aaaaaaaaaaaaaaaaaaaaa");

        l.add("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        l.add("cccccccc");
        l.add("ddddddddddddddddddddd");
        for (int i = 0; i < 4; i++)
            System.out.println(l.get(i));
        printIterator2(l.iterator());
        printIterator3(l.iterator());
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
    public Object get(int i) {
        return this.elements[i];
    }

    // Ajouter l'élément si la capacité n'est pas atteinte, et
    // ne rien faire sinon.
    public void add(Object elt) {
        if (this.size < this.capacity) this.elements[this.size++] = elt;
    }

    @Override
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

class LinkedList<T> implements List2<T>, Iterable<T> {
    // On maintient des références vers les premier et dernier blocs.
    private Block<T> firstBlock, lastBlock;

    // À l'origine on crée une liste vide : il n'y a ni premier ni dernier bloc
    public LinkedList() {
        this.firstBlock = null;
        this.lastBlock = null;
    }

    // Ajout d'un élément
    public void add(T elt) {
        // On crée un nouveau bloc [b]
        Block<T> b = new Block<T>(elt);
        // Si la liste est vide [b] devient le premier bloc,
        // sinon [b] devient le successeur du dernier bloc courant
        if (this.firstBlock == null) {
            this.firstBlock = b;
        } else {
            this.lastBlock.nextBlock = b;
        }
        // Dans tous les cas, [b] devient le dernier bloc
        this.lastBlock = b;
    }

    @Override
    public int size() {
        Block<T> currentBlock = this.firstBlock;
        int j = 0;
        for (; currentBlock != null; j++) currentBlock = currentBlock.nextBlock;
        return j;
    }




    // Accès à l'élément à un indice [i] donné
    public T get(int i) {
        // On mémorise un bloc courant,
        Block<T> currentBlock = this.firstBlock;
        // on avance [i] fois,
        for (int j = 0; j < i; j++) currentBlock = currentBlock.nextBlock;
        // et on renvoie la valeur trouvée.
        return currentBlock.contents;
    }

    // Création d'un itérateur à qui on fournit le premier bloc, à partir
    // duquel il pourra accéder à tous les autres
    public Iterator<T> iterator() {
        return new LinkedListIterator<T>(this.firstBlock);
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


/**
 * Itérateur descendant.
 */
class DescendingIterator<T> implements Iterator<T> {
    // Similaire au précédente, mais la position initiale est la dernière de
    // la liste, et chaque appel à [next] décrémente la position.
    private List2<T> list;
    private int currentIndex;

    public DescendingIterator(List2<T> l) {
        this.list = l;
        this.currentIndex = l.size();
    }

    public boolean hasNext() {
        return this.currentIndex > 0;
    }

    public T next() {
        return this.list.get(--currentIndex);
    }
}


/**
 * Itérateur de liste chaînée
 */
class LinkedListIterator<T> implements Iterator<T> {
    // On mémorise uniquement le bloc courant, les champs [nextBlock]
    // suffiront à atteindre les autres
    private Block<T> currentBlock;

    // À la construction, on fournit un bloc (a priori le premier)
    public LinkedListIterator(Block<T> block) {
        this.currentBlock = block;
    }

    // Il y a un prochain élément tant qu'il y a un bloc
    public boolean hasNext() {
        return currentBlock != null;
    }

    // Extrait l'élément du bloc courant, puis change le bloc courant. Si le
    // bloc courant était le dernier, [currentBlock] prendra la valeur [null].
    public T next() {
        T elt = currentBlock.contents;
        this.currentBlock = this.currentBlock.nextBlock;
        return elt;
    }
}
