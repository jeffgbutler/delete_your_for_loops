// this shows the use of higher order functions.  Now the applicationInformation array
// contains specialized functions for each column.  The makeInsertStatementBuilderForApplication
// returns a new function that is called a partial and then the specialized function is called
// in getStatementsFromRow

const applicationInformation = [
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

// this is a partial (kind of like currying)
function makeInsertStatementBuilderForApplication(applicationId) {
    return function (userId) {
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

function generate(sheetData) {
    return sheetData
        .filter(row => hasValidUserId(row))
        .map(row => getStatementsFromRow(row))
        .reduce((acc, arr) => acc.concat(arr), []);  // this is a flatten
}

function getStatementsFromRow(row) {
    return applicationInformation
        .filter(ai => hasAuthority(row[ai.columnIndex]))
        .map(ai => ai.getInsertStatement(getUserId(row)));
}

function hasValidUserId(row) {
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
