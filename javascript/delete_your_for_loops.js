var XLSX = require('xlsx');

function doIt() {
    var workbook = XLSX.readFile('Users.xlsx');
    var firstSheet = workbook.SheetNames[0];
    var worksheet = workbook.Sheets[firstSheet];
    var sheetData = XLSX.utils.sheet_to_json(worksheet, {header:1}); // generate an array of arrays
    sheetData.forEach(function(row) {
        console.log(row);
    });
}

doIt();
