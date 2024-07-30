/**
 * // culc field mercado with valorComum anterior and atual
 */
function calcularMercado() {
    var valorComumAtual = parseFloat(document.getElementById("valorComum").value);
    var valorComumAnterior = parseFloat(document.getElementById("valorComumAnterior").value);
    
    console.log("Valor Comum Anterior = : " + valorComumAnterior);
    console.log("Valor Comum Atual = : " + valorComumAtual);

    if (!isNaN(valorComumAnterior)) {
        if (valorComumAtual > valorComumAnterior) {
            document.getElementById("mercado").value = "MFI"; // Mercado Forte
        } else if (valorComumAtual < valorComumAnterior) {
            document.getElementById("mercado").value = "MFR"; // Mercado Ruim
        } else if (valorComumAtual === valorComumAnterior) {
            document.getElementById("mercado").value = "ME"; // Mercado Estável
        } else {
            document.getElementById("mercado").value = "MV"; // Mercado Vazio (não foi possível calcular a diferença)
        }
    } else {
        document.getElementById("mercado").value = "MV"; // Caso não tenha valores da cotação anterior
    }
}