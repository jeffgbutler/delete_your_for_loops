// this variant changes all the for loops into map/filter/reduce

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
    // we can assume there is a valid userId here because invalids are filtered out above
    return applicationInformation
        .filter(ai => hasAuthority(row[ai.columnIndex]))
        .map(ai => getInsertStatement(getUserId(row), ai.applicationId));
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
