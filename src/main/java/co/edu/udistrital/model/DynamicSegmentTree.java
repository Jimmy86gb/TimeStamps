package co.edu.udistrital.model;

/**
 * Gestor principal del Árbol de Segmentos Dinámico (Dynamic Segment Tree).
 * Proporciona una interfaz para actualizar y consultar rangos masivos de datos
 * @author Jimmy86gb
 */
public class DynamicSegmentTree {
    private Node root;
    private long MIN_VAL;
    private long MAX_VAL; 

    /**
     * Construye un nuevo árbol inicializando sus límites de operación.
     * @param minVal Límite inferior del rango de análisis
     * @param maxVal Límite superior del rango de análisis
     */
    public DynamicSegmentTree(long minVal, long maxVal) {
        this.root = new Node();
        this.MIN_VAL = minVal;
        this.MAX_VAL = maxVal;
    }

    /**
     * Obtiene el nodo raíz del árbol de segmentos.
     * @return El nodo raíz que representa el rango total [MIN_VAL, MAX_VAL].
     */
    public Node getRoot() { 
        return root; 
    }

    /**
     * Obtiene el límite inferior definido para el árbol.
     * @return El valor mínimo del rango gestionado por el árbol.
     */
    public long getMinVal() { 
        return MIN_VAL; 
    }

    /**
     * Obtiene el límite superior definido para el árbol.
     * @return El valor máximo del rango gestionado por el árbol.
     */
    public long getMaxVal() { 
        return MAX_VAL; 
    }

    /**
     * Operación interna que ordena al nodo actual propagar su información a 
     * las capas inferiores.
     * @param node Nodo actual que evaluará si necesita propagar trabajo.
     * @param L Límite izquierdo del nodo actual.
     * @param R Límite derecho del nodo actual.
     * @param mid Punto medio del rango.
     */
    private void pushDown(Node node, long L, long R, long mid) {
        if (node == null){
            return;
        }
        long leftSize = mid - L + 1;
        long rightSize = R - mid;
        node.pushDownToChildren(leftSize, rightSize);
    }

    /**
     * Interfaz pública para añadir una cantidad de tráfico uniforme a lo largo 
     * de un rango de tiempo.
     * @param qL      Timestamp inicial de la actualización.
     * @param qR      Timestamp final de la actualización.
     * @param traffic Cantidad de tráfico en MB a sumar por cada milisegundo 
     * en el rango.
     */
    public void updateRange(long qL, long qR, long traffic) {
        updateRange(root, MIN_VAL, MAX_VAL, qL, qR, traffic);
    }

    /**
     * Implementación recursiva privada para actualizar un rango de tiempo 
     * específico.
     * @param node Nodo que se está evaluando actualmente.
     * @param L    Límite inferior del segmento cubierto por el nodo actual.
     * @param R    Límite superior del segmento cubierto por el nodo actual.
     * @param qL   Límite inferior del rango de consulta.
     * @param qR   Límite superior del rango de consulta.
     * @param val  Valor de tráfico a aplicar en el rango.
     */
    private void updateRange(Node node, long L, long R, long qL, long qR,
            long val) {
        if (node == null || qL > R || qR < L){
            return; // Fuera del rango
        } 
        
        // Solape total: El nodo absorbe todo el trabajo de manera perezosa
        if (qL <= L && R <= qR) {
            node.data.applyLazy(val, R - L + 1); 
            return;
        }
        
        long mid = L + (R - L) / 2;
        pushDown(node, L, R, mid);
        
        if (node.left == null){
            node.left = new Node();
        }
        if (node.right == null){
            node.right = new Node();
        }
        
        updateRange(node.left, L, mid, qL, qR, val);
        updateRange(node.right, mid + 1, R, qL, qR, val);
        
        // El nodo se actualiza tras haber procesado sus ramas inferiores
        node.updateConsolidatedValue(); 
    }

    /**
     * Interfaz pública para consultar el tráfico total acumulado en un rango 
     * específico.
     * @param qL Timestamp de inicio de la búsqueda.
     * @param qR Timestamp de fin de la búsqueda.
     * @return El tráfico total consolidado dentro de los límites dados.
     */
    public long queryRange(long qL, long qR) {
        return queryRange(root, MIN_VAL, MAX_VAL, qL, qR);
    }

    /**
     * Implementación recursiva privada para consultar el tráfico total en 
     * un rango.
     * @param node Nodo que se está evaluando actualmente.
     * @param L    Límite inferior del segmento cubierto por el nodo actual.
     * @param R    Límite superior del segmento cubierto por el nodo actual.
     * @param qL   Límite inferior del rango de consulta.
     * @param qR   Límite superior del rango de consulta.
     * @return     Suma del tráfico en el rango solicitado.
     */
    private long queryRange(Node node, long L, long R, long qL, long qR) {
        if (node == null || qL > R || qR < L){
            return 0;
        } // Sin cobertura
        
        // Solape total: Devuelve la sumatoria sin seguir descendiendo
        if (qL <= L && R <= qR){
            return node.data.value;
        } 
        
        long mid = L + (R - L) / 2;
        
        // Limpiar las tareas pendientes antes de leer ramas inferiores para no 
        // obtener datos viejos
        pushDown(node, L, R, mid);
        
        return queryRange(node.left, L, mid, qL, qR) + 
               queryRange(node.right, mid + 1, R, qL, qR);
    }
}