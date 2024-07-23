function searchMunicipios() {
    const nome = document.getElementById('searchNome').value;
    const ibge = document.getElementById('searchIbge').value;
    const uf = document.getElementById('searchUf').value;

    fetch(`/api/municipios?nome=${nome}&ibge=${ibge}&uf=${uf}`)
        .then(response => response.json())
        .then(municipios => {
            const searchResultsContainer = document.getElementById('searchResults');
            searchResultsContainer.innerHTML = '';

            municipios.forEach(municipio => {
                const div = document.createElement('div');
                div.classList.add('search-result-item');
                div.innerHTML = `
                    <p><strong>${municipio.nome}</strong> - ${municipio.uf} (IBGE: ${municipio.ibge})</p>
                    <button type="button" class="btn btn-primary btn-sm" onclick="selectMunicipio('${municipio.ibge}', '${municipio.nome}')">Selecionar</button>
                `;
                searchResultsContainer.appendChild(div);
            });
        })
        .catch(error => {
            console.error('Erro ao buscar municípios:', error);
        });
}

function selectMunicipio(ibge, nome) {
    const municipioSelect = document.getElementById('municipio');
    let optionExists = false;

    for (const option of municipioSelect.options) {
        if (option.value === ibge) {
            option.selected = true;
            optionExists = true;
            break;
        }
    }

    if (!optionExists) {
        const option = document.createElement('option');
        option.value = ibge;
        option.text = nome;
        option.selected = true;
        municipioSelect.appendChild(option);
    }

    console.log(`Município selecionado: ${nome} (IBGE: ${ibge})`);

    // Fechar o modal
    const modalElement = document.getElementById('searchMunicipioModal');
    const modal = bootstrap.Modal.getInstance(modalElement);
    modal.hide();
}