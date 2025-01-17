function searchMunicipio() {
    const nome = document.getElementById('municipioName').value;
    const uf = document.getElementById('uf').value;
    const codigo = document.getElementById('codigoIbge').value;

    fetch(`/searchMunicipio?nome=${nome}&uf=${uf}&codigo=${codigo}`)
        .then(response => response.json())
        .then(data => {
            const resultsBody = document.getElementById('resultsBody');
            resultsBody.innerHTML = '';
            data.forEach(municipio => {
                const row = document.createElement('tr');
                row.innerHTML = `<td>${municipio.nome}</td><td>${municipio.uf}</td><td>${municipio.codigo}</td><td><button type="button" class="btn btn-primary" onclick="selectMunicipio(${municipio.id}, '${municipio.nome}', '${municipio.codigo}')"></button></td>`;
                resultsBody.appendChild(row);
            });
        })
        .catch(error => console.error('Error:', error));
}

function openModal() {
    document.getElementById('municipioSearchModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('municipioSearchModal').style.display = 'none';
}

function selectMunicipio(id, nome, codigo) {
	document.getElementById('municipioId').value = id; // Preenche o ID
	document.getElementById('municipioCodigo').value = codigo;
	
	// Atualiza o campo de nome do município
	 document.getElementById('municipioNome').value = nome;


    closeModal();
}
