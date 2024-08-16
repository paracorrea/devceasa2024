$(document).ready(function() {
    $('#propertyCode').on('input', function() {
        let code = $(this).val().trim();
        if (code.length > 0) {
            $.ajax({
                url: '/notas/searchPropertyByCode',
                method: 'GET',
                data: { code: code },
                success: function(data) {
                    if (!data) {
                        $('#searchResults').html('<p>Nenhuma propriedade encontrada.</p>');
                    } else {
                        $('#propriedadeId').val(data.id); // Atualize o campo hidden com o ID da propriedade
                        $('#searchResults').html(
                            '<p>Propriedade encontrada: ' + data.codigo + ' - ' +
                            data.produto.nome + ' ' + data.variedade + ' ' +
                            data.subvariedade + ' ' + data.classificacao + ' ' +
                            data.peso + ' ' + data.unidade + '</p>'
                        );
                        $('#selectPropertyButton').prop('disabled', false);
                    }
                },
                error: function() {
                    $('#searchResults').html('<p>Nenhuma propriedade encontrada.</p>');
                }
            });
        }
    });

    $('#searchProductButton').click(function() {
        let productName = $('#productName').val().trim();
        if (productName.length > 0) {
            $.ajax({
                url: '/notas/searchPropertyByProductName',
                method: 'GET',
                data: { productName: productName },
                success: function(data) {
                    if (data.length === 0) {
                        $('#searchResults').html('<p>Nenhuma propriedade encontrada.</p>');
                    } else {
                        let resultsHtml = '<ul>';
                        data.forEach(function(item) {
                            resultsHtml += '<li><a href="#" class="select-property" data-id="' + item.id + '">' +
                                item.codigo + ' - ' + item.produto.nome + ' ' + item.variedade + ' ' +
                                item.subvariedade + ' ' + item.classificacao + ' ' + item.peso + ' ' + item.unidade +
                                '</a></li>';
                        });
                        resultsHtml += '</ul>';
                        $('#searchResults').html(resultsHtml);

                        $('.select-property').click(function(e) {
                            e.preventDefault();
                            $('#propriedadeId').val($(this).data('id')); // Set the hidden input value
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
