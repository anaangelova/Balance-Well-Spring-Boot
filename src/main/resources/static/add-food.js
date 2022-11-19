document.addEventListener('DOMContentLoaded', function () {
    $('.foodLink').click(function () {
        var name = $(this).text();
        var path = encodeURI("http://localhost:8080/food/getDetails/"+name)
        $.ajax({
            url: path,
            success: function (result) {
                $('#foodDetailContainer').html(result).show();

            }
        });
    })

});
