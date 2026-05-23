<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="co.edu.udistrital.model.Node"%>
<%@page import="co.edu.udistrital.model.DynamicSegmentTree"%>

<%! 
    public void drawTree(jakarta.servlet.jsp.JspWriter out, Node node, long L, long R) throws java.io.IOException {
        if (node == null) return;
        out.print("<li>");
        out.print("<span class='node-box'>[ " + L + " - " + R + " ]<br/>");
        
        // ¡CAMBIO AQUÍ! Ahora leemos la información desde el objeto 'data'
        out.print("<b>Suma: " + node.data.value + "</b><br/>");
        if (node.data.lazy > 0) {
            out.print("<span class='lazy-tag'>Lazy: +" + node.data.lazy + "</span>");
        }
        
        out.print("</span>");
        if (node.left != null || node.right != null) {
            out.print("<ul>");
            long mid = L + (R - L) / 2;
            drawTree(out, node.left, L, mid);
            drawTree(out, node.right, mid + 1, R);
            out.print("</ul>");
        }
        out.print("</li>");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Segment Tree Dinámico - Mapa Interactivo</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="main-container">
        <div class="forms-wrapper">
            <div class="panel" style="border-top-color: #ff9800;">
                <% if(request.getAttribute("error") != null) { %> <div class="alert alert-error"><b><%= request.getAttribute("error") %></b></div> <% } %>
                <h3>0. Crear Servidor</h3>
                <form action="TrafficServlet" method="POST">
                    <input type="hidden" name="action" value="initTree">
                    <label>Límite Inferior:</label>
                    <input type="number" name="minRango" value="1" required>
                    <label>Límite Superior:</label>
                    <input type="number" name="maxRango" value="1000000" required>
                    <button type="submit" style="background: #ff9800;">Construir Rango</button>
                </form>
            </div>

            <div class="panel">
                <% if(request.getAttribute("message") != null) { %> <div class="alert"><%= request.getAttribute("message") %></div> <% } %>
                <h3>1. Añadir Tráfico</h3>
                <form action="TrafficServlet" method="POST">
                    <input type="hidden" name="action" value="updateRange">
                    <label>Desde ms:</label><input type="number" name="uStart" required>
                    <label>Hasta ms:</label><input type="number" name="uEnd" required>
                    <label>MB por ms:</label><input type="number" name="traffic" required>
                    <button type="submit">Actualizar (Lazy)</button>
                </form>
            </div>

            <div class="panel" style="border-top-color: #4caf50;">
                <% if(request.getAttribute("queryResult") != null) { %> <div class="alert" style="border-color: #4caf50; color: #4caf50;"><b><%= request.getAttribute("queryResult") %></b></div> <% } %>
                <h3>2. Consultar Suma</h3>
                <form action="TrafficServlet" method="POST">
                    <input type="hidden" name="action" value="queryRange">
                    <label>Desde ms:</label><input type="number" name="qStart" required>
                    <label>Hasta ms:</label><input type="number" name="qEnd" required>
                    <button type="submit" style="background: #4caf50;">Calcular Tráfico</button>
                </form>
            </div>
        </div>

        <div>
            <div class="toolbar">
                <span style="font-weight: bold;">Controles de Vista: </span>
                <button class="btn-zoom" onclick="zoomIn()">Zoom (+)</button>
                <button class="btn-zoom" onclick="zoomOut()">Zoom (-)</button>
                <button class="btn-zoom" onclick="resetZoom()">Restablecer</button>
            </div>
            
            <div class="tree-viewport" id="viewport">
                <div class="zoom-container" id="tree-container">
                    <ul class="tree">
                        <%
                            DynamicSegmentTree tree = (DynamicSegmentTree) session.getAttribute("segmentTree");
                            if (tree != null && tree.getRoot() != null) {
                                drawTree(out, tree.getRoot(), tree.getMinVal(), tree.getMaxVal());
                            } else {
                                out.print("<p style='text-align:center;'>Configura el rango en el Panel 0 para comenzar.</p>");
                            }
                        %>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <script src="js/script.js"></script>
</body>
</html>