document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        navLinks: true,
        navLinkDayClick: function(date) {
            $.ajax({
                url: "http://localhost:8080/api/calendar/logDay?date="+date.toISOString(),
                success: function (result) {
                    const logDay = JSON.parse(result);
                    console.log(logDay);
                    $('#diaryInfo')
                        .append('<span>'+logDay.dateForDay+'</span></br>')
                        .append('<span>Target calories: '+logDay.targetCalories+'</span></br>')
                        .append('<span>Total calories: '+logDay.totalCalories+'</span></br>')
                        .show()
                    ;

                }});
        }
    });
    calendar.render();
});
