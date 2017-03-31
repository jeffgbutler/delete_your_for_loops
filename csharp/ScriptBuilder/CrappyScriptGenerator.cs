using System;
using System.Collections.Generic;
using OfficeOpenXml;

namespace ScriptBuilder
{
    public class CrappyScriptGenerator : IGenerator
    {
        public List<string> generate(ExcelWorksheet sheet)
        {
            int[,] applicationInformation =
            {
                {2, 2237}, {3, 4352}, {4, 3657}, {5, 5565}
            };
            List<string> lines = new List<string>();
            int rows = sheet.Dimension.Rows;

            for (int row = 1; row <= rows; row++)
            {
                object value = sheet.Cells[row, 1].Value;
                if (value != null)
                {
                    string userId = value.ToString();
                    if (userId.Substring(1, 1) == ".")
                    {
                        for (int i = 0; i < applicationInformation.GetLength(0); i++)
                        {
                            value = sheet.Cells[row, applicationInformation[i, 0]].Value;
                            if (value != null)
                            {
                                String cellValue = value.ToString();
                                if (cellValue == "X")
                                {
                                    lines.Add(getInsertStatement(userId, applicationInformation[i, 1]));
                                }
                            }
                        }
                    }
                }
            }

            return lines;
        }

        private string getInsertStatement(string userId, int applicationId)
        {
            return "insert into ApplicationPermission(user_id, application_id) values('"
            + userId
            + "', "
            + applicationId
            + ");";
        }
    }
}
