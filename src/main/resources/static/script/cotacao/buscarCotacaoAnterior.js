/**
 * 
 */

function buscarCotacaoAnterior() {

			var propriedadeId = document.getElementById("propriedade").value;
			var dataCotacao = document.getElementById("dataCotacao").value;
			
			

			console.log("ID da cotação:", propriedadeId);
			console.log("Data da cotação:", dataCotacao);
			
			// Faça uma solicitação AJAX para buscar a cotação anterior
			// Você pode usar uma biblioteca como jQuery ou o objeto XMLHttpRequest para isso

			// Exemplo usando jQuery:
			$.ajax({
				url: "/cotacoes/buscar-cotacao-anterior",
				method: "GET",
				data: {
					propriedadeId: propriedadeId,
					dataCotacao: dataCotacao
				},
				success: function (response) {
					// Preencha os campos do formulário com os dados da cotação anterior
						
					
			 var campos = ["valor1", "valor2", "valor3", "valor4", "valor5", "valor6", "valor7", "valor8", "valor9", "valor10"];
            	campos.forEach(function (campo) {
                document.getElementById(campo).value = response[campo];
            });

           
					document.getElementById("precoMinimo").value = response.precoMinimo;
					document.getElementById("precoMedio").value = response.precoMedio;
					document.getElementById("precoMaximo").value = response.precoMaximo;
					document.getElementById("valorComum").value = response.valorComum;

					document.getElementById("valorComumAnterior").value = response.valorComum;
					console.log("Valor comum anterior:",document.getElementById("valorComumAnterior").value = response.valorComum) ;	
					// Preencha outros campos, se necessário
				},
				error: function () {
					// Trate erros, se necessário
				}
			});
		}