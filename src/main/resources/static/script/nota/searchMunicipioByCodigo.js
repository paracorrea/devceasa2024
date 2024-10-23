function searchMunicipioByCodigo() {
    let codigo = document.getElementById('municipioCodigo').value;
    if (codigo.length > 2) {
        fetch(`/notas/searchMunicipioByCodigo?codigo=${codigo}`)
            .then(response => response.json())
            .then(data => {
                const municipioSelect = document.getElementById('municipio');
                municipioSelect.innerHTML = ''; // Limpa as opções anteriores
                
                if (data && data.id) {  // Verifica se data não é null e possui ID
                    const option = document.createElement('option');
                    option.value = data.id;
                    option.text = `${data.codigo} - ${data.nome} - ${data.uf}`;
                    option.selected = true;
                    municipioSelect.appendChild(option);
                } else {
                    const municipioResult = document.getElementById('municipioResult');
                    municipioResult.innerHTML = 'Município não encontrado.';
                }
            })
            .catch(error => {
                console.error('Erro na pesquisa:', error);
                const municipioResult = document.getElementById('municipioResult');
                municipioResult.innerHTML = 'Erro na pesquisa.';
            });
    }
}