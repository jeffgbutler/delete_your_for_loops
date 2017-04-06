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
    let lines = [];
    sheetData.forEach(function (row) {
        if (hasValidUserId(row)) {
            getStatementsFromRow(row, lines);
        }
    });
    return lines;
}

function getStatementsFromRow(row, lines) {
    let userId = getUserId(row);
    applicationInformation.forEach(function (appInfo) {
        let cell = row[appInfo.columnIndex];
        if (hasAuthority(cell)) {
            lines.push(getInsertStatement(userId, appInfo.applicationId));
        }
    });
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
