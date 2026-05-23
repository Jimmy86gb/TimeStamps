package co.edu.udistrital.model;

/**
 * Clase responsable de gestionar las operaciones matemáticas y de negocio del 
 * tráfico.
 * @author Jimmy86gb
 */
public class TrafficData {

    private long value;
    private long lazy;

    /**
     * Constructor por defecto. Inicializa el tráfico acumulado y las tareas 
     * pendientes (lazy) en cero.
     */
    public TrafficData() {
        this.value = 0;
        this.lazy = 0;
    }

    /**
     * Obtiene el valor consolidado del tráfico en el segmento.
     * @return El valor total del tráfico registrado.
     */
    public long getValue() { 
        return value; 
    }

    /**
     * Obtiene la etiqueta de actualización pendiente (Lazy Tag).
     * @return El valor pendiente de propagar a las ramas inferiores.
     */
    public long getLazy() { 
        return lazy; 
    }

    /**
     * Aplica un valor acumulado (Lazy) al nodo actual multiplicándolo por el 
     * tamaño del subrango para obtener el tráfico total real.
     * @param lazyValue El valor de tráfico por milisegundo a sumar.
     * @param rangeSize El tamaño del rango (cantidad de milisegundos que abarca).
     */
    public void applyLazy(long lazyValue, long rangeSize) {
        this.value += lazyValue * rangeSize;
        this.lazy += lazyValue;
    }

    /**
     * Consolida o mezcla los datos provenientes de dos nodos hijos, 
     * calculando la suma total del tráfico de la rama izquierda y derecha.
     * @param leftData  Los datos del hijo izquierdo (puede ser nulo).
     * @param rightData Los datos del hijo derecho (puede ser nulo).
     */
    public void merge(TrafficData leftData, TrafficData rightData) {
        long leftVal = (leftData != null) ? leftData.getValue() : 0;
        long rightVal = (rightData != null) ? rightData.getValue() : 0;
        this.value = leftVal + rightVal;
    }

    /**
     * Verifica si este nodo tiene una actualización pendiente por delegar.
     * @return true si existe un valor lazy mayor a cero, false en caso contrario.
     */
    public boolean hasPendingTasks() {
        if (this.lazy > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Limpia la etiqueta de tareas pendientes tras haber realizado la 
     * propagación a los hijos.
     */
    public void clearTasks() {
        this.lazy = 0;
    }
}