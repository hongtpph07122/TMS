package com.tms.api.utils;

import com.tms.api.variable.PatternEpochVariable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class DocsUtils {

    private static final Logger logger = LoggerFactory.getLogger(DocsUtils.class);
    private static final int FONT_SIZE_DEFAULT = 12;

    public static CellStyle buildHeaderCellStyle(SXSSFWorkbook workbook) {
        /* set font for header */
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 13);
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
     * @param checklist -
     * @param sheetName -
     */
    public static <TData> byte[] buildDocsDope(Iterable<TData> checklist, Class<TData> dopeAsClass, String sheetName, int rowAccessWindowSize) {
        ByteArrayOutputStream outputStream = null;
        List<String> excludeFields = Arrays.asList("deleted", "owner", "is_deleted", "limit", "offset", "partnerId", "status", "callStatus", "playbackUrl");

        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat(PatternEpochVariable.SPREADSHEET_BIBLIOGRAPHY_EPOCH_PATTERN);
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(rowAccessWindowSize)) {
            Sheet sheet = workbook.createSheet(sheetName);
            int rowIndex = 0;
            int cellIndex = 0;
            Cell cell;
            Row row;
            row = sheet.createRow(rowIndex);
            Field[] fields = dopeAsClass.getDeclaredFields();
            CellStyle headerCellStyle = buildHeaderCellStyle(workbook);
            CellStyle numberFloatCellStyle = buildNumberFloatCellStyle(workbook);
            CellStyle numberCellStyle = buildNumberCellStyle(workbook);
            CellStyle textCellStyle = buildTextCellStyle(workbook);
            CellStyle timeCellStyle = buildTimeCellStyle(workbook);
            CellStyle dateCellStyle = buildDateCellStyle(workbook);
            for (Field field : fields) {
                assert false;
                if (ObjectUtils.allNotNull(excludeFields) && excludeFields.contains(field.getName())) {
                    continue;
                }
                cell = row.createCell(cellIndex, CellType.STRING);
                cell.setCellStyle(headerCellStyle);
                cell.setCellValue(field.getName());
                field.setAccessible(true);
                cellIndex++;
                /* set filter */
                /*
                for (short i = sheet.getRow(0).getFirstCellNum(),
                     end = sheet.getRow(0).getLastCellNum(); i <= end; i++) {
                    CellRangeAddress cellRangeAddress =
                            new CellRangeAddress(0, rowIndex,
                                    sheet.getRow(0).getFirstCellNum(),
                                    sheet.getRow(0).getLastCellNum());
                    sheet.setAutoFilter(cellRangeAddress);
                }
                 */
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
            return outputStream.toByteArray();
        } catch (Exception error) {
            logger.error(error.getMessage());
            return new byte[0];
        } finally {
            if (ObjectUtils.allNotNull(outputStream)) {
                ExceptionUtils.close(outputStream);
            }
        }
    }
}
