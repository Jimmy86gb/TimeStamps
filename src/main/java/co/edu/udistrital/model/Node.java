package co.edu.udistrital.model;

/**
 * Representa un nodo estructural dentro del Árbol de Segmentos Dinámico.
 * Su única responsabilidad es mantener la jerarquía en memoria (hijos izquierdo
 * y derecho) y delegar el comportamiento matemático a su objeto contenedor
 * {@link TrafficData}.
 * * @author Jimmy86gb
 */
public class Node {

    /**
     * Contenedor de los datos matemáticos y de estado del nodo.
     */
    public TrafficData data; 

    /**
     * Referencia al hijo izquierdo en la jerarquía del árbol.
     */
    public Node left; 

    /**
     * Referencia al hijo derecho en la jerarquía del árbol.
     */
    public Node right; 

    /**
     * Crea un nuevo nodo hoja genérico, inicializando sus punteros en nulo
     * y creando un nuevo contenedor de datos limpio.
     */
    public Node() {
        this.data = new TrafficData();
        this.left = null;
        this.right = null;
    }

    /**
     * Propaga (PushDown) el trabajo acumulado a los nodos hijos directos.
     * Si los hijos no existen en memoria, los instancia dinámicamente bajo 
     * demanda.
     * @param leftRangeSize  Tamaño del rango correspondiente al hijo izquierdo.
     * @param rightRangeSize Tamaño del rango correspondiente al hijo derecho.
     */
    public void pushDownToChildren(long leftRangeSize, long rightRangeSize) {
        if (!this.data.hasPendingTasks()){
            return;
        }

        // Instanciación dinámica (Sparse Segment Tree)
        if (this.left == null){
            this.left = new Node();
        }
        if (this.right == null){
            this.right = new Node();
        }

        // Delega la aplicación matemática a la data de los hijos
        this.left.data.applyLazy(this.data.lazy, leftRangeSize);
        this.right.data.applyLazy(this.data.lazy, rightRangeSize);

        // Limpia su propia tarea pendiente una vez delegada
        this.data.clearTasks();
    }

    /**
     * Le pide a su contenedor de datos que recalcule su valor basándose 
     * en la información actual de sus ramas izquierda y derecha.
     */
    public void updateConsolidatedValue() {
        TrafficData leftData = (this.left != null) ? this.left.data : null;
        TrafficData rightData = (this.right != null) ? this.right.data : null;
        
        this.data.merge(leftData, rightData);
    }
}