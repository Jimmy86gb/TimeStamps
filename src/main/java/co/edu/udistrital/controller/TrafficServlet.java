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
 * Controlador que procesa los comandos de la red y genera el resumen
 * operativa para el panel de resumen.
 * * @author Jimmy86gb
 */
@WebServlet(name = "TrafficServlet", urlPatterns = {"/TrafficServlet"})
public class TrafficServlet extends HttpServlet {

    /**
     * Procesa las peticiones HTTP, gestiona la sesión del árbol de segmentos y
     * enruta las acciones de inicialización, actualización o consulta según el
     * parámetro de acción recibido.
     * @param request  Objeto con la petición enviada por el cliente.
     * @param response Objeto para configurar la respuesta enviada al cliente.
     * @throws ServletException Si ocurre un error durante el procesamiento.
     * @throws IOException      Si ocurre un error de entrada o salida.
     */
    protected void processRequest(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        DynamicSegmentTree tree =
                (DynamicSegmentTree) session.getAttribute("segmentTree");
        String action = request.getParameter("action");
        
        // Valores por defecto para el resumen lateral
        request.setAttribute("summaryAction", "Ninguna");
        request.setAttribute("summaryRange", "N/A");
        request.setAttribute("summaryComplexity", "O(1)");
        request.setAttribute("summaryStatus", "Inactivo");

        try {
            if ("initTree".equals(action)) {
                long min = Long.parseLong(request.getParameter("minRango"));
                long max = Long.parseLong(request.getParameter("maxRango"));
                tree = new DynamicSegmentTree(min, max);
                session.setAttribute("segmentTree", tree);
                
                request.setAttribute("message", "Servidor inicializado.");
                request.setAttribute("summaryAction", "CONSTRUIR");
                request.setAttribute("summaryRange", "[" + min + " - " 
                        + max + "]");
                request.setAttribute("summaryComplexity", "O(1) - Raíz");
                request.setAttribute("summaryStatus", "Éxito");
                
            } else if (tree != null) { 
                if ("updateRange".equals(action)) {
                    long start = Long.parseLong(request.getParameter("uStart"));
                    long end = Long.parseLong(request.getParameter("uEnd"));
                    long traffic =
                            Long.parseLong(request.getParameter("traffic"));
                    
                    tree.updateRange(start, end, traffic);
                    
                    request.setAttribute("message", "Tráfico inyectado.");
                    request.setAttribute("summaryAction", "UPDATE (LAZY)");
                    request.setAttribute("summaryRange", "[" + start + " - "
                            + end + "]");
                    request.setAttribute("summaryComplexity", "O(log N)");
                    request.setAttribute("summaryStatus", "Dato Aplazado");
                    
                } else if ("queryRange".equals(action)) {
                    long start = Long.parseLong(request.getParameter("qStart"));
                    long end = Long.parseLong(request.getParameter("qEnd"));
                    
                    long result = tree.queryRange(start, end);
                    
                    request.setAttribute("queryResult", "Resultado: " + result
                            + " MB");
                    request.setAttribute("summaryAction", "QUERY (SUMA)");
                    request.setAttribute("summaryRange", "[" + start + " - " +
                            + end + "]");
                    request.setAttribute("summaryComplexity", "O(log N)");
                    request.setAttribute("summaryStatus", "PushDown Evaluado");
                }
            } else {
                if (action != null) {
                    request.setAttribute("error", "Requiere inicialización.");
                }
            }
        } catch (Exception e) {
            if (action != null) {
                request.setAttribute("error", "Error de formato numérico.");
            }
        }
        
        if (tree != null && tree.getRoot() != null) {
            String htmlTree = TreeRenderer.render(tree.getRoot(),
                    tree.getMinVal(), tree.getMaxVal());
            request.setAttribute("htmlTree", htmlTree);
        }
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * Gestiona las peticiones GET redirigiendo el flujo a processRequest.
     * @param request  Petición HTTP recibida.
     * @param response Respuesta HTTP a generar.
     * @throws ServletException Si ocurre un error de servlet.
     * @throws IOException      Si ocurre un error de entrada o salida.
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException { 
        processRequest(request, response); 
    }

    /**
     * Gestiona las peticiones POST redirigiendo el flujo a processRequest.
     * @param request  Petición HTTP recibida.
     * @param response Respuesta HTTP a generar.
     * @throws ServletException Si ocurre un error de servlet.
     * @throws IOException      Si ocurre un error de entrada o salida.
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException { 
        processRequest(request, response); 
    }
}