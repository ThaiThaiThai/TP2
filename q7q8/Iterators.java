import java.lang.Iterable;
import java.util.Iterator;
import java.util.ArrayList;

/** Interface des listes (extrait). */
interface List<T> {
    /** Renvoie l'élément d'indice [i]. */
    T get(int i);
    /** Ajoute l'élément [elt] à la fin de la liste. */
    void add(T elt);
}

/** Interface désignant une itération en cours. */
/*
interface Iterator<T> {
    // Renvoie vrai s'il existe un prochain élément.
    boolean hasNext();
    // Donne le prochain élément et prépare le passage au suivant.
    T next();
}
*/

/** Interface des collections sur les éléments desquelles on peut itérer. */
/*
interface Iterable<T> {
    Iterator<T> iterator();
}
*/

public class Iterators {
    public static void main(String[] args) {
        ArrayList<String> abcd = new ArrayList<String>();
        abcd.add("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        abcd.add("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        abcd.add("cccccccc");
        abcd.add("ddddddddddddddddddddd");
        // Création d'un itérateur
        Iterator<String> it = abcd.iterator();
        // Tant qu'il reste des éléments...
        while (it.hasNext()) {
            // prendre le prochain élément et l'afficher.
            System.out.println(it.next());
        }
        
        // Deux fois chaque élément
        // Création d'un itérateur
        Iterator<String> it2 = abcd.iterator();
        // Tant qu'il reste des éléments...
        while (it2.hasNext()) {
            // prendre le prochain élément
            String s = it2.next();
            // et l'afficher deux fois
            System.out.println(s);
            System.out.println(s);
        }

        // Un élément sur deux
        // Création d'un itérateur
        Iterator<String> it3 = abcd.iterator();
        // Tant qu'il reste des éléments...
        while (it3.hasNext()) {
            // prendre le prochain élément et l'afficher
            System.out.println(it3.next());
            // puis passer au suivant s'il y en a un.
            if (it3.hasNext()) { it3.next(); }
        }

    }
}

/** Première version : tableau non redimensionable, non paramétré. */
class FixedCapacityList implements List<Object>, Iterable<Object> {
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
    public Object get(int i) { return this.elements[i]; }

    // Ajouter l'élément si la capacité n'est pas atteinte, et
    // ne rien faire sinon.
    public void add(Object elt) {
        if (this.size < this.capacity) this.elements[this.size++] = elt;
    }

    // Création d'itérateurs
    public Iterator<Object> iterator() {
        // La liste est passée elle-même en paramètre, car elle va servir
        // de base à l'itérateur.
        return new AscendingIterator(this);
    }
    public Iterator<Object> reversedIterator() {
        return new DescendingIterator(this);
    }
    
}

/** Itérateur ascendant. */
class AscendingIterator implements Iterator<Object> {
    // On garde une référence sur la liste sur laquelle on itère
    private final FixedCapacityList list;
    // On retient en plus la position courante
    private int currentIndex = 0;
    // La liste sur laquelle itérer est passée en paramètre à la construction
    public AscendingIterator(FixedCapacityList l) {
        this.list = l;
    }
    // Il reste des éléments tant que la position courante n'a pas atteint
    // la fin de la liste
    public boolean hasNext() { return this.currentIndex < list.size; }
    // Extrait l'élément à la position courante, puis incrémente la position
    public Object next() { return this.list.get(currentIndex++); }
}

/** Itérateur descendant. */
class DescendingIterator implements Iterator<Object> {
    // Similaire au précédente, mais la position initiale est la dernière de
    // la liste, et chaque appel à [next] décrémente la position.
    private FixedCapacityList list;
    private int currentIndex;
    public DescendingIterator(FixedCapacityList l) {
        this.list = l;
        this.currentIndex = l.size;
    }
    public boolean hasNext() { return this.currentIndex > 0; }
    public Object next() { return this.list.get(--currentIndex); }
}
    
/** Deuxième version : liste chaînée, paramétrée par un type [T] de contenu. */
/**
   On définit d'abord une classe pour les blocs de la liste chaînée.
   Note : on pourrait faire de cette classe une classe interne de [LinkedList].
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

class LinkedList<T> implements List<T>, Iterable<T> {
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

    // Accès à l'élément à un indice [i] donné
    public T get(int i) {
        // On mémorise un bloc courant,
        Block<T> currentBlock = this.firstBlock;
        // on avance [i] fois,
        for (int j=0; j<i; j++) currentBlock = currentBlock.nextBlock;
        // et on renvoie la valeur trouvée.
        return currentBlock.contents;
    }

    // Création d'un itérateur à qui on fournit le premier bloc, à partir
    // duquel il pourra accéder à tous les autres
    public Iterator<T> iterator() {
        return new LinkedListIterator<T>(this.firstBlock);
    }
}

/** Itérateur de liste chaînée */
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
