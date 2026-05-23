const viewport = document.getElementById('viewport');
const treeContainer = document.getElementById('tree-container');

let isDragging = false;
let currentX = 0, currentY = 0;
let initialX = 0, initialY = 0;
let xOffset = 0, yOffset = 0;
let currentScale = 1;

viewport.addEventListener("mousedown", dragStart);
window.addEventListener("mouseup", dragEnd);
window.addEventListener("mousemove", drag);

function dragStart(e) {
    initialX = e.clientX - xOffset;
    initialY = e.clientY - yOffset;
    isDragging = true;
    viewport.style.cursor = 'grabbing';
    treeContainer.style.transition = 'none'; 
}

function dragEnd(e) {
    if (!isDragging) return;
    initialX = currentX;
    initialY = currentY;
    isDragging = false;
    viewport.style.cursor = 'grab';
    treeContainer.style.transition = 'transform 0.2s ease-out'; 
}

function drag(e) {
    if (isDragging) {
        e.preventDefault(); 
        currentX = e.clientX - initialX;
        currentY = e.clientY - initialY;
        xOffset = currentX;
        yOffset = currentY;
        applyTransform();
    }
}

function applyTransform() {
    treeContainer.style.transform = "translate(" + currentX + "px, "
            + currentY + "px) scale(" + currentScale + ")";
}

function zoomIn() { 
    currentScale += 0.15; applyTransform();
}
function zoomOut() { 
    if (currentScale > 0.2){ 
        currentScale -= 0.15; applyTransform();
    } 
}
function resetZoom() {
    currentScale = 1; currentX = 0; currentY = 0; xOffset = 0; yOffset = 0;
    applyTransform();
}

viewport.addEventListener('wheel', (e) => {
    e.preventDefault();
    if (e.deltaY < 0) zoomIn(); else zoomOut();
});