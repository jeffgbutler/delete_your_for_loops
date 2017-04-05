const Seq = require("immutable").Seq;
const fromJS = require("immutable").fromJS;

// use immutable.js to immplement streams instead of concatenting arrays

const applicationInformation = Seq.of(
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
);

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
    return Seq.of(fromJS(sheetData)) // returns a List of Lists
        .get(0)  // gets the inner lists
        .filter(row => hasValidUserId(row))
        .flatMap(row => getStatementsFromRow(row))
        .toArray();
}


function getStatementsFromRow(row) {
    return applicationInformation
        .filter(ai => hasAuthority(row.get(ai.columnIndex)))
        .map(ai => ai.getInsertStatement(getUserId(row)));
}

function hasValidUserId(row) {
    let userId = getUserId(row);
    return userId != undefined && isValidUserId(userId);
}

function getUserId(row) {
    return row.get(0);
}

function hasAuthority(value) {
    return value === "X";
}

function isValidUserId(value) {
    return value.substring(1, 2) === ".";
}

exports.generate = generate;
