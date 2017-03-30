using System.IO;
using System.Collections.Generic;
using Xunit;

namespace ScriptBuilder.Tests
{
    public class ScriptBuilderTest
    {
        [Fact]
        public void Test1()
        {
            FileInfo file = new FileInfo("Users.xlsx");
            Assert.True(file.Exists, "The file doesn't exist!!!");
            CrappyScriptGenerator generator = new CrappyScriptGenerator();
            List<string> lines = generator.generate(file);
            Assert.Equal(44, lines.Count);
        }
    }
}
