package com.tms.api.helper;

import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;

import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.poi.sl.usermodel.TextRun.FieldType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class ExcelHelper {
    private static Logger logger = LoggerFactory.getLogger(ExcelHelper.class);

    private static CellStyle createStyleForTitle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    private static CellStyle createStyleForTitle(SXSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    private static CellStyle createNumberCellStyle(SXSSFWorkbook workbook) {
        Font font = workbook.createFont();
		DataFormat fmt = workbook.createDataFormat();
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setDataFormat(fmt.getFormat("#,#"));
        return style;
    }

    public static <TData> byte[] createExcelData(Iterable<TData> listData, Class<TData> dataType) throws TMSException {
        ByteArrayOutputStream fileOut = null;
        List<String> excludeFields = null;
        if (dataType.equals(com.tms.dto.GetDoNewResp.class)) {
                excludeFields = java.util.Arrays.asList("orgId", "warehouseId", "carrierId", "status", "updateby", "createby", "soId", "approvedTime", "expectedDeliveryTime",
                        "expectedArrivalTime", "ffmReturnCode", "ffmReason", "ffmReasonDetail", "lastmileReturnCode", "lastmileReason", "lastmileReasonDetail", "rescueId", "cpName", "category");
        }
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
            Sheet sheet = workbook.createSheet("sheet1");
            int rownum = 0;
            Cell cell;
            Row row;
            row = sheet.createRow(rownum);

            Field[] fields = dataType.getDeclaredFields();
            CellStyle style = createStyleForTitle(workbook);
            CellStyle numberStyle = createNumberCellStyle(workbook);
            int cellnum = 0;
            int type = 0;
            for (Field field : fields) {
                if (excludeFields != null && excludeFields.contains(field.getName()))
                    continue;

                cell = row.createCell(cellnum, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(field.getName());
                field.setAccessible(true);
                cellnum++;

            }

            java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            for (TData data : listData) {
                if (data == null) {
                    continue;
                }

                rownum++;
                row = sheet.createRow(rownum);
                cellnum = 0;

                for (Field field : fields) {
                    if (excludeFields != null && excludeFields.contains(field.getName()))
                        continue;

                    type = 0;
                    if (field.getType().equals(String.class)) {
                        cell = row.createCell(cellnum, CellType.STRING);
                    } else if (field.getType().equals(java.util.Date.class)) {
                        cell = row.createCell(cellnum, CellType.STRING);
                    }
                    else if (field.getType().equals(Double.class)) {
                        cell = row.createCell(cellnum, CellType.NUMERIC);
                    	cell.setCellStyle(numberStyle);
    				} else {
                        type = 1;
                        cell = row.createCell(cellnum, CellType.NUMERIC);
                    }
                    if (field.get(data) != null) {
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
                        //cell.setCellValue("");
                    }
                    cellnum++;
                }
            }
//            sheet.autoSizeColumn(0);
            fileOut = new ByteArrayOutputStream();
            workbook.write(fileOut);
            return fileOut.toByteArray();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new TMSException(ErrorMessage.REPORT_ERROR);
        } finally

        {
            close(fileOut);
        }

    }

    public static <TData> byte[] createExcelDataNew(Iterable<TData> listData, Class<TData> dataType) throws TMSException {
        ByteArrayOutputStream fileOut = null;
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
            Sheet sheet = workbook.createSheet("sheet1");
            int rownum = 0;
            Cell cell;
            Row row;
            row = sheet.createRow(rownum);
            Field[] fields = dataType.getDeclaredFields();
            CellStyle style = createStyleForTitle(workbook);
            int cellnum = 0;
            int type = 0;
            for (Field field : fields) {
                cell = row.createCell(cellnum, CellType.STRING);
                cell.setCellValue(field.getName());
                cell.setCellStyle(style);
                field.setAccessible(true);
                cellnum++;
            }
            for (TData data : listData) {
                if (data == null) {
                    continue;
                }

                rownum++;
                row = sheet.createRow(rownum);
                cellnum = 0;

                for (Field field : fields) {
                    type = 0;
                    if (field.getType().equals(String.class)) {
                        cell = row.createCell(cellnum, CellType.STRING);
                    } else if (field.getType().equals(java.util.Date.class)) {
                        cell = row.createCell(cellnum, CellType.STRING);
                    } else {
                        type = 1;
                        cell = row.createCell(cellnum, CellType.NUMERIC);
                    }
                    if (field.get(data) != null) {
                        if (field.get(data) instanceof String) {
                            cell.setCellValue((String) field.get(data));
                        } else if (field.get(data) instanceof Long) {
                            cell.setCellValue((Long) field.get(data));
                        } else if (field.get(data) instanceof Integer) {
                            cell.setCellValue((Integer) field.get(data));
                        } else if (field.get(data) instanceof Double) {
                            cell.setCellValue((Double) field.get(data));
                        } else {
                            cell.setCellValue(field.get(data).toString());
                        }
                    } else {
                        cell.setCellValue("");
                    }
                    cellnum++;
                }
            }
            sheet.autoSizeColumn(0);
            fileOut = new ByteArrayOutputStream();
            workbook.write(fileOut);
            return fileOut.toByteArray();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new TMSException(ErrorMessage.REPORT_ERROR);
        } finally

        {
            close(fileOut);
        }

    }

    public static void close(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException e) {
            //log the exception
        }
    }
}
