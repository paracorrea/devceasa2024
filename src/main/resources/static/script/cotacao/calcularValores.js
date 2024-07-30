/**
 * 
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



				// Ajusta o cálculo do preço mínimo considerando o peso da cotação
				var selectedIndex = document.getElementById('propriedade').selectedIndex;
				var selectedOption = document.getElementById('propriedade').options[selectedIndex];

				// Adicione um log para verificar o valor de peso
				console.log("Peso da Cotação: " + selectedOption.dataset.peso);

				var pesoDaCotacaoSelecionada = parseFloat(selectedOption.dataset.peso);
				var pesoVariavel = parseFloat(document.getElementById('pesoVariavel').value.replace(',', '.'));
				console.log("Peso Variavel da Cotação: " + pesoVariavel);
				
				
				if (!isNaN(pesoVariavel)) {
		            pesoDaCotacaoSelecionada = pesoVariavel;
		        }
				
				if (isNaN(pesoDaCotacaoSelecionada) || pesoDaCotacaoSelecionada === null) {
					// Se for NaN ou nulo, atribui o valor 1
					pesoDaCotacaoSelecionada = 1;
				}

				console.log("Peso da Cotação (Float): " + pesoDaCotacaoSelecionada);



				var precoMinimo = Math.min.apply(null, valores) / pesoDaCotacaoSelecionada;
				var precoMaximo = Math.max.apply(null, valores) / pesoDaCotacaoSelecionada;
				var precoMedio = valores.reduce(function (acc, val) {
					return acc + val;
				}, 0) / valores.length / pesoDaCotacaoSelecionada;

				document.getElementById('precoMinimo').value = precoMinimo.toFixed(2).replace(',', '.');
				document.getElementById('precoMaximo').value = precoMaximo.toFixed(2).replace(',', '.');
				document.getElementById('precoMedio').value = precoMedio.toFixed(2).replace(',', '.');

				// Calcula o valor modal
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

					// Atualiza o campo valorComum (modal)
					document.getElementById('valorComum').value = valorModal.toFixed(2).replace(',', '.');
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
