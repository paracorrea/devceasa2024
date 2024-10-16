/**
 * 
 */
function verPropriedades(embalagemId) {
    // Fazer uma requisição AJAX para obter as propriedades associadas
    fetch(`/embalagens/${embalagemId}/propriedades`)
        .then(response => response.json())
        .then(data => {
            const propriedadesList = document.getElementById('propriedadesList');
            propriedadesList.innerHTML = ''; // Limpar lista anterior

            if (data.length === 0) {
                propriedadesList.innerHTML = '<li class="list-group-item">Nenhuma propriedade associada</li>';
            } else {
                data.forEach(propriedade => {
                    const listItem = document.createElement('li');
                    listItem.classList.add('list-group-item');
                    listItem.textContent = `${propriedade.codigo} - ${propriedade.produto.nome}`;
                    propriedadesList.appendChild(listItem);
                });
            }

            // Abrir o modal
            const propriedadesModal = new bootstrap.Modal(document.getElementById('propriedadesModal'));
            propriedadesModal.show();
        })
        .catch(error => console.error('Erro ao carregar propriedades:', error));
}