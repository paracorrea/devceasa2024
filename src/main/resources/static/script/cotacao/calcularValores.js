/**
 * calcularValores.js
 */

function calcularValores() {
    var valores = [];
    var camposValidos = true;

    for (var i = 1; i <= 10; i++) {
        var campoId = 'valor' + i;
        var valor = document.getElementById(campoId).value;

        if (valor !== '') {
            if (!validarNumero(valor, campoId)) {
                camposValidos = false;
                break;
            }

            valores.push(parseFloat(valor.replace(',', '.')));
        }
    }

    if (camposValidos) {
        var pesoDaCotacaoSelecionada = parseFloat(document.getElementById('selectedPropertyPeso').value.replace(',', '.'));
        var pesoVariavel = parseFloat(document.getElementById('pesoVariavel').value.replace(',', '.'));

        if (!isNaN(pesoVariavel)) {
            pesoDaCotacaoSelecionada = pesoVariavel;
        }

        if (isNaN(pesoDaCotacaoSelecionada) || pesoDaCotacaoSelecionada === null) {
            pesoDaCotacaoSelecionada = 1;
        }

        var precoMinimo = Math.min.apply(null, valores) / pesoDaCotacaoSelecionada;
        var precoMaximo = Math.max.apply(null, valores) / pesoDaCotacaoSelecionada;
        var precoMedio = valores.reduce(function (acc, val) {
            return acc + val;
        }, 0) / valores.length / pesoDaCotacaoSelecionada;

        document.getElementById('precoMinimo').value = precoMinimo.toFixed(2).replace('.', ',');
        document.getElementById('precoMaximo').value = precoMaximo.toFixed(2).replace('.', ',');
        document.getElementById('precoMedio').value = precoMedio.toFixed(2).replace('.', ',');

        var contagemValores = {};
        valores.forEach(function (valor) {
            contagemValores[valor] = (contagemValores[valor] || 0) + 1;
        });

        var valorModal = null;
        var contagemMaxima = 0;

        for (var valor in contagemValores) {
            if (contagemValores[valor] > contagemMaxima) {
                contagemMaxima = contagemValores[valor];
                valorModal = parseFloat(valor) / pesoDaCotacaoSelecionada;
            }
        }

        if (contagemMaxima === 1) {
            alert('Alerta: Nenhum valor modal encontrado.');
        } else {
            alert('Valor Modal: ' + valorModal.toFixed(2));

            document.getElementById('valorComum').value = valorModal.toFixed(2).replace('.', ',');
        }
    }
}

function validarNumero(valor, campoId) {
    var numero = parseFloat(valor.replace(',', '.'));

    if (isNaN(numero)) {
        alert("Por favor, insira um valor numérico válido no campo " + campoId + ".");
        return false;
    }

    return true;
}
