package co.edu.udistrital.controller;
import co.edu.udistrital.model.Node;

/**
 * Clase utilitaria encargada de transformar la estructura del árbol binario
 * en una representación jerárquica de listas HTML.
 * @author Jimmy86gb
 */
public class TreeRenderer {

    /**
     * Genera la cadena HTML que representa el estado actual del árbol.
     * @param node Nodo raíz desde el cual comenzar el renderizado.
     * @param L    Límite inferior del rango del nodo.
     * @param R    Límite superior del rango del nodo.
     * @return     String con la estructura de etiquetas ul y li.
     */
    public static String render(Node node, long L, long R) {
        StringBuilder sb = new StringBuilder();
        renderInternal(node, L, R, sb);
        return sb.toString();
    }

    /**
     * Método recursivo interno para construir el árbol HTML de abajo 
     * hacia arriba.
     */
    private static void renderInternal(Node node, long L, long R,
            StringBuilder sb) {
        if (node == null) {
            return;
        }

        sb.append("<li>");
        sb.append("<div class='node-box'>");
        sb.append("<span class='rango-texto'>[ ").append(L).append(" - ").append(R).append(" ]</span><br/>");
        sb.append("<span class='suma-texto'>Suma: <strong>").append(node.data.getValue()).append("</strong></span><br/>");

        if (node.data.getLazy() > 0) {
            sb.append("<span class='lazy-tag'>Lazy: +").append(node.data.getLazy()).append("</span>");
        }

        sb.append("</div>");

        if (node.left != null || node.right != null) {
            sb.append("<ul>");
            long mid = L + (R - L) / 2;
            renderInternal(node.left, L, mid, sb);
            renderInternal(node.right, mid + 1, R, sb);
            sb.append("</ul>");
        }
        sb.append("</li>");
    }
}