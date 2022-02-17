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

public class IteratorsQ8 {
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

	ExtensibleList caracole = new ExtensibleList(3);
	caracole.add("Furvent, ");
	caracole.add("ceux ");
	caracole.add("qui ");
	caracole.add("vont ");
	caracole.add("mûrir ");
	caracole.add("te ");
	caracole.add("saluent !");
	// Remarque en passant : l'interface [Iterable] est exactement ce
	// qui permet l'itération de type "for each". L'effet du code suivant
	// est comme au-dessus la création d'un itérateur, puis l'itération
	// tant que [hasNext] renvoie [true].
	for (Object obj : caracole) {
	    System.out.println(obj.toString());
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
    Block firstBlock, lastBlock;
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
	return new LinkedListIterator();
    }

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

class ExtensibleList implements List<Object>, Iterable<Object> {
    private LinkedList<FixedCapacityList> list;
    private final int capacity;

    public ExtensibleList(int c) {
	this.list = new LinkedList<FixedCapacityList>();
	this.capacity = c;
    }
    
    public void add(Object elt) {
	if (list.lastBlock != null && list.lastBlock.contents.size < capacity) {
	    list.lastBlock.contents.add(elt);
	} else {
	    FixedCapacityList fcl = new FixedCapacityList(this.capacity);
	    fcl.add(elt);
	    list.add(fcl);
	}
    }
    public Object get(int i) {
	return list.get(i/capacity).get(i%capacity);
    }
    public Iterator<Object> iterator() {
	return new ExtensibleListIterator();
    }

    class ExtensibleListIterator implements Iterator<Object> {
	// Une manière brutale consiste à garder un compteur global, et
	// à enchaîner les appels à [get]. C'est similaire à ce qui était fait
	// pour [FixedCapacityList]. On évite les accès répétés à la liste
	// chaînée en utilisant un peu de la technique utilisée dans
	// [LinkedList].
	private LinkedList<FixedCapacityList>.Block currentBlock;
	private int currentIndex;
	public ExtensibleListIterator() {
	    this.currentBlock = list.firstBlock;
	    this.currentIndex = 0;
	}
	public boolean hasNext() {
	    // Cette version ne prévoit pas le cas de la liste vide.
	    return this.currentIndex < this.currentBlock.contents.size
		|| this.currentBlock.nextBlock != null;
	}
	public Object next() {
	    if (this.currentIndex >= this.currentBlock.contents.size) {
		this.currentBlock = this.currentBlock.nextBlock;
		this.currentIndex = 0;
	    }
	    return this.currentBlock.contents.get(this.currentIndex++);
	}
    }
}
