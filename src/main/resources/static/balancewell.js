google.charts.load('current', {packages: ['corechart', 'line']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {

    var rawData = [];
    $.ajax({
        url: "http://localhost:8080/api/users/get-progress/",
        async: false,
        success: function (result) {
            for(var i =0; i<result.length; i++) {
                var date = new Date(result[i].year, result[i].month, result[i].day);

                rawData.push([date,result[i].weight,createTooltip(result[i].image,date.toDateString(),result[i].weight)]);
            }
        }});

    var data = new google.visualization.DataTable();
    data.addColumn('date', 'X');
    data.addColumn('number', 'Weight');
    data.addColumn({'type': 'string', 'role': 'tooltip', 'p': {'html': true}})
    data.addRows(rawData);

    var columnRange = data.getColumnRange(1);
    var yMin;
    var yMax;
    yMin = columnRange.min - 20;
    yMax = columnRange.max + 20;

    var options = {
        hAxis: {
            title: 'Date of weighing',
            titleTextStyle: {
                color: '#015201',
                fontName: 'IBM Plex Sans',
                bold: true,
                italic: false,
                fontSize: 20
            },
            textStyle: {
                bold: true,
                italic: false,
                fontSize: 16
            }
        },
        vAxis: {
            title: 'Weight in kg',
            viewWindowMode:'explicit',
            format: 'decimal',
            viewWindow: {
                max:yMax,
                min:yMin
            },
            titleTextStyle: {
                color: '#015201',
                fontName: 'IBM Plex Sans',
                bold: true,
                italic: false,
                fontSize: 20
            },
            textStyle: {
                bold: true,
                italic: false,
                fontSize: 16
            }
        },
        colors: ['#FCAB34'],
        pointSize: 10,
        height: 500,
        width: '100%',
        tooltip: {isHtml: true},
        backgroundColor: { fill:'rgba(255,255,255,0.7)' }
    };

    var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
    chart.draw(data, options);

    function createTooltip(image,date,weight) {
        return '<div> <img src="' + image + '" style="width:100%;height: 150px"> <br/>' +
            '<span style="font-size: 12px;"><b>'+date+'</b></span><br/><span style="font-size: 16px;"><b>'+weight+' kgs</b></span> ';
    }
}
