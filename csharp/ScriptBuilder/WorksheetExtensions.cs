using OfficeOpenXml;
public static class WorksheetExtensions
{
    public static string[,] ToStringArray(this ExcelWorksheet sheet)
    {
        string[,] answer = new string[sheet.Dimension.Rows, sheet.Dimension.Columns];
        for (var rowNumber = 1; rowNumber <= sheet.Dimension.Rows; rowNumber++)
        {
            for (var columnNumber = 1; columnNumber <= sheet.Dimension.Columns; columnNumber++)
            {
                var cell = sheet.Cells[rowNumber, columnNumber].Value;
                if (cell == null)
                {
                    answer[rowNumber -1, columnNumber -1] = "";
                }
                else
                {
                    answer[rowNumber - 1, columnNumber - 1] = cell.ToString();
                }
            }

        }
        return answer;
    }
}
