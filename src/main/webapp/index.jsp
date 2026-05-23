<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="co.edu.udistrital.model.DynamicSegmentTree"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Dynamic Segment Tree</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="dashboard-container">
        
        <header class="dashboard-header">
            <div class="brand">
                <h1>TRAFFIC ANALYZER</h1>
            </div>
        </header>

        <div class="workspace-grid">
            
            <div class="control-box">
                <div class="tabs-forms">
                    <h3>Panel de Estructura Inicial</h3>
                </div>
                
                <form action="TrafficServlet" method="POST" class="form-dark">
                    <input type="hidden" name="action" value="initTree">
                    <div class="inline-inputs">
                        <input type="number" name="minRango" placeholder="Min Rango" value="1" required>
                        <input type="number" name="maxRango" placeholder="Max Rango" value="100000" required>
                    </div>
                    <button type="submit" class="btn-cyan">Inicializar Estructura</button>
                </form>

                <form action="TrafficServlet" method="POST" class="form-dark">
                    <input type="hidden" name="action" value="updateRange">
                    <div class="inline-inputs">
                        <input type="number" name="uStart" placeholder="Desde ms" required>
                        <input type="number" name="uEnd" placeholder="Hasta ms" required>
                    </div>
                    <input type="number" name="traffic" placeholder="Carga (MB)" required>
                    <button type="submit" class="btn-amber">Ejecutar Update (Lazy)</button>
                </form>
            </div>

            <div class="control-box">
                <h3>Panel de Búsqueda</h3>
                <form action="TrafficServlet" method="POST" class="form-dark">
                    <input type="hidden" name="action" value="queryRange">
                    <div class="inline-inputs">
                        <input type="number" name="qStart" placeholder="Desde ms" required>
                        <input type="number" name="qEnd" placeholder="Hasta ms" required>
                    </div>
                    <button type="submit" class="btn-green">Lanzar Query de Rango</button>
                </form>
            </div>

            <div class="control-box summary-panel">
                <h3>Resumen de la Operación</h3>
                <div class="stat-row">
                    <span class="stat-label">Comando Ejecutado:</span>
                    <span class="stat-value text-cyan"><%= request.getAttribute("summaryAction") != null ? request.getAttribute("summaryAction") : "Ninguno" %></span>
                </div>
                <div class="stat-row">
                    <span class="stat-label">Intervalo Afectado:</span>
                    <span class="stat-value"><%= request.getAttribute("summaryRange") != null ? request.getAttribute("summaryRange") : "N/A" %></span>
                </div>
                <div class="stat-row">
                    <span class="stat-label">Complejidad Temporal:</span>
                    <span class="stat-value text-amber"><%= request.getAttribute("summaryComplexity") != null ? request.getAttribute("summaryComplexity") : "O(1)" %></span>
                </div>
                <div class="stat-row">
                    <span class="stat-label">Estado de Ramas:</span>
                    <span class="stat-value text-green"><%= request.getAttribute("summaryStatus") != null ? request.getAttribute("summaryStatus") : "Estable" %></span>
                </div>
                <div class="stat-row">
                    <span class="stat-label">Salida del Query:</span>
                    <span class="stat-value text-green">
                        <%= request.getAttribute("queryResult") != null ? request.getAttribute("queryResult") : "No se ha ejecutado" %>
                    </span>
                </div>
                <div class="stat-row">
                    <span class="stat-label">Aviso del Sistema:</span>
                    <span class="stat-value">
                        <% 
                        if (request.getAttribute("error") != null) { 
                        %>
                            <span style="color: var(--color-red);"><%= request.getAttribute("error") %></span>
                        <% 
                        } else if (request.getAttribute("message") != null) { 
                        %>
                            <span style="color: var(--color-cyan);"><%= request.getAttribute("message") %></span>
                        <% 
                        } else { 
                        %>
                            <span style="color: var(--text-secondary);">En espera...</span>
                        <% 
                        } 
                        %>
                    </span>
                </div>
            </div>
        </div>

        <div class="canvas-box">
            <div class="canvas-header">
                <span class="title">Arbol</span>
                <div class="controls">
                    <button onclick="zoomIn()">Zoom +</button>
                    <button onclick="zoomOut()">Zoom -</button>
                    <button onclick="resetZoom()" class="btn-border">Reset</button>
                </div>
            </div>
            
            <div class="tree-viewport" id="viewport">
                <div class="zoom-container" id="tree-container">
                    <ul class="tree">
                        <%
                            if (request.getAttribute("htmlTree") != null) {
                                out.print(request.getAttribute("htmlTree"));
                            } else {
                        %>
                                <div class="welcome-msg">
                                    <p>INICIALICE EL RANGO PARA ANALIZAR LOS NODOS DINÁMICOS</p>
                                </div>
                        <%
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