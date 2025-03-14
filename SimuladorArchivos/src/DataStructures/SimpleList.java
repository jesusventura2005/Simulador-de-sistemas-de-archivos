package DataStructures;

public class SimpleList {

    private Nodo pFirst;
    private Nodo pLast;
    private int size;

    public SimpleList() {
        this.pFirst = null;
        this.pLast = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return (getpFirst() == null);
    }

    public Object getValor(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        Nodo actual = pFirst;
        for (int i = 0; i < index; i++) {
            actual = actual.getpNext();
        }
        return actual.getInfo();
    }

    public boolean contains(Object elemento) {
        Nodo actual = pFirst;
        while (actual != null) {
            if (actual.getInfo().equals(elemento)) {
                return true;
            }
            actual = actual.getpNext();
        }
        return false;
    }

    public void insertLast(Object x) {
        Nodo nuevo = new Nodo(x);
        if (this.isEmpty()) {
            setpFirst(pLast = nuevo);
        } else {
            Nodo aux = pLast;
            aux.setpNext(nuevo);
            pLast = nuevo;
        }
        size++;
    }

    public void remove(Object elemento) {
        if (isEmpty()) {
            return;
        }

        if (pFirst.getInfo().equals(elemento)) {
            pFirst = pFirst.getpNext();
            size--;
            if (pFirst == null) {
                pLast = null;
            }
            return;
        }

        Nodo prev = pFirst;
        Nodo current = pFirst.getpNext();
        while (current != null) {
            if (current.getInfo().equals(elemento)) {
                prev.setpNext(current.getpNext());
                size--;
                if (current == pLast) {
                    pLast = prev;
                }
                return;
            }
            prev = current;
            current = current.getpNext();
        }
    }

    public String printList() {
        if (isEmpty()) {
            return "La lista está vacía.";
        }

        StringBuilder sb = new StringBuilder(); 
        Nodo actual = getpFirst();
        int count = 0;

        while (actual != null) {
            sb.append("[").append(actual.getInfo()).append("] ");
            actual = actual.getpNext();
            count++;

            if (count % 5 == 0) {
                sb.append("\n"); 
            }
        }

        if (count % 5 != 0) {
            sb.append("\n"); // Salto de línea si la última fila no está completa
        }

        return sb.toString(); // Retorna la cadena construida
    }

    public void vaciar() {
        this.pFirst = null;
        this.pLast = null;
        this.size = 0;

        Nodo actual = pFirst;
        while (actual != null) {
            Nodo siguiente = actual.getpNext();
            actual.setpNext(null);
            actual = siguiente;
        }
    }

    public Nodo getpFirst() {
        return pFirst;
    }

    public void setpFirst(Nodo pFirst) {
        this.pFirst = pFirst;
    }

    public Nodo getpLast() {
        return pLast;
    }

    public void setpLast(Nodo pLast) {
        this.pLast = pLast;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void replaceBLock(Object elemento, int index) {
        if (index < 0 || index >= size) {
            return;
        }

        Nodo actual = pFirst;
        for (int i = 0; i < index; i++) {
            actual = actual.getpNext();
        }
        actual.setInfo(elemento);
    }

}
