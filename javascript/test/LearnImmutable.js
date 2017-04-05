const Immutable = require("immutable");
const expect = require("chai").expect;

describe("Should add a list of integers", function () {
    var sum = Immutable.Seq.of(1, 2, 3, 4)
        .reduce((l, r) => l + r);

    it("sums a list correctly", function () {
        expect(sum).to.equal(10);
    })
});

describe("Should square and add a list of integers", function () {
    var sum = Immutable.Seq.of(1, 2, 3, 4)
        .map(x => x * x)
        .reduce((l, r) => l + r);

    it("sums a list correctly", function () {
        expect(sum).to.equal(30);
    })
});

describe("Should square and add a list of even integers", function () {
    var sum = Immutable.Seq.of(1, 2, 3, 4)
        .filter(x => x % 2 == 0)
        .map(x => x * x)
        .reduce((l, r) => l + r);

    it("sums a list correctly", function () {
        expect(sum).to.equal(20);
    })
});

describe("Should add an array of integers", function () {
    var myNumbers = [1, 2, 3, 4];
    var sum = Immutable.Seq.of(Immutable.fromJS(myNumbers))
        .flatten()
        .reduce((l, r) => l + r);

    it("sums a list correctly", function () {
        expect(sum).to.equal(10);
    })
});

describe("Should add an array of integers", function () {
    var myNumbers = [[1, 5], [2, 10], [3, 15], [4, 20]];
    var sum = Immutable.Seq.of(Immutable.fromJS(myNumbers))
        .get(0)
        .map(row => row.get(0) + row.get(1))
        .reduce((l, r) => l + r);

    it("sums a list correctly", function () {
        expect(sum).to.equal(60);
    })
});
