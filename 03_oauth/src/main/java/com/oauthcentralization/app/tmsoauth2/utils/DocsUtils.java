package com.oauthcentralization.app.tmsoauth2.utils;

import com.itextpdf.text.FontFactory;
import com.oauthcentralization.app.tmsoauth2.model.response.BasesResponse;
import com.oauthcentralization.app.tmsoauth2.utilities.CollectionsUtility;
import com.oauthcentralization.app.tmsoauth2.utilities.StringUtility;
import com.oauthcentralization.app.tmsoauth2.variables.PatternEpochVariable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"all"})
public class DocsUtils {

    private static final Logger logger = LoggerFactory.getLogger(DocsUtils.class);
    private static final int FONT_SIZE_DEFAULT = 12;

    public static CellStyle buildHeaderCellStyle(SXSSFWorkbook workbook) {
        /* set font for header */
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 13);
        font.setFontName(FontFactory.TIMES_ROMAN);
        font.setBold(true);

        /* set style for header */
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(font);
        headerCellStyle.setLocked(true);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        /* commons */
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return headerCellStyle;
    }

    public static CellStyle buildNumberCellStyle(SXSSFWorkbook workbook) {
        /* set font for number cell */
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) FONT_SIZE_DEFAULT);
        font.setFontName(FontFactory.TIMES_ROMAN);
        DataFormat dataFormat = workbook.createDataFormat();

        /* set style for number cell */
        CellStyle numericCellStyle = workbook.createCellStyle();
        numericCellStyle.setFont(font);
        numericCellStyle.setDataFormat(dataFormat.getFormat("#"));
        numericCellStyle.setAlignment(HorizontalAlignment.RIGHT);

        /* commons */
        numericCellStyle.setBorderBottom(BorderStyle.THIN);
        numericCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        numericCellStyle.setBorderLeft(BorderStyle.THIN);
        numericCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        numericCellStyle.setBorderRight(BorderStyle.THIN);
        numericCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        numericCellStyle.setBorderTop(BorderStyle.THIN);
        numericCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return numericCellStyle;
    }

    public static CellStyle buildNumberFloatCellStyle(SXSSFWorkbook workbook) {
        /* set font for number cell */
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) FONT_SIZE_DEFAULT);
        font.setFontName(FontFactory.TIMES_ROMAN);
        DataFormat dataFormat = workbook.createDataFormat();

        /* set style for number cell */
        CellStyle numericCellStyle = workbook.createCellStyle();
        numericCellStyle.setFont(font);
        numericCellStyle.setDataFormat(dataFormat.getFormat("#,#"));
        numericCellStyle.setAlignment(HorizontalAlignment.RIGHT);

        /* commons */
        numericCellStyle.setBorderBottom(BorderStyle.THIN);
        numericCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        numericCellStyle.setBorderLeft(BorderStyle.THIN);
        numericCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        numericCellStyle.setBorderRight(BorderStyle.THIN);
        numericCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        numericCellStyle.setBorderTop(BorderStyle.THIN);
        numericCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return numericCellStyle;
    }

    public static CellStyle buildTextCellStyle(SXSSFWorkbook workbook) {
        /* set font for text cell */
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) FONT_SIZE_DEFAULT);
        font.setFontName(FontFactory.TIMES_ROMAN);

        /* set style for text cell */
        CellStyle textCellStyle = workbook.createCellStyle();
        textCellStyle.setFont(font);
        /* textCellStyle.setWrapText(true); */
        textCellStyle.setAlignment(HorizontalAlignment.LEFT);

        /* commons */
        textCellStyle.setBorderBottom(BorderStyle.THIN);
        textCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        textCellStyle.setBorderLeft(BorderStyle.THIN);
        textCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        textCellStyle.setBorderRight(BorderStyle.THIN);
        textCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        textCellStyle.setBorderTop(BorderStyle.THIN);
        textCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return textCellStyle;
    }

    public static CellStyle buildTimeCellStyle(SXSSFWorkbook workbook) {
        /* set font for text cell */
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) FONT_SIZE_DEFAULT);
        font.setFontName(FontFactory.TIMES_ROMAN);

        /* set style for text cell */
        CellStyle timeCellStyle = workbook.createCellStyle();
        timeCellStyle.setFont(font);
        timeCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        timeCellStyle.setDataFormat(workbook.createDataFormat().getFormat(PatternEpochVariable.SHORT_TIME_BIBLIOGRAPHY_EPOCH_PATTERN));

        /* commons */
        timeCellStyle.setBorderBottom(BorderStyle.THIN);
        timeCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        timeCellStyle.setBorderLeft(BorderStyle.THIN);
        timeCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        timeCellStyle.setBorderRight(BorderStyle.THIN);
        timeCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        timeCellStyle.setBorderTop(BorderStyle.THIN);
        timeCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return timeCellStyle;
    }

    public static CellStyle buildDateCellStyle(SXSSFWorkbook workbook) {
        /* set font for text cell */
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) FONT_SIZE_DEFAULT);
        font.setFontName(FontFactory.TIMES_ROMAN);

        /* set style for text cell */
        CellStyle timeCellStyle = workbook.createCellStyle();
        timeCellStyle.setFont(font);
        timeCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        timeCellStyle.setDataFormat(workbook.createDataFormat().getFormat(PatternEpochVariable.BIBLIOGRAPHY_EPOCH_PATTERN));

        /* commons */
        timeCellStyle.setBorderBottom(BorderStyle.THIN);
        timeCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        timeCellStyle.setBorderLeft(BorderStyle.THIN);
        timeCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        timeCellStyle.setBorderRight(BorderStyle.THIN);
        timeCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        timeCellStyle.setBorderTop(BorderStyle.THIN);
        timeCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return timeCellStyle;
    }

    /**
     * @param rowAccessWindowSize the number of rows that are kept in memory until flushed out.
     * @param dopeAsClass         this is model class {DTO.class}
     * @param checklist           data input
     * @param sheetName           name of sheet
     */
    public static <TData> byte[] buildDocsDope(Iterable<TData> checklist, Class<TData> dopeAsClass, String sheetName, int rowAccessWindowSize) {
        return buildDocsDope(checklist, dopeAsClass, sheetName, false, rowAccessWindowSize);
    }

    /**
     * @param rowAccessWindowSize the number of rows that are kept in memory until flushed out.
     * @param dopeAsClass         this is model class {DTO.class}
     * @param checklist           data
     * @param sheetName           name of sheet
     * @param isSuperClass        : point to support to get all fields
     */
    public static <TData> byte[] buildDocsDope(Iterable<TData> checklist, Class<TData> dopeAsClass, String sheetName, boolean isSuperClass, int rowAccessWindowSize) {
        return buildDocsDope(checklist, dopeAsClass, sheetName, isSuperClass, new ArrayList<>(), rowAccessWindowSize);
    }

    /**
     * @param rowAccessWindowSize the number of rows that are kept in memory until flushed out.
     * @param dopeAsClass         this is model class {DTO.class}
     * @param checklist           data
     * @param sheetName           name of sheet
     * @param isSuperClass        : point to support to get all fields
     */
    public static <TData> byte[] buildDocsDope(
            Iterable<TData> checklist,
            Class<TData> dopeAsClass,
            String sheetName,
            boolean isSuperClass,
            List<String> excludeFields,
            int rowAccessWindowSize) {
        ByteArrayOutputStream outputStream = null;
        Field[] fields;

        if (CollectionsUtility.isEmpty(excludeFields)) {
            excludeFields = new ArrayList<>();
        }

        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat(PatternEpochVariable.SPREADSHEET_BIBLIOGRAPHY_EPOCH_PATTERN);
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(rowAccessWindowSize)) {
            Sheet sheet = workbook.createSheet(sheetName);
            int rowIndex = 0;
            int cellIndex = 0;
            Cell cell;
            Row row;
            row = sheet.createRow(rowIndex);
            if (!isSuperClass) {
                fields = dopeAsClass.getDeclaredFields();
            } else {
                fields = ClassesUtils.toArray(ClassesUtils.snapFieldsIncludingSuperclasses(dopeAsClass));
            }
            CellStyle headerCellStyle = buildHeaderCellStyle(workbook);
            CellStyle numberFloatCellStyle = buildNumberFloatCellStyle(workbook);
            CellStyle numberCellStyle = buildNumberCellStyle(workbook);
            CellStyle textCellStyle = buildTextCellStyle(workbook);
            CellStyle timeCellStyle = buildTimeCellStyle(workbook);
            CellStyle dateCellStyle = buildDateCellStyle(workbook);
            for (Field field : fields) {
                assert false;
                if (CollectionsUtility.isNotEmpty(excludeFields) && excludeFields.contains(field.getName())) {
                    continue;
                }
                cell = row.createCell(cellIndex, CellType.STRING);
                cell.setCellStyle(headerCellStyle);
                cell.setCellValue(StringUtility.camelToSnakeEachWord(field.getName()));
                field.setAccessible(true);
                cellIndex++;
            }

            for (TData data : checklist) {
                if (!ObjectUtils.allNotNull(data)) {
                    continue;
                }
                rowIndex++;
                row = sheet.createRow(rowIndex);
                cellIndex = 0;
                for (Field field : fields) {
                    if (ObjectUtils.allNotNull(excludeFields) && excludeFields.contains(field.getName())) {
                        continue;
                    }
                    if (field.getType().equals(String.class)) {
                        cell = row.createCell(cellIndex, CellType.STRING);
                    } else if (field.getType().equals(java.util.Date.class)) {
                        cell = row.createCell(cellIndex, CellType.STRING);
                    } else if (field.getType().equals(Double.class)) {
                        cell = row.createCell(cellIndex, CellType.NUMERIC);
                    } else {
                        cell = row.createCell(cellIndex, CellType.NUMERIC);
                    }
                    if (ObjectUtils.allNotNull(field.get(data))) {
                        if (field.get(data) instanceof String) {
                            cell.setCellValue((String) field.get(data));
                            cell.setCellStyle(textCellStyle);
                        } else if (field.get(data) instanceof Long) {
                            cell.setCellValue((Long) field.get(data));
                            cell.setCellStyle(numberCellStyle);
                        } else if (field.get(data) instanceof Integer) {
                            cell.setCellValue((Integer) field.get(data));
                            cell.setCellStyle(numberCellStyle);
                        } else if (field.get(data) instanceof Double) {
                            cell.setCellValue((Double) field.get(data));
                            cell.setCellStyle(numberFloatCellStyle);
                        } else if (field.get(data) instanceof java.util.Date) {
                            cell.setCellValue(dateFormat.format(field.get(data)));
                            cell.setCellStyle(dateCellStyle);
                        } else {
                            cell.setCellValue(field.get(data).toString());
                            cell.setCellStyle(textCellStyle);
                        }
                    } else {
                        cell.setCellValue("");
                        cell.setCellStyle(textCellStyle);
                    }
                    cellIndex++;
                }
            }
            outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            return outputStream.toByteArray();
        } catch (Exception error) {
            logger.error("Can not export data!");
            logger.error(error.getMessage(), error);
            return new byte[0];
        } finally {
            if (ObjectUtils.allNotNull(outputStream)) {
                ExceptionUtils.close(outputStream);
            }
        }
    }

    /**
     * @param rowAccessWindowSize the number of rows that are kept in memory until flushed out.
     * @param dopeAsClass         this is model class {DTO.class}
     * @param checklist           data input
     * @param sheetName           name of sheet
     */
    public static <TData> byte[] buildDocsDopeNormal(Iterable<TData> checklist, Class<TData> dopeAsClass, String sheetName, int rowAccessWindowSize) {
        return buildDocsDopeNormal(checklist, dopeAsClass, sheetName, false, rowAccessWindowSize);
    }

    /**
     * @param rowAccessWindowSize the number of rows that are kept in memory until flushed out.
     * @param dopeAsClass         this is model class {DTO.class}
     * @param checklist           data
     * @param sheetName           name of sheet
     * @param isSuperClass        : point to support to get all fields
     */
    public static <TData> byte[] buildDocsDopeNormal(Iterable<TData> checklist, Class<TData> dopeAsClass, String sheetName, boolean isSuperClass, int rowAccessWindowSize) {
        return buildDocsDopeNormal(checklist, dopeAsClass, sheetName, isSuperClass, new ArrayList<>(), rowAccessWindowSize);
    }

    /**
     * @param rowAccessWindowSize the number of rows that are kept in memory until flushed out.
     * @param dopeAsClass         this is model class {DTO.class}
     * @param checklist           data
     * @param sheetName           name of sheet
     * @param isSuperClass        : point to support to get all fields
     */
    public static <TData> byte[] buildDocsDopeNormal(
            Iterable<TData> checklist,
            Class<TData> dopeAsClass,
            String sheetName,
            boolean isSuperClass,
            List<String> excludeFields,
            int rowAccessWindowSize) {
        ByteArrayOutputStream outputStream = null;
        Field[] fields;

        if (CollectionsUtility.isEmpty(excludeFields)) {
            excludeFields = new ArrayList<>();
        }

        if (!dopeAsClass.equals(BasesResponse.class)) {
            excludeFields.add("app");
            excludeFields.add("deleted");
            excludeFields.add("archived");
            excludeFields.add("owner");
        }

        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat(PatternEpochVariable.SPREADSHEET_BIBLIOGRAPHY_EPOCH_PATTERN);
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(rowAccessWindowSize)) {
            Sheet sheet = workbook.createSheet(sheetName);
            int rowIndex = 0;
            int cellIndex = 0;
            Cell cell;
            Row row;
            row = sheet.createRow(rowIndex);
            if (!isSuperClass) {
                fields = dopeAsClass.getDeclaredFields();
            } else {
                fields = ClassesUtils.toArray(ClassesUtils.snapFieldsIncludingSuperclasses(dopeAsClass));
            }
            CellStyle headerCellStyle = buildHeaderCellStyle(workbook);

            for (Field field : fields) {
                assert false;
                if (CollectionsUtility.isNotEmpty(excludeFields) && excludeFields.contains(field.getName())) {
                    continue;
                }
                cell = row.createCell(cellIndex, CellType.STRING);
                cell.setCellStyle(headerCellStyle);
                cell.setCellValue(StringUtility.camelToSnakeEachWord(field.getName()));
                field.setAccessible(true);
                cellIndex++;
            }

            for (TData data : checklist) {
                if (!ObjectUtils.allNotNull(data)) {
                    continue;
                }
                rowIndex++;
                row = sheet.createRow(rowIndex);
                cellIndex = 0;
                for (Field field : fields) {
                    if (ObjectUtils.allNotNull(excludeFields) && excludeFields.contains(field.getName())) {
                        continue;
                    }
                    if (field.getType().equals(String.class)) {
                        cell = row.createCell(cellIndex, CellType.STRING);
                    } else if (field.getType().equals(java.util.Date.class)) {
                        cell = row.createCell(cellIndex, CellType.STRING);
                    } else if (field.getType().equals(Double.class)) {
                        cell = row.createCell(cellIndex, CellType.NUMERIC);
                    } else {
                        cell = row.createCell(cellIndex, CellType.NUMERIC);
                    }
                    if (ObjectUtils.allNotNull(field.get(data))) {
                        if (field.get(data) instanceof String) {
                            cell.setCellValue((String) field.get(data));
                        } else if (field.get(data) instanceof Long) {
                            cell.setCellValue((Long) field.get(data));
                        } else if (field.get(data) instanceof Integer) {
                            cell.setCellValue((Integer) field.get(data));
                        } else if (field.get(data) instanceof Double) {
                            cell.setCellValue((Double) field.get(data));
                        } else if (field.get(data) instanceof java.util.Date) {
                            cell.setCellValue(dateFormat.format(field.get(data)));
                        } else {
                            cell.setCellValue(field.get(data).toString());
                        }
                    } else {
                        cell.setCellValue("");
                    }
                    cellIndex++;
                }
            }
            outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            return outputStream.toByteArray();
        } catch (Exception error) {
            logger.error("Can not export data!");
            logger.error(error.getMessage(), error);
            return new byte[0];
        } finally {
            if (ObjectUtils.allNotNull(outputStream)) {
                ExceptionUtils.close(outputStream);
            }
        }
    }

    public static boolean isRowEmpty(Row row) {
        boolean isEmpty = true;
        DataFormatter dataFormatter = new DataFormatter();

        if (row != null) {
            for (Cell cell : row) {
                if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
                    isEmpty = false;
                    break;
                }
            }
        }

        return isEmpty;
    }

    public static String getCellValue(Cell cell) {
        String value = "";
        if (CellType.NUMERIC.equals(cell)) {
            value = String.valueOf(cell.getNumericCellValue());
        } else if (CellType.STRING.equals(cell)) {
            value = cell.getStringCellValue();
        } else if (CellType.BLANK.equals(cell)) {
            value = "";
        } else if (CellType.BOOLEAN.equals(cell)) {
            value = String.valueOf(cell.getBooleanCellValue());
        } else if (CellType.ERROR.equals(cell)) {
            value = "";
        } else if (CellType.FORMULA.equals(cell)) {

        } else if (CellType._NONE.equals(cell)) {
            value = "";
        }
        return String.valueOf(cell);
    }

}
