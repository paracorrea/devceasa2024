/**
 * 
 */
$(document).ready(function() {
    $('#propertyCode').on('input', function() {
        let code = $(this).val();
        if (code.length > 0) {
            $.ajax({
                url: '/searchPropertyByCode',
                method: 'GET',
                data: { code: code },
                success: function(data) {
                    if (!data) {
                        $('#searchResults').html('<p>Nenhuma propriedade encontrada.</p>');
                        $('#selectPropertyButton').prop('disabled', true);
                    } else {
                        $('#selectedPropertyId').val(data.id);
                        $('#searchResults').html('<p>Propriedade encontrada: ' + data.codigo + ' - ' + data.produto.nome + ' ' + data.variedade + ' ' + data.subvariedade + '</p>');
                        $('#selectPropertyButton').prop('disabled', false);
                    }
                },
                error: function() {
                    $('#searchResults').html('<p>Nenhuma propriedade encontrada.</p>');
                    $('#selectPropertyButton').prop('disabled', true);
                }
            });
        }
    });

    $('#searchProductButton').click(function() {
        let productName = $('#productName').val();
        if (productName.length > 0) {
            $.ajax({
                url: '/searchPropertyByProductName',
                method: 'GET',
                data: { productName: productName },
                success: function(data) {
                    if (data.length === 0) {
                        $('#searchResults').html('<p>Nenhuma propriedade encontrada.</p>');
                    } else {
                        let resultsHtml = '<ul>';
                        data.forEach(function(item) {
                            resultsHtml += '<li><a href="#" class="select-property" data-id="' + item.id + '">' + item.codigo + ' - ' + item.produto.nome + ' ' + item.variedade + ' ' + item.subvariedade + '</a></li>';
                        });
                        resultsHtml += '</ul>';
                        $('#searchResults').html(resultsHtml);

                        $('.select-property').click(function(e) {
                            e.preventDefault();
                            $('#selectedPropertyId').val($(this).data('id'));
                            $('#searchResults').html('<p>Propriedade selecionada: ' + $(this).text() + '</p>');
                            $('#selectPropertyButton').prop('disabled', false);
                        });
                    }
                },
                error: function() {
                    $('#searchResults').html('<p>Nenhuma propriedade encontrada.</p>');
                }
            });
        }
    });
});
