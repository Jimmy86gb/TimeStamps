package co.edu.udistrital.controller;

import co.edu.udistrital.model.DynamicSegmentTree;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador de tráfico de red para la aplicación web.
 * Actúa como mediador entre la vista (index.jsp) y el modelo 
 * (DynamicSegmentTree), gestionando la inicialización del árbol y las 
 * operaciones de actualización y consulta.
 * * @author Jimmy86gb
 */
@WebServlet(name = "TrafficServlet", urlPatterns = {"/TrafficServlet"})
public class TrafficServlet extends HttpServlet {

    /**
     * Procesa las peticiones HTTP para ambos métodos (GET y POST).
     * Gestiona la sesión del usuario para persistir la instancia del árbol de 
     * segmentos y enruta las acciones de inicialización, actualización de 
     * datos o consulta de tráfico.
     * @param request  El objeto HttpServletRequest que contiene la petición 
     * del cliente.
     * @param response El objeto HttpServletResponse que se enviará al cliente.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de entrada o salida.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        DynamicSegmentTree tree = (DynamicSegmentTree) session.getAttribute("segmentTree");
        
        String action = request.getParameter("action");
        
        try {
            // Acción para inicializar el árbol con el rango definido por el usuario
            if ("initTree".equals(action)) {
                long min = Long.parseLong(request.getParameter("minRango"));
                long max = Long.parseLong(request.getParameter("maxRango"));
                tree = new DynamicSegmentTree(min, max);
                session.setAttribute("segmentTree", tree);
                request.setAttribute("message",
                        "Árbol inicializado con rango de " + min + " a " + max);
                
            } else if (tree != null) { 
                // Operaciones de negocio si el árbol ya existe en sesión
                if ("updateRange".equals(action)) {
                    long start = Long.parseLong(request.getParameter("uStart"));
                    long end = Long.parseLong(request.getParameter("uEnd"));
                    long traffic = 
                            Long.parseLong(request.getParameter("traffic"));
                    tree.updateRange(start, end, traffic);
                    request.setAttribute("message", 
                            "Tráfico actualizado exitosamente.");
                    
                } else if ("queryRange".equals(action)) {
                    long start = Long.parseLong(request.getParameter("qStart"));
                    long end = Long.parseLong(request.getParameter("qEnd"));
                    long result = tree.queryRange(start, end);
                    request.setAttribute("queryResult", 
                            "Tráfico en [" + start + " - " + end + "]: " 
                                    + result + " MB");
                }
            } else {
                if(action != null){
                    request.setAttribute("error",
                            "Primero debes inicializar el rango del árbol.");
                }
            }
        } catch (Exception e) {
            if(action != null){
                request.setAttribute("error", 
                        "Error en los datos. Verifica los números.");
            }
        }

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * Maneja las peticiones HTTP GET delegando el procesamiento a 
     * processRequest.
     * @param request  El objeto HttpServletRequest que contiene la petición 
     * del cliente.
     * @param response El objeto HttpServletResponse que se enviará al cliente.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de entrada o salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException { 
        processRequest(request, response); 
    }

    /**
     * Maneja las peticiones HTTP POST delegando el procesamiento a 
     * processRequest.
     * @param request  El objeto HttpServletRequest que contiene la petición 
     * del cliente.
     * @param response El objeto HttpServletResponse que se enviará al cliente.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de entrada o salida.
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException { 
        processRequest(request, response); 
    }
}