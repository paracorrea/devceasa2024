// Função para buscar município diretamente pelo código
document.getElementById('municipioCodigo')?.addEventListener('input', function () {
    const codigo = this.value.trim(); // Obtém o valor digitado

    // Limpa os campos se o código estiver vazio
    if (codigo === '') {
        document.getElementById('municipioId').value = '';
        document.getElementById('municipioNome').value = '';
        return;
    }

    // Busca o município pelo código
    fetch(`/searchMunicipio?codigo=${codigo}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erro na busca: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data.length > 0) {
                const municipio = data[0]; // Supondo que o backend retorna um array
                document.getElementById('municipioId').value = municipio.id;
                document.getElementById('municipioNome').value = municipio.nome;
				document.getElementById('municipioUF').value = municipio.uf;
				
            } else {
                // Caso não encontre o município
                document.getElementById('municipioId').value = '';
                document.getElementById('municipioNome').value = 'Município não encontrado';
				document.getElementById('municipioUF').value = 'Estado não localizado';
            }
        })
        .catch(error => {
            console.error('Erro ao buscar município:', error);
            document.getElementById('municipioId').value = '';
            document.getElementById('municipioNome').value = 'Erro ao buscar';
			
        });
});
