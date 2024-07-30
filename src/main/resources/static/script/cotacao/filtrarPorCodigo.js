/**
 * 
 */

		function filtrarPorCodigo() {
			var input, filter, select, options, i, txtValue;
			input = document.getElementById("codigoPesquisa");
			filter = input.value.toUpperCase();
			select = document.getElementById("propriedade");
			options = select.getElementsByTagName("option");

			// Desmarcar todas as opções antes de aplicar o filtro
			for (i = 0; i < options.length; i++) {
				options[i].selected = false;
			}

			// Aplicar o filtro
			for (i = 0; i < options.length; i++) {
				txtValue = options[i].text || options[i].textContent;
				if (txtValue.toUpperCase().indexOf(filter) > -1) {
					options[i].style.display = "";
					select.selectedIndex = i; // Selecionar automaticamente o produto encontrado
				} else {
					options[i].style.display = "none";
				}
			}

			// Atualizar a exibição da seleção atual
			var selectedOption = select.options[select.selectedIndex];
			var selectedText = selectedOption ? selectedOption.text || selectedOption.textContent : "";
			document.getElementById("selecaoAtual").innerText = "Seleção Atual: " + selectedText;

			// Limpar os campos e buscar a cotação anterior
			limparCampos();
			buscarCotacaoAnterior();
		}