$(document).ready(function() {
    let itemIndex = 0;

    function searchPropertyByCode(index) {
        let code = $(`#propertyCode-${index}`).val();
        if (code.length > 0) {
            $.ajax({
                url: '/notas/searchPropertyByCode',
                method: 'GET',
                data: { code: code },
                success: function(data) {
                    if (!data) {
                        $(`#propertyResult-${index}`).text('Nenhuma propriedade encontrada.');
                    } else {
                        $(`#propertyResult-${index}`).text(`${data.codigo} - ${data.produto.nome} ${data.variedade} ${data.subvariedade} ${data.classificacao} ${data.peso} ${data.unidade}`);
                        $(`#propriedadeId-${index}`).val(data.id);
                    }
                },
                error: function() {
                    $(`#propertyResult-${index}`).text('Nenhuma propriedade encontrada.');
                }
            });
        }
    }

    function addItem() {
        itemIndex++;
        const newItem = `
            <div class="item row mb-3">
                <div class="col-md-4 form-group-inline">
                    <label for="propertyCode-${itemIndex}">Código da Propriedade:</label>
                    <input type="text" id="propertyCode-${itemIndex}" class="form-control form-control-small" placeholder="Digite o código da propriedade">
                    <div id="propertyResult-${itemIndex}" class="property-result" readonly></div>
                    <input type="hidden" id="propriedadeId-${itemIndex}" name="itens[${itemIndex}].propriedade.id">
                </div>
                <div class="col-md-2 form-group-inline" style="margin-left: 20px;">
                    <label for="quantidade-${itemIndex}">Quantidade</label>
                    <input type="number" id="quantidade-${itemIndex}" name="itens[${itemIndex}].quantidade" class="form-control form-control-small" step="any" required>
                </div>
                <div class="col-md-3 form-group-inline" style="margin-left: 20px;">
                    <label for="unidadeMedida-${itemIndex}">Unidade de Medida</label>
                    <select id="unidadeMedida-${itemIndex}" name="itens[${itemIndex}].unidadeMedida" class="form-control form-control-small" required>
                        <option value="" disabled selected>Selecione</option>
                        ${$('#unidadeMedida-0').html()}
                    </select>
                </div>
                <div class="col-md-1 d-flex align-items-center" style="margin-left: 20px;">
                    <button type="button" class="btn btn-danger btn-sm remove-item">x</button>
                </div>
            </div>`;
        $('#itensContainer').append(newItem);

        // Bind event to new item
        $(`#propertyCode-${itemIndex}`).on('input', function() {
            searchPropertyByCode(itemIndex);
        });

        updateRemoveButtons();
    }

    function updateRemoveButtons() {
        const items = $('#itensContainer .item');
        items.each(function(index, item) {
            const removeButton = $(item).find('.remove-item');
            removeButton.prop('disabled', items.length === 1);
        });
    }

    // Bind initial events
    $('#propertyCode-0').on('input', function() {
        searchPropertyByCode(0);
    });

    $('.add-item').on('click', function() {
        addItem();
    });

    $('#itensContainer').on('click', '.remove-item', function() {
        $(this).closest('.item').remove();
        updateRemoveButtons();
    });

    updateRemoveButtons();
});
