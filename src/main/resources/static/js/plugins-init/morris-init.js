//$(function () {
//    "use strict";
//
//    let line = new Morris.Line({
//        element: 'morris-line-chart',
//        resize: true,
//        data: [],
//        xkey: 'month',
//        ykeys: ['total'],
//        labels: ['Total Expenses'],
//        gridColor: 'transparent',
//        lineColors: ['#4d7cff'],
//        lineWidth: 1,
//        hideHover: 'auto',
//    });
//
//    function getCurrentYear() {
//        return new Date().getFullYear();
//    }
//
//    function fetchData(year) {
//        $.ajax({
//            url: `/ajax/chart/${year}`,
//            type: 'POST',
//            dataType: 'json',
//            success: function(response) {
//                // Assuming response is the data you want to display
//                console.log(response);
//                line.setData(response); // Set the data to the chart
//            },
//            error: function(xhr, status, error) {
//                console.error('Error fetching data:', error);
//            }
//        });
//    }
//
//    function initialize() {
//        var year = document.getElementById('yearSelect').value || getCurrentYear();
//        fetchData(year);
//    }
//
//    document.getElementById('yearSelect').addEventListener('change', function() {
//        const selectedYear = this.value;
//        fetchData(selectedYear);
//    });
//
//    initialize();
//});
