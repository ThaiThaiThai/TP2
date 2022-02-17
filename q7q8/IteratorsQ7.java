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


// /** Interface désignant une itération en cours. */
// interface Iterator<T> {
//     /** Renvoie vrai s'il existe un prochain élément. */
//     boolean hasNext();
//     /** Donne le prochain élément et prépare le passage au suivant. */
//     T next();
// }

// /** Interface des collections sur les éléments desquelles on peut itérer. */
// interface Iterable<T> {
//     Iterator<T> iterator();
// }

public class IteratorsQ7 {
    public static void main(String[] args) {
	ArrayList<String> vent = new ArrayList<String>();
	vent.add("'            ,            ,  «   -    ».");
	vent.add("           ,             ,   '           ,   '    ,   '   .");
	vent.add("          ,       ,               .");
	// Création d'un itérateur
	Iterator<String> it = vent.iterator();
	// Tant qu'il reste des éléments...
	while (it.hasNext()) {
	    // prendre le prochain élément et l'afficher.
	    System.out.println(it.next());
	}
    }
}

/** Première version : tableau non redimensionable, non paramétré. */
class FixedCapacityList implements List<Object>, Iterable<Object> {
    final int capacity;
    private Object[] elements;
    int size = 0;
    
    public FixedCapacityList(int c) {
	this.capacity = c;
	this.elements = new Object[c];
    }
    public Object get(int i) { return this.elements[i]; }
    public void add(Object elt) {
	if (this.size < this.capacity) this.elements[this.size++] = elt;
    }

    public Iterator<Object> iterator() {
	return new AscendingIterator();
    }
    public Iterator<Object> reversedIterator() {
	return new DescendingIterator();
    }
    

    // Différence : on a de toute façon un pointeur vers l'objet de la
    // classe [FixedCapacityList] qui a créé l'itérateur, donc pas besoin
    // de l'attribut [list].
    // Le constructeur et les accès aux champs sont modifiés en conséquence
    // (le constructeur disparaît même dans la version ascendante puisqu'il
    // n'y a plus rien à initialiser).
    class AscendingIterator implements Iterator<Object> {
	private int currentIndex = 0;
	public boolean hasNext() { return this.currentIndex < size; }
	public Object next() { return get(currentIndex++); }
    }
    
    class DescendingIterator implements Iterator<Object> {
	private int currentIndex;
	public DescendingIterator() {
	    this.currentIndex = size;
	}
	public boolean hasNext() { return this.currentIndex > 0; }
	public Object next() { return get(--currentIndex); }
    }

}

    

class LinkedList<T> implements List<T>, Iterable<T> {
    private Block firstBlock, lastBlock;
    public LinkedList() {
	this.firstBlock = null;
	this.lastBlock = null;
    }
    public void add(T elt) {
	if (this.firstBlock == null) {
	    Block b = new Block(elt);
	    this.firstBlock = b;
	    this.lastBlock = b;
	} else {
	    this.lastBlock = new Block(elt, this.lastBlock);
	}
    }
    public T get(int i) {
	Block currentBlock = this.firstBlock;
	int remaining = i;
	while (remaining > 0) {
	    currentBlock = currentBlock.nextBlock;
	    remaining--;
	}
	return currentBlock.contents;
    }
    public Iterator<T> iterator() {
	// Différence : on ne passe plus le premier bloc en paramètre au
	// constructeur, il y aura accès via l'attribut [firstBlock].
	return new LinkedListIterator();
    }


    // Différence : on ne mentionne plus de paramètre de type, car on intègre
    // déjà le [T] de la classe englobante.
    class Block {
	T contents;
	Block nextBlock;
	public Block(T elt) {
	    this.contents = elt;
	    this.nextBlock = null;
	}
	public Block(T elt, Block previousBlock) {
	    this.contents = elt;
	    this.nextBlock = null;
	    previousBlock.nextBlock = this;
	}
    }

    class LinkedListIterator implements Iterator<T> {
	private Block currentBlock;
	public LinkedListIterator() {
	    this.currentBlock = firstBlock;
	}
	public boolean hasNext() {
	    return currentBlock != null;
	}
	public T next() {
	    T elt = currentBlock.contents;
	    this.currentBlock = this.currentBlock.nextBlock;
	    return elt;
	}
    }

}

