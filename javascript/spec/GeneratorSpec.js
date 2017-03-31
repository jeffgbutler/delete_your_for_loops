const crappyGenerator = require("../lib/CrappyScriptGenerator");
const functionalGenerator1 = require("../lib/FunctionalScriptGeneratorStep1");
const functionalGenerator2 = require("../lib/FunctionalScriptGeneratorStep2");
const functionalGenerator3 = require("../lib/FunctionalScriptGeneratorStep3");

describe("CrappyGenerator", function () {
    let lines = crappyGenerator.generate();
    describeGenerator(lines);
});

describe("FunctionalScriptGeneratorStep1", function () {
    let lines = functionalGenerator1.generate();
    describeGenerator(lines);
});

describe("FunctionalScriptGeneratorStep2", function () {
    let lines = functionalGenerator2.generate();
    describeGenerator(lines);
});

describe("FunctionalScriptGeneratorStep3", function () {
    let lines = functionalGenerator3.generate();
    describeGenerator(lines);
});

function describeGenerator(lines) {
    describe("FunctionalScriptGeneratorStep3", function () {
        it("generates 44 records", function () {
            expect(lines.length).toBe(44);
        });

        it("generates a correct 0th record", function () {
            expect(lines[0]).toBe("insert into ApplicationPermission(user_id, application_id) values('t.wilson', 2237);");
        });

        it("generates a correct 22nd record", function () {
            expect(lines[22]).toBe("insert into ApplicationPermission(user_id, application_id) values('b.walton', 4352);");
        });

        it("generates a correct 43rd record", function () {
            expect(lines[43]).toBe("insert into ApplicationPermission(user_id, application_id) values('e.nash', 5565);");
        });
    });
}