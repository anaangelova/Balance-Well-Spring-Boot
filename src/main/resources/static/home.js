document.addEventListener('DOMContentLoaded', function () {
    populateLogDayInitially();
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        navLinks: true,
        headerToolbar: {
            start: 'title',
            center: '',
            end: ''
        },
        footerToolbar: {
            start: '',
            center: 'prev,next',
            end: ''
        },
        navLinkDayClick: function (date) {
            $.ajax({
                url: "http://localhost:8080/calendar/logDay?date=" + date.toISOString(),
                success: function (result) {
                    //const logDay = JSON.parse(result);
                    $('#diaryInfo').html(result).show();

                }
            });
        }
    });
    calendar.render();

    function populateLogDayInitially() {
        $.ajax({
            url: "http://localhost:8080/calendar/logDay?date=" + new Date().toISOString(),
            success: function (result) {
                $('#diaryInfo').html(result).show();
            }
        });
    }
});
