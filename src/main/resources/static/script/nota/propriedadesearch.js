function searchPropriedades(itemIndex) {
    const query = document.getElementById(`searchPropriedade-${itemIndex}`).value.toLowerCase();
    const select = document.getElementById(`propriedade-${itemIndex}`);
    const options = select.options;

    for (let i = 1; i < options.length; i++) {
        const text = options[i].text.toLowerCase();
        if (text.includes(query)) {
            options[i].style.display = '';
        } else {
            options[i].style.display = 'none';
        }
    }
}