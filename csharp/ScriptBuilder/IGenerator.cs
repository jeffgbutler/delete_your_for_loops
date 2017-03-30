using System.Collections.Generic;
using System.IO;

namespace ScriptBuilder
{
    public interface IGenerator
    {
        List<string> generate(FileInfo file);
    }
}
