// add row
$("#addRow").click(function () {

    var html = '';
    html += '<div id="inputFormRow">';
    html += '<div class="input-group mb-3">';
    html += '<input type="text" name="instructions" class="form-control m-input mr-2" placeholder="Enter instruction" autocomplete="off">';
    html += '<div class="input-group-append">';
    html += '<button id="removeRow" type="button" class="btn removeModified p-0">&mdash;</button>';
    html += '</div>';
    html += '</div>';

    $('#newRow').append(html);
});

// remove row
$(document).on('click', '#removeRow', function () {
    $(this).closest('#inputFormRow').remove();
});

$("#addRowIngr").click(function () {



    var html = '';
    html += '<div id="inputFormRow">';
    html += '<div class="input-group mb-3">';
    html += '<input type="number" name="ingredientQuantities" class="form-control m-input mr-2" min="0" placeholder="Quantity">';
    html += '<select name="ingredientMeasurements" class="form-control mr-2">';

    var i;
    for (i = 0; i < modelAttributeValue.length; i++) {
        var itemM = modelAttributeValue[i];
        console.log(itemM);
        var opt = document.createElement("option");
        opt.setAttribute("value", itemM.toString());
        opt.text = itemM.toString();
        console.log(opt.outerHTML);
        html += opt.outerHTML;
    }
    html += '</select>';
    html += '<input type="text" name="ingredientNames" class="form-control m-input mr-2" placeholder="Ingredient name" autocomplete="off">';
    html += '<div class="input-group-append">';
    html += '<button id="removeRow" type="button" class="btn removeModified p-0">&mdash;</button>';
    html += '</div>';
    html += '</div>';

    $('#newRowIngr').append(html);
});

// remove row
$(document).on('click', '#removeRow', function () {
    $(this).closest('#inputFormRow').remove();
});
