package co.edu.udistrital.model;

public class Node {
    public long value; // Suma total del tráfico en el rango
    public long lazy;  // La nota pendiente (Lazy Tag)
    public Node left, right; // Hijos creados dinámicamente

    public Node() {
        this.value = 0;
        this.lazy = 0;
        this.left = null;
        this.right = null;
    }
}