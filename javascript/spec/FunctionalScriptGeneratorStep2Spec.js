var generator = require("../lib/FunctionalScriptGeneratorStep2");

describe("FunctionalScriptGeneratorStep2", function() {
    let lines = generator.generate();

    it("generates 44 records", function() {
        expect(lines.length).toBe(44);
    })

    it("generates a correct 0th record", function() {
        expect(lines[0]).toBe("insert into ApplicationPermission(user_id, application_id) values('t.wilson', 2237);");
    })

    it("generates a correct 22nd record", function() {
        expect(lines[22]).toBe("insert into ApplicationPermission(user_id, application_id) values('b.walton', 4352);");
    })

    it("generates a correct 43rd record", function() {
        expect(lines[43]).toBe("insert into ApplicationPermission(user_id, application_id) values('e.nash', 5565);");
    })
});
