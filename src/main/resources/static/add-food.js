document.addEventListener('DOMContentLoaded', function () {
    $('.foodLink').click(function () {
        var name = $(this).text();
        var meal = $('#mealId').text();
        var date = $('#dateId').text();
        var path = encodeURI("http://localhost:8080/food/getDetails/"+date+ "/"+meal+ "/"+name)
        $.ajax({
            url: path,
            beforeSend: function () {
                $('#foodDetailContainer').hide();
                $("#loaderDiv").show();
            },
            success: function (result) {
                $('#foodDetailContainer').html(result);
            },
            complete: function () { // always executes
                $("#loaderDiv").hide();
                $('#foodDetailContainer').show();
            }
        });
    })

});
