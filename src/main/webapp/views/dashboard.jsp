<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/highcharts-3d.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script src="https://code.highcharts.com/modules/export-data.js"></script>
<script src="https://code.highcharts.com/modules/accessibility.js"></script>
<style>
    #container{
        width:600px;
        height:600px;
        border:2px solid blue;
    }
</style>
<script>
    let dashboard = {
        init:function(){
            $.ajax({
                url:'/chart',
                success:function(result){
                    dashboard.display(result);
                }
            });
        },
        display:function(result){
            Highcharts.setOptions({
                colors: Highcharts.getOptions().colors.map(function (color) {
                    return {
                        radialGradient: {
                            cx: 0.4,
                            cy: 0.3,
                            r: 0.5
                        },
                        stops: [
                            [0, color],
                            [1, Highcharts.color(color).brighten(-0.2).get('rgb')]
                        ]
                    };
                })
            });

// Set up the chart
            var chart = new Highcharts.Chart({
                chart: {
                    renderTo: 'container',
                    margin: 100,
                    type: 'scatter3d',
                    animation: false,
                    options3d: {
                        enabled: true,
                        alpha: 10,
                        beta: 30,
                        depth: 250,
                        viewDistance: 5,
                        fitToPlot: false,
                        frame: {
                            bottom: { size: 1, color: 'rgba(0,0,0,0.02)' },
                            back: { size: 1, color: 'rgba(0,0,0,0.04)' },
                            side: { size: 1, color: 'rgba(0,0,0,0.06)' }
                        }
                    }
                },
                title: {
                    text: 'Draggable box'
                },
                subtitle: {
                    text: 'Click and drag the plot area to rotate in space'
                },
                plotOptions: {
                    scatter: {
                        width: 10,
                        height: 10,
                        depth: 10
                    }
                },
                yAxis: {
                    min: 0,
                    max: 10,
                    title: null
                },
                xAxis: {
                    min: 0,
                    max: 10,
                    gridLineWidth: 1
                },
                zAxis: {
                    min: 0,
                    max: 10,
                    showFirstLabel: false
                },
                legend: {
                    enabled: false
                },
                series: [{
                    name: 'Data',
                    colorByPoint: true,
                    accessibility: {
                        exposeAsGroupOnly: true
                    },
                    data: result
                }]
            });


// Add mouse and touch events for rotation
            (function (H) {
                function dragStart(eStart) {
                    eStart = chart.pointer.normalize(eStart);

                    var posX = eStart.chartX,
                        posY = eStart.chartY,
                        alpha = chart.options.chart.options3d.alpha,
                        beta = chart.options.chart.options3d.beta,
                        sensitivity = 5,  // lower is more sensitive
                        handlers = [];

                    function drag(e) {
                        // Get e.chartX and e.chartY
                        e = chart.pointer.normalize(e);

                        chart.update({
                            chart: {
                                options3d: {
                                    alpha: alpha + (e.chartY - posY) / sensitivity,
                                    beta: beta + (posX - e.chartX) / sensitivity
                                }
                            }
                        }, undefined, undefined, false);
                    }

                    function unbindAll() {
                        handlers.forEach(function (unbind) {
                            if (unbind) {
                                unbind();
                            }
                        });
                        handlers.length = 0;
                    }

                    handlers.push(H.addEvent(document, 'mousemove', drag));
                    handlers.push(H.addEvent(document, 'touchmove', drag));


                    handlers.push(H.addEvent(document, 'mouseup', unbindAll));
                    handlers.push(H.addEvent(document, 'touchend', unbindAll));
                }
                H.addEvent(chart.container, 'mousedown', dragStart);
                H.addEvent(chart.container, 'touchstart', dragStart);
            }(Highcharts));
        }
    };
    $(function(){
        dashboard.init();
    })
</script>


<div class="d-sm-flex align-items-center justify-content-between mb-4">
    <h1 class="h3 mb-0 text-gray-800">Dashboard</h1>
    <a href="#" class="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm"><i
            class="fas fa-download fa-sm text-white-50"></i> Generate Report</a>
</div>
<div id="container"></div>

