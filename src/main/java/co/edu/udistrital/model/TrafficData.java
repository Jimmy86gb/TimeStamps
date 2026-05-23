package co.edu.udistrital.model;

/**
 * Clase responsable de gestionar las operaciones matemáticas y de negocio del 
 * tráfico.
 * Lógica de la "Suma" y de la "Propagación Perezosa" (Lazy) de la estructura 
 * jerárquica del árbol.
 * * @author Jimmy86gb
 */
public class TrafficData {

    /**
     * Valor consolidado del tráfico en el segmento.
     */
    public long value;

    /**
     * Etiqueta de actualización pendiente (Lazy Tag).
     */
    public long lazy;

    /**
     * Constructor por defecto. Inicializa el tráfico y las tareas pendientes 
     * en cero.
     */
    public TrafficData() {
        this.value = 0;
        this.lazy = 0;
    }

    /**
     * Aplica un valor acumulado (Lazy) al nodo actual multiplicándolo por el 
     * tamaño del subrango para obtener el tráfico total real.
     * @param lazyValue El valor de tráfico por milisegundo a sumar.
     * @param rangeSize El tamaño del rango (cantidad de milisegundos que 
     * abarca).
     */
    public void applyLazy(long lazyValue, long rangeSize) {
        this.value += lazyValue * rangeSize;
        this.lazy += lazyValue;
    }

    /**
     * Consolida o mezcla los datos provenientes de dos nodos hijos.
     * Calcula la suma total del tráfico de la rama izquierda y derecha.
     * Puede ser nulo si no existe:
     * @param leftData  Los datos del hijo izquierdo
     * @param rightData Los datos del hijo derecho 
     */
    public void merge(TrafficData leftData, TrafficData rightData) {
        long leftVal = 0;
        long rightVal = 0;

        if (leftData != null) {
            leftVal = leftData.value;
        }

        if (rightData != null) {
            rightVal = rightData.value;
        }

        this.value = leftVal + rightVal;
    }

    /**
     * Verifica si este nodo tiene una actualización pendiente por delegar.
     * @return true si existe un valor lazy mayor a cero, false en caso 
     * contrario.
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
     * propagación.
     */
    public void clearTasks() {
        this.lazy = 0;
    }
}