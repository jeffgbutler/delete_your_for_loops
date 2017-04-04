using System;
using System.Collections.Generic;
using OfficeOpenXml;

namespace ScriptBuilder
{
    public class CrappyScriptGenerator : IGenerator
    {
        private int[,] ApplicationInformation =
        {
            {2, 2237}, {3, 4352}, {4, 3657}, {5, 5565}
        };

        private string GetInsertStatement(string userId, int applicationId)
        {
            return "insert into ApplicationPermission(user_id, application_id) values('"
            + userId
            + "', "
            + applicationId
            + ");";
        }

        public List<string> Generate(ExcelWorksheet sheet)
        {
            List<string> lines = new List<string>();
            int rows = sheet.Dimension.Rows;

            for (int row = 1; row <= rows; row++)
            {
                var value = sheet.Cells[row, 1].Value;
                if (value != null)
                {
                    string userId = value.ToString();
                    if (userId.Substring(1, 1) == ".")
                    {
                        for (int i = 0; i < ApplicationInformation.GetLength(0); i++)
                        {
                            value = sheet.Cells[row, ApplicationInformation[i, 0]].Value;
                            if (value != null)
                            {
                                String cellValue = value.ToString();
                                if (cellValue == "X")
                                {
                                    lines.Add(GetInsertStatement(userId, ApplicationInformation[i, 1]));
                                }
                            }
                        }
                    }
                }
            }

            return lines;
        }
    }
}
