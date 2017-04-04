const XLSX = require("xlsx");
const expect = require("chai").expect;
const crappyGenerator = require("../lib/CrappyScriptGenerator");
const functionalGenerator1 = require("../lib/FunctionalScriptGeneratorStep1");
const functionalGenerator2 = require("../lib/FunctionalScriptGeneratorStep2");
const functionalGenerator3 = require("../lib/FunctionalScriptGeneratorStep3");

describe("CrappyGenerator", function () {
    let lines = crappyGenerator.generate(getSheetData());
    describeGenerator(lines);
});

describe("FunctionalScriptGeneratorStep1", function () {
    let lines = functionalGenerator1.generate(getSheetData());
    describeGenerator(lines);
});

describe("FunctionalScriptGeneratorStep2", function () {
    let lines = functionalGenerator2.generate(getSheetData());
    describeGenerator(lines);
});

describe("FunctionalScriptGeneratorStep3", function () {
    let lines = functionalGenerator3.generate(getSheetData());
    describeGenerator(lines);
});

function getSheetData() {
    let workbook = XLSX.readFile("test/Users.xlsx");
    let worksheet = workbook.Sheets[workbook.SheetNames[0]];
    return XLSX.utils.sheet_to_json(worksheet, { header: 1 }); // generate an array of arrays
}

function describeGenerator(lines) {
    describe("FunctionalScriptGeneratorStep3", function () {
        it("generates 44 records", function () {
            expect(lines.length).to.equal(44);
        });

        it("generates a correct 0th record", function () {
            expect(lines[0]).to.equal("insert into ApplicationPermission(user_id, application_id) values('t.wilson', 2237);");
        });

        it("generates a correct 22nd record", function () {
            expect(lines[22]).to.equal("insert into ApplicationPermission(user_id, application_id) values('b.walton', 4352);");
        });

        it("generates a correct 43rd record", function () {
            expect(lines[43]).to.equal("insert into ApplicationPermission(user_id, application_id) values('e.nash', 5565);");
        });
    });
}
