// Função para buscar município diretamente pelo código no input
document.getElementById('municipioCodigo')?.addEventListener('input', function () {
    const codigo = this.value.trim(); // Obtém o valor do campo de entrada

    // Se o campo estiver vazio, limpar o nome e o ID do município
    if (codigo === '') {
        document.getElementById('municipioNome').value = '';
        document.getElementById('municipioId').value = '';
        return;
    }

    // Fazer a chamada AJAX para buscar o município pelo código
    fetch(`/searchMunicipio?codigo=${codigo}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erro na busca: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data.length > 0) {
                const municipio = data[0]; // Considerando que o retorno é um array
                document.getElementById('municipioNome').value = municipio.nome;
                document.getElementById('municipioId').value = municipio.id; // Preencher o ID
            } else {
                document.getElementById('municipioNome').value = 'Município não encontrado';
                document.getElementById('municipioId').value = ''; // Limpar o ID se não encontrar
            }
        })
        .catch(error => {
            console.error('Erro na busca do município:', error);
            document.getElementById('municipioNome').value = 'Erro ao buscar município';
            document.getElementById('municipioId').value = ''; // Limpar o ID em caso de erro
        });
});
