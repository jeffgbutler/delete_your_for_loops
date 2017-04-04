using System.Collections.Generic;
using OfficeOpenXml;

namespace ScriptBuilder
{
    public interface IGenerator
    {
        List<string> Generate(ExcelWorksheet sheet);
    }
}
