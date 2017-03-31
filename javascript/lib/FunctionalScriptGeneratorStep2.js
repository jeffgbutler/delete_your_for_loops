// this variant shows the use of a higher order function

const XLSX = require("xlsx");

const applicationInformation = [
    {
        columnIndex: 1,
        applicationId: 2237
    },
    {
        columnIndex: 2,
        applicationId: 4352
    },
    {
        columnIndex: 3,
        applicationId: 3657
    },
    {
        columnIndex: 4,
        applicationId: 5565
    }
];

// this is currying
function makeInsertStatementBuilderForUser(userId) {
    return function (applicationId) {
        return getInsertStatement(userId, applicationId);
    };
}

function getInsertStatement(userId, applicationId) {
    return "insert into ApplicationPermission(user_id, application_id) values('"
        + userId
        + "', "
        + applicationId
        + ");";
}

function generate() {
    let workbook = XLSX.readFile("lib/Users.xlsx");
    let worksheet = workbook.Sheets[workbook.SheetNames[0]];
    let sheetData = XLSX.utils.sheet_to_json(worksheet, { header: 1 }); // generate an array of arrays
    return getStatements(sheetData);
}

function getStatements(sheetData) {
    return sheetData
        .filter(row => hasUserId(row))
        .map(row => getStatementsForRow(row))
        .reduce((acc, arr) => acc.concat(arr), []);  // this is a flatten
}

function getStatementsForRow(row) {
    let getInsertStatement = makeInsertStatementBuilderForUser(getUserId(row));
    return applicationInformation
        .filter(ai => hasAuthority(row[ai.columnIndex]))
        .map(ai => getInsertStatement(ai.applicationId));
}

function hasUserId(row) {
    let userId = getUserId(row);
    return userId != undefined && isValidUserId(userId);
}

function getUserId(row) {
    return row[0];
}

function hasAuthority(value) {
    return value === "X";
}

function isValidUserId(value) {
    return value.substring(1, 2) === ".";
}

exports.generate = generate;