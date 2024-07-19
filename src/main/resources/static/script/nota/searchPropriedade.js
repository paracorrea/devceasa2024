/**
 * 
 */

document.getElementById('searchCodigoPropriedade').addEventListener('input', function() {
    const query = this.value;
    if (query.length > 2) {
        fetch(`/propriedades/search?query=${query}`)
            .then(response => response.json())
            .then(data => {
                const resultContainer = document.getElementById('propriedadesResult');
                resultContainer.innerHTML = '';
                data.forEach(propriedade => {
                    const div = document.createElement('div');
                    div.textContent = `${propriedade.codigo} - ${propriedade.produto.nome} - ${propriedade.variedade}`;
                    div.onclick = () => {
                        document.getElementById('propriedade').value = propriedade.id;
                        resultContainer.innerHTML = '';
                    };
                    resultContainer.appendChild(div);
                });
            });
    } else {
        document.getElementById('propriedadesResult').innerHTML = '';
    }
});
