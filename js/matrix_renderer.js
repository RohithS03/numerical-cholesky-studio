/**
 * Visual Matrix Renderer for Cholesky Studio
 */

class MatrixRenderer {
  static createInputGrid(containerId, matrixData, onCellChange) {
    const container = document.getElementById(containerId);
    if (!container) return;

    const rows = matrixData.length;
    const cols = matrixData[0].length;

    let html = `<div class="matrix-grid-wrapper">`;
    for (let i = 0; i < rows; i++) {
      html += `<div class="matrix-row">`;
      for (let j = 0; j < cols; j++) {
        const val = matrixData[i][j];
        html += `<input type="number" step="any" class="matrix-cell-input" data-r="${i}" data-c="${j}" value="${val}" />`;
      }
      html += `</div>`;
    }
    html += `</div>`;

    container.innerHTML = html;

    // Attach event listeners
    const inputs = container.querySelectorAll('.matrix-cell-input');
    inputs.forEach(input => {
      input.addEventListener('change', (e) => {
        const r = parseInt(e.target.getAttribute('data-r'));
        const c = parseInt(e.target.getAttribute('data-c'));
        const val = parseFloat(e.target.value) || 0;
        if (onCellChange) onCellChange(r, c, val);
      });
    });
  }

  static renderVisualMatrix(containerId, matrixData, label) {
    const container = document.getElementById(containerId);
    if (!container) return;

    const rows = matrixData.length;
    const cols = matrixData[0].length;

    let html = `
      <div style="margin-bottom:0.5rem; font-weight:600; color:var(--primary-cyan)">${label} (${rows}x${cols})</div>
      <div class="matrix-grid-wrapper">
    `;

    for (let i = 0; i < rows; i++) {
      html += `<div class="matrix-row">`;
      for (let j = 0; j < cols; j++) {
        const val = matrixData[i][j];
        const isDiag = (i === j);
        const isZero = (val === 0);
        html += `<div class="matrix-cell-view ${isDiag ? 'diagonal' : ''} ${isZero ? 'zero' : ''}">${val.toFixed(3)}</div>`;
      }
      html += `</div>`;
    }
    html += `</div>`;

    container.innerHTML = html;
  }
}

window.MatrixRenderer = MatrixRenderer;
