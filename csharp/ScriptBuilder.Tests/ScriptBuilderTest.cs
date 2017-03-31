using System.IO;
using System.Collections.Generic;
using Xunit;

namespace ScriptBuilder.Tests
{
    public class ScriptBuilderTest
    {
        [Fact]
        public void TestCrappyScriptGenerator()
        {
            TestGenerator(new CrappyScriptGenerator());
        }

        private void TestGenerator(IGenerator generator)
        {
            FileInfo file = new FileInfo("Users.xlsx");
            Assert.True(file.Exists, "The file doesn't exist!!!");
            List<string> lines = generator.generate(file);
            Assert.Equal(44, lines.Count);
            Assert.Equal(lines[0], "insert into ApplicationPermission(user_id, application_id) values('t.wilson', 2237);");
            Assert.Equal(lines[22], "insert into ApplicationPermission(user_id, application_id) values('b.walton', 4352);");
            Assert.Equal(lines[43], "insert into ApplicationPermission(user_id, application_id) values('e.nash', 5565);");
        }
    }
}
