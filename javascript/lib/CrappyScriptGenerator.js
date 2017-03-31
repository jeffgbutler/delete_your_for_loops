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
        let userId = row[0];
        if (userId !== undefined) {
            if (userId.substring(1, 2) === ".") {
                // valid user id
                applicationInformation.forEach(function (appInfo) {
                    let value = row[appInfo.columnIndex];
                    if (value === "X") {
                        lines.push(getInsertStatement(userId, appInfo.applicationId));
                    }
                });
            }
        }
    });
    return lines;
}

exports.generate = generate;
