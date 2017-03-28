var XLSX = require('xlsx');
var columnToApplicationIdMap = [
    [1, 2237],
    [2, 4352],
    [3, 3657],
    [4, 5565]
];

function getInsertStatement(userId, applicationId) {
    return "insert into ApplicationPermission(user_id, application_id) values('"
        + userId
        + "', "
        + applicationId
        + ");";
}

function generate() {
    let workbook = XLSX.readFile('Users.xlsx');
    let firstSheet = workbook.SheetNames[0];
    let worksheet = workbook.Sheets[firstSheet];
    let sheetData = XLSX.utils.sheet_to_json(worksheet, { header: 1 }); // generate an array of arrays
    sheetData.forEach(function (row) {
        let userId = row[0];
        if (userId !== undefined) {
            if (userId.substring(1, 2) === '.') {
                // valid user id
                columnToApplicationIdMap.forEach(function (appInfo) {
                    let value = row[appInfo[0]];
                    if (value === 'X') {
                        console.log(getInsertStatement(userId, appInfo[1]));
                    }
                });
            }
        }
    });
}

generate();
