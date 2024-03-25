/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Add header in statistics table to group metrics by category
 * format
 *
 */
function summaryTableHeader(header) {
    var newRow = header.insertRow(-1);
    newRow.className = "tablesorter-no-sort";
    var cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Requests";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 3;
    cell.innerHTML = "Executions";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 7;
    cell.innerHTML = "Response Times (ms)";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Throughput";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 2;
    cell.innerHTML = "Network (KB/sec)";
    newRow.appendChild(cell);
}

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex, headerCreator) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();

    // Call callback is available
    if(headerCreator) {
        headerCreator(header);
    }

    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if(info.overall){
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for(var index=0;index < data.length; index++){
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]): data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if(seriesFilter) {
        regexp = new RegExp(seriesFilter, 'i');
    }
    // Populate body with data.items array
    for(var index=0; index < info.items.length; index++){
        var item = info.items[index];
        if((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
                &&
                (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)){
            if(item.data.length > 0) {
                var newRow = tBody.insertRow(-1);
                for(var col=0; col < item.data.length; col++){
                    var cell = newRow.insertCell(-1);
                    cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
                }
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList : defaultSorts});
}

$(document).ready(function() {

    // Customize table sorter default options
    $.extend( $.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 41.31736526946108, "KoPercent": 58.68263473053892};
    var dataset = [
        {
            "label" : "FAIL",
            "data" : data.KoPercent,
            "color" : "#FF6347"
        },
        {
            "label" : "PASS",
            "data" : data.OkPercent,
            "color" : "#9ACD32"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series : {
            pie : {
                show : true,
                radius : 1,
                label : {
                    show : true,
                    radius : 3 / 4,
                    formatter : function(label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round10(series.percent, -2)
                            + '%</div>';
                    },
                    background : {
                        opacity : 0.5,
                        color : '#000'
                    }
                }
            }
        },
        legend : {
            show : true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.40633019674935844, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.0, 500, 1500, "GET Reserva no Restaurante"], "isController": false}, {"data": [1.0, 500, 1500, "GET Reservas"], "isController": false}, {"data": [0.9789473684210527, 500, 1500, "DELETE Reserva no Restaurante"], "isController": false}, {"data": [0.0, 500, 1500, "GET Nome Restaurantes"], "isController": false}, {"data": [0.0, 500, 1500, "POST Avaliação do Restaurante"], "isController": false}, {"data": [0.973404255319149, 500, 1500, "GET Avaliações"], "isController": false}, {"data": [0.9846938775510204, 500, 1500, "GET Tipo de Cozinha Restaurantes"], "isController": false}, {"data": [0.0, 500, 1500, "POST Restaurantes"], "isController": false}, {"data": [0.0, 500, 1500, "DELETE Restaurantes"], "isController": false}, {"data": [0.0, 500, 1500, "GET Restaurantes"], "isController": false}, {"data": [0.9795918367346939, 500, 1500, "GET Localizacao Restaurantes"], "isController": false}, {"data": [0.0, 500, 1500, "POST Reserva no Restaurante"], "isController": false}]}, function(index, item){
        switch(index){
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 1169, 686, 58.68263473053892, 302.43199315654414, 153, 15909, 165.0, 614.0, 667.0, 1350.3999999999855, 22.86328965382359, 19.60411497041854, 6.442256564150205], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["GET Reserva no Restaurante", 96, 96, 100.0, 195.09375000000009, 155, 883, 164.0, 185.6, 537.3499999999998, 883.0, 3.415276246042193, 2.6930598295919457, 0.8171315237112668], "isController": false}, {"data": ["GET Reservas", 98, 0, 0.0, 177.25510204081638, 157, 449, 166.0, 200.3, 232.05, 449.0, 3.483825097760398, 3.6324097938144333, 0.7722932589761821], "isController": false}, {"data": ["DELETE Reserva no Restaurante", 95, 0, 0.0, 181.54736842105254, 154, 569, 159.0, 188.60000000000002, 319.39999999999674, 569.0, 3.3772974510291873, 2.2952291229691775, 0.8245355105051726], "isController": false}, {"data": ["GET Nome Restaurantes", 98, 98, 100.0, 177.97959183673467, 154, 323, 163.5, 206.2000000000001, 311.05, 323.0, 3.4180879634473857, 2.785234862753305, 0.8044523429597852], "isController": false}, {"data": ["POST Avaliação do Restaurante", 95, 95, 100.0, 204.8842105263158, 156, 946, 165.0, 223.40000000000015, 529.3999999999994, 946.0, 3.3488437676254934, 2.665614920420897, 1.4454970168852228], "isController": false}, {"data": ["GET Avaliações", 94, 0, 0.0, 205.21276595744683, 157, 700, 168.0, 285.5, 544.25, 700.0, 3.3114915803565137, 3.172572416860424, 0.740558175685197], "isController": false}, {"data": ["GET Tipo de Cozinha Restaurantes", 98, 0, 0.0, 194.12244897959187, 155, 959, 161.0, 201.10000000000002, 408.0, 959.0, 3.4732066912390134, 2.8995765921817407, 0.8276000318967961], "isController": false}, {"data": ["POST Restaurantes", 98, 98, 100.0, 174.46938775510205, 153, 440, 161.0, 179.3, 226.24999999999955, 440.0, 3.41951917373251, 2.939739697826163, 1.3357496772392616], "isController": false}, {"data": ["DELETE Restaurantes", 103, 103, 100.0, 1472.7378640776697, 628, 15909, 666.0, 1833.4000000000037, 7682.999999999995, 15836.51999999999, 2.0144729121846274, 1.7413486456092313, 0.4760766061998826], "isController": false}, {"data": ["GET Restaurantes", 98, 98, 100.0, 184.27551020408174, 153, 659, 160.0, 200.20000000000002, 407.5, 659.0, 3.419638495359062, 2.9417505583083257, 0.7213299951148021], "isController": false}, {"data": ["GET Localizacao Restaurantes", 98, 0, 0.0, 211.55102040816328, 154, 1100, 161.5, 218.20000000000206, 463.1499999999996, 1100.0, 3.43690818545276, 3.413139707687452, 0.8458016237637652], "isController": false}, {"data": ["POST Reserva no Restaurante", 98, 98, 100.0, 177.50000000000009, 155, 661, 162.0, 196.3, 235.2, 661.0, 3.5046311196938813, 2.777246942388156, 1.6325283633730285], "isController": false}]}, function(index, item){
        switch(index){
            // Errors pct
            case 3:
                item = item.toFixed(2) + '%';
                break;
            // Mean
            case 4:
            // Mean
            case 7:
            // Median
            case 8:
            // Percentile 1
            case 9:
            // Percentile 2
            case 10:
            // Percentile 3
            case 11:
            // Throughput
            case 12:
            // Kbytes/s
            case 13:
            // Sent Kbytes/s
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0, summaryTableHeader);

    // Create error table
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["404", 686, 100.0, 58.68263473053892], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 1169, 686, "404", 686, "", "", "", "", "", "", "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": ["GET Reserva no Restaurante", 96, 96, "404", 96, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["GET Nome Restaurantes", 98, 98, "404", 98, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["POST Avaliação do Restaurante", 95, 95, "404", 95, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["POST Restaurantes", 98, 98, "404", 98, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["DELETE Restaurantes", 103, 103, "404", 103, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["GET Restaurantes", 98, 98, "404", 98, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": ["POST Reserva no Restaurante", 98, 98, "404", 98, "", "", "", "", "", "", "", ""], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
