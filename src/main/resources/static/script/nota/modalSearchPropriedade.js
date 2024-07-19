/**
 * 
 */
function searchPropriedades() {
    const codigo = document.getElementById('searchCodigo').value;
    const produto = document.getElementById('searchProduto').value;
    const nome = document.getElementById('searchNome').value;
    fetch(`/propriedades/search?codigo=${codigo}&produto=${produto}&nome=${nome}`)
        .then(response => response.json())
        .then(data => {
            const resultContainer = document.getElementById('searchPropriedadesResults');
            resultContainer.innerHTML = '';
            data.forEach(propriedade => {
                const div = document.createElement('div');
                div.textContent = `${propriedade.codigo} - ${propriedade.produto.nome} - ${propriedade.variedade}`;
                div.onclick = () => {
                    document.getElementById('propriedade').value = propriedade.id;
                    resultContainer.innerHTML = '';
                    const modal = bootstrap.Modal.getInstance(document.getElementById('searchPropriedadeModal'));
                    modal.hide();
                };
                resultContainer.appendChild(div);
            });
        });
}
