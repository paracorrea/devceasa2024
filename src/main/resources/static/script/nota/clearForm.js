/**
 * Arquivo de limpeza dos formularios
 */

document.addEventListener('DOMContentLoaded', (event) => {
    // Set the date field to today's date
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('data').value = today;

    // Clear form fields
    document.getElementById('portaria').selectedIndex = 0;
    document.getElementById('faixaHorario').selectedIndex = 0;
    document.getElementById('tipoVeiculo').selectedIndex = 0;
    document.getElementById('localDestino').selectedIndex = 0;
    document.getElementById('municipio').selectedIndex = 0;

    const itensContainer = document.getElementById('itensContainer');
    while (itensContainer.children.length > 1) {
        itensContainer.removeChild(itensContainer.lastChild);
    }
    const firstItem = itensContainer.firstChild;
    firstItem.querySelector('#propriedade').selectedIndex = 0;
    firstItem.querySelector('#quantidade').value = '';
    firstItem.querySelector('#unidadeMedida').selectedIndex = 0;
});