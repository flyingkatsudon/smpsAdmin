/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package com.blogspot.na5cent.exom;

import com.blogspot.na5cent.exom.util.EachFieldCallback;
import com.blogspot.na5cent.exom.util.ReflectionUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * https://github.com/pramoth/excel-object-mapping
 *
 * @author redcrow
 *         <p>
 *         changed :
 *         1. override map(head position)
 */
public class ExOM {

    private static final Logger LOG = LoggerFactory.getLogger(ExOM.class);

    private final File excelFile;
    private Class clazz;

    private ExOM(File excelFile) {
        this.excelFile = excelFile;
    }

    public static ExOM mapFromExcel(File excelFile) {
        return new ExOM(excelFile);
    }

    public ExOM to(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    private String getValueByName(String name, Row row, Map<String, Integer> cells) {
        if (cells.get(name) == null) {
            return null;
        }

        Cell cell = row.getCell(cells.get(name));
        return getCellValue(cell);
    }

    private void mapName2Index(String name, Row row, Map<String, Integer> cells) {
        int index = findIndexCellByName(name, row);
        if (index != -1) {
            cells.put(name, index);
        }
    }

    private void readExcelHeader(final Row row, final Map<String, Integer> cells) throws Throwable {
        ReflectionUtils.eachFields(clazz, new EachFieldCallback() {

            @Override
            public void each(Field field, String name) throws Throwable {
                mapName2Index(name, row, cells);
            }
        });
    }

    private Object readExcelContent(final Row row, final Map<String, Integer> cells) throws Throwable {
        final Object instance = clazz.newInstance();
        ReflectionUtils.eachFields(clazz, new EachFieldCallback() {

            @Override
            public void each(Field field, String name) throws Throwable {
                ReflectionUtils.setValueOnField(instance, field, getValueByName(
                        name,
                        row,
                        cells
                ));
            }
        });

        return instance;
    }

    private boolean isVersion2003(File file) {
        return file.getName().endsWith(".xls");
    }

    private Workbook createWorkbook(InputStream inputStream) throws IOException {
        if (isVersion2003(excelFile)) {
            return new HSSFWorkbook(inputStream);
        } else { //2007+
            return new XSSFWorkbook(inputStream);
        }
    }

    public <T> List<T> map() throws Throwable {
        return map(0);
    }

    public <T> List<T> map(int posHeader) throws Throwable {
        InputStream inputStream = null;
        List<T> items = new LinkedList<>();

        try {
            Iterator<Row> rowIterator;
            inputStream = new FileInputStream(excelFile);
            Workbook workbook = createWorkbook(inputStream);
            int numberOfSheets = workbook.getNumberOfSheets();

            for (int index = 0; index < numberOfSheets; index++) {
                Sheet sheet = workbook.getSheetAt(index);
                rowIterator = sheet.iterator();

                Map<String, Integer> nameIndexMap = new HashMap<>();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    if (row.getRowNum() == posHeader) {
                        readExcelHeader(row, nameIndexMap);
                    } else if (row.getRowNum() >= posHeader) {
                        items.add((T) readExcelContent(row, nameIndexMap));
                    }
                }
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return items;
    }

    private int findIndexCellByName(String name, Row row) {
        Iterator<Cell> iterator = row.cellIterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            if (getCellValue(cell).trim().equalsIgnoreCase(name)) {
                return cell.getColumnIndex();
            }
        }

        return -1;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        String value = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                value += String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (!HSSFDateUtil.isCellDateFormatted(cell)) {
                    value += new BigDecimal(cell.getNumericCellValue()).toString();
                } else {
                    Date date = cell.getDateCellValue();
                    value += new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                }
                break;
            case Cell.CELL_TYPE_STRING:
                value += cell.getStringCellValue();
                break;
        }

        return value;
    }
}
