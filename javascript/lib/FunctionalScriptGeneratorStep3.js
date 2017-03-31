// this shows a more interesting use of higher order functions.  Now the applicationInformation array
// contains specialized functions for each column.  The makeInsertStatementBuilder... curried function
// has been reversed and then the specialized function is called in getStatementsForRow

var XLSX = require('xlsx');

exports.generate = function () {
    var applicationInformation = [
        {
            columnIndex: 1,
            getInsertStatement: makeInsertStatementBuilderForApplication(2237)
        },
        {
            columnIndex: 2,
            getInsertStatement: makeInsertStatementBuilderForApplication(4352)
        },
        {
            columnIndex: 3,
            getInsertStatement: makeInsertStatementBuilderForApplication(3657)
        },
        {
            columnIndex: 4,
            getInsertStatement: makeInsertStatementBuilderForApplication(5565)
        }
    ];

    // this is currying
    function makeInsertStatementBuilderForApplication(applicationId) {
        return function (userId) {
            return getInsertStatement(userId, applicationId);
        }
    }

    function getInsertStatement(userId, applicationId) {
        return "insert into ApplicationPermission(user_id, application_id) values('"
            + userId
            + "', "
            + applicationId
            + ");";
    }

    function getStatements(sheetData) {
        return sheetData
            .filter(row => hasUserId(row))
            .map(row => getStatementsForRow(row))
            .reduce((acc, arr) => acc.concat(arr), []);  // this is a flatten
    }

    function getStatementsForRow(row) {
        return applicationInformation
            .filter(ai => hasAuthority(row[ai.columnIndex]))
            .map(ai => ai.getInsertStatement(getUserId(row)));
    }

    function hasUserId(row) {
        let userId = getUserId(row);
        return userId != undefined && isValidUserId(userId);
    }

    function getUserId(row) {
        return row[0];
    }

    function hasAuthority(value) {
        return value === 'X';
    }

    function isValidUserId(value) {
        return value.substring(1, 2) === '.';
    }

    let workbook = XLSX.readFile('lib/Users.xlsx');
    let worksheet = workbook.Sheets[workbook.SheetNames[0]];
    let sheetData = XLSX.utils.sheet_to_json(worksheet, { header: 1 }); // generate an array of arrays
    return getStatements(sheetData);
}
