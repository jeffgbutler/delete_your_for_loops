using System;
using System.Collections.Generic;
using System.IO;
using OfficeOpenXml;

namespace delete_your_for_loops
{
    class CrappyScriptGenerator
    {
        static void Main(string[] args)
        {
            int[,] applicationInformation =
            {
                {2, 2257}, {3, 4352}, {4, 3657}, {5, 5565}
            };
            List<string> lines = new List<string>();

            FileInfo file = new FileInfo("Users.xlsx");
            ExcelPackage package = new ExcelPackage(file);
            ExcelWorksheet sheet = package.Workbook.Worksheets[1];
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

            Console.WriteLine(lines.Count + " lines generated");
            foreach(string line in lines)
            {
                Console.WriteLine(line);
            }
        }

        private static string getInsertStatement(string userId, int applicationId)
        {
            return "insert into ApplicationPermission(user_id, application_id) values('"
            + userId
            + "', "
            + applicationId
            + ");";
        }
    }
}
