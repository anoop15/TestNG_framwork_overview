package com.testcases;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class excelread  {
	private transient Object[][] data;
    String fileName,sheetName;

    public excelread() {

    }

    public excelread(InputStream excelInputStream, String sheetName)
            throws IOException {
        this.data = loadFromSpreadsheet(excelInputStream, sheetName);
    }

    public Object[][] getData() {
        return data;

    }

    Object[][] loadFromSpreadsheet(InputStream excelFile, String sheetName)
            throws IOException {
        // TODO Auto-generated method stub
        HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheet(sheetName);

        int numberOfColumns = countNonEmptyColumns(sheet);
        int numberOfRows = sheet.getLastRowNum() + 1;

        data = new Object[numberOfRows - 1][numberOfColumns - 1];

        for (int rowNum = 1; rowNum < numberOfRows; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (isEmpty(row)) {
                break;
            } else {
                for (int column = 1; column < numberOfColumns; column++) {
                    Cell cell = row.getCell(column);
                    if (cell == null
                            || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                        data[rowNum - 1][column - 1] = "";
                    } else {
                        data[rowNum - 1][column - 1] = objectFrom(workbook,
                                cell);
                    }
                }
            }
        }

        return data;
    }

    private boolean isEmpty(Row row) {
        // TODO Auto-generated method stub
        Cell firstCell = row.getCell(0);
        boolean rowIsEmpty = (firstCell == null)
                || (firstCell.getCellType() == Cell.CELL_TYPE_BLANK);
        return rowIsEmpty;
    }

    /**
     * Count the number of columns, using the number of non-empty cells in the
     * first row.
     */
    private int countNonEmptyColumns(Sheet sheet) {
        // TODO Auto-generated method stub
        Row firstRow = sheet.getRow(0);
        return firstEmptyCellPosition(firstRow);
    }

    private int firstEmptyCellPosition(Row cells) {
        // TODO Auto-generated method stub
        int columnCount = 0;
        for (Cell cell : cells) {
            if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                break;
            }
            columnCount++;
        }
        return columnCount;
    }

    private Object objectFrom(HSSFWorkbook workbook, Cell cell) {
        // TODO Auto-generated method stub
        Object cellValue = null;
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            cellValue = cell.getRichStringCellValue().getString();
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            cellValue = getNumericCellValue(cell);
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            cellValue = cell.getBooleanCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            cellValue = evaluateCellFormula(workbook, cell);
        }

        return cellValue;
    }

    private Object getNumericCellValue(final Cell cell) {
        Object cellValue;
        if (DateUtil.isCellDateFormatted(cell)) {
            cellValue = new Date(cell.getDateCellValue().getTime());
        } else {
            cellValue = cell.getNumericCellValue();
        }
        return cellValue;
    }

    private Object evaluateCellFormula(final HSSFWorkbook workbook,
            final Cell cell) {
        FormulaEvaluator evaluator = workbook.getCreationHelper()
                .createFormulaEvaluator();
        CellValue cellValue = evaluator.evaluate(cell);
        Object result = null;

        if (cellValue.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            result = cellValue.getBooleanValue();
        } else if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            result = cellValue.getNumberValue();
        } else if (cellValue.getCellType() == Cell.CELL_TYPE_STRING) {
            result = cellValue.getStringValue();
        }

        return result;
    }
    public void updateExcel(final InputStream excelFile, String SheetName,
            List<String> list) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet = null;
        if (workbook.getSheetIndex(SheetName) > 0) {
            sheet = workbook.getSheet(SheetName);
            if (list != null && list.size() != sheet.getLastRowNum()) {
                workbook.removeSheetAt(workbook.getSheetIndex(SheetName));
                createSheet(SheetName, workbook, list);
            } else {
                createSheet(SheetName, workbook, list);
            }
        }

    }
    void createSheet(String SheetName, HSSFWorkbook workbook, List<String> list) {
        // TODO Auto-generated method stub
        String[] Heading = {"UserName", "Password",
                "Result" };
        Sheet sheet = workbook.createSheet(SheetName);
        HSSFRow row = null;
        HSSFCell cell = null;

        row = (HSSFRow) sheet.createRow(0);
        for (int cellNum = 0; cellNum < Heading.length; cellNum++) {
            cell = row.createCell(cellNum);
            cell.setCellValue(Heading[cellNum]);
        }   
        for (int rowNum = 1; rowNum <= list.size(); rowNum++) {
            String[] cellVals = {"uname",
                    "pswd", list.get(rowNum - 1) };

            row = (HSSFRow) sheet.createRow(rowNum);
            for (int cellNum = 0; cellNum < cellVals.length; cellNum++) {
                cell = row.createCell(cellNum);
                if (!(cellNum == cellVals.length))
                    cell.setCellValue(cellVals[cellNum]);
                else
                    cell.setCellValue(true);
            }
        }
        try {
            FileOutputStream out = new FileOutputStream("Data//LoginPage.xls");
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
