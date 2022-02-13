package com.tms.api.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tms.entity.log.InsCLFreshV11;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tms.api.helper.Const;
import com.tms.api.helper.EnumType;
import com.tms.api.helper.RedisHelper;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.UploadService;
import com.tms.api.task.DBLogWriter;
import com.tms.dto.DBResponse;
import com.tms.dto.GetProductParams;
import com.tms.entity.CLFresh;
import com.tms.entity.PDProduct;
import com.tms.service.impl.CLProductService;
import com.tms.service.impl.LogService;

@Service
public class UploadServiceImpl implements UploadService {

    @SuppressWarnings("deprecation")
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        cell.setCellType(CellType.STRING);
        switch (cell.getCellType()) {
        case STRING:
            return cell.getStringCellValue();
        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                return dateFormat.format(date);
            } else {
                return cell.getNumericCellValue() + "";
            }
        case FORMULA:
            return cell.getCellFormula();
        case BLANK:
            return "";
        default:
            return "";
        }
    }

    public static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK)
                return false;
        }
        return true;
    }

    @Autowired
    LogService logService;

    @Autowired
    DBLogWriter dbLog;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    CLProductService clProductService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public TMSResponse<?> createLeadByExcel(@RequestParam("file") MultipartFile file, @PathVariable Integer cpId,
            @PathVariable Integer clId, String SESSION_ID, int userId, int orgId) throws Exception {

        int count = 0;
        StringBuilder errorSb = new StringBuilder();
        try {
            Workbook book = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = book.getSheetAt(0);
            Map<String, Integer> headerIndexMap = new HashMap<String, Integer>();
            Row headerRow = sheet.getRow(0);
            short minColIx = headerRow.getFirstCellNum();
            short maxColIx = headerRow.getLastCellNum();
            for (short colIx = minColIx; colIx < maxColIx; colIx++) { // loop from first to last index
                Cell cell = headerRow.getCell(colIx);
                headerIndexMap.put(cell.getStringCellValue().toLowerCase(), cell.getColumnIndex());
            }

            List<InsCLFreshV11> clFreshList = new ArrayList<>();
            GetProductParams getProductParams = new GetProductParams();
            getProductParams.setOrgId(orgId);

            for (Row dataRow : sheet) {
                int rowNum = dataRow.getRowNum();

                if (rowNum == 0) {
                    continue;
                }

                if (isRowEmpty(dataRow))
                    continue;

                String phone = getCellValue(dataRow.getCell(headerIndexMap.get("Customer Phone".toLowerCase())));

                if (phone == null || phone.isEmpty()) {
                    errorSb.append("Row " + rowNum + ": No phone number \n");
                    continue;
                }

                String name = getCellValue(dataRow.getCell(headerIndexMap.get("Customer Name".toLowerCase())));

                String address = Stream
                        .of(getCellValue(dataRow.getCell(3)), getCellValue(dataRow.getCell(4)),
                                getCellValue(dataRow.getCell(5)),
                                getCellValue(dataRow.getCell(headerIndexMap.get("Province".toLowerCase()))),
                                getCellValue(dataRow.getCell(headerIndexMap.get("PostCode".toLowerCase()))))
                        .filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining(", "));

              
                String productName = getCellValue(dataRow.getCell(headerIndexMap.get("Products Name".toLowerCase())));
                
                
                String comment = Stream
                        .of(productName,
                             getCellValue(dataRow.getCell(headerIndexMap.get("Price".toLowerCase()))),
                            getCellValue(dataRow.getCell(headerIndexMap.get("Payment Method".toLowerCase()))),
                            getCellValue(dataRow.getCell(headerIndexMap.get("Comment".toLowerCase()))))
                        .filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining(" - "));

                String agentNote = getCellValue(dataRow.getCell(headerIndexMap.get("Agent note".toLowerCase())));
              

                Integer productId = null;
                if (!productName.isEmpty()) {

                    getProductParams.setName(productName);
                    DBResponse<List<PDProduct>> dbResponse = clProductService.getProduct(SESSION_ID, getProductParams);
                    if (dbResponse != null && !dbResponse.getResult().isEmpty()) {
                        productId = dbResponse.getResult().get(0).getProdId();
                    } else {
                        errorSb.append("Row " + rowNum + ": Product not found \n");
                    }
                }

                Date now = new Date();
                DateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                String nowText = formatter.format(now);

                InsCLFreshV11 clFresh = new InsCLFreshV11();
                clFresh.setOrgId(orgId);
                clFresh.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
                clFresh.setLeadType("M"); // flag to generate lead_id automatically
                clFresh.setName(name);
                clFresh.setPhone(phone);

                if (!productName.isEmpty())
                    clFresh.setProdName(productName);
                if (productId != null)
                    clFresh.setProdId(productId);

                clFresh.setAddress(address);
                clFresh.setComment(comment);
                clFresh.setAssigned(0);
                clFresh.setCpId(cpId);
                clFresh.setCallinglistId(clId);
                clFresh.setCreatedate(nowText);
                clFresh.setModifydate(nowText);
                clFresh.setNextCallTime(nowText);
                clFresh.setAgentNote(agentNote);
                clFresh.setCrmActionType(EnumType.CRM_ACTION_TYPE.NEW_LEAD.getValue());

                clFreshList.add(clFresh);
            }

            for (InsCLFreshV11 clFresh : clFreshList) {
                DBResponse<?> dbResponse = logService.insCLFreshV11(SESSION_ID, clFresh);

                // insert log
                try {
                    int leadId = Integer.parseInt(dbResponse.getErrorMsg());
                    if (leadId > 0) {
                        count++;
                        CLFresh lead = new CLFresh();
						ModelMapper modelMapper = new ModelMapper();
						lead = modelMapper.map(clFresh, CLFresh.class);
						lead.setLeadId(leadId);
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						Date date  = sdf.parse(clFresh.getModifydate());
						clFresh.setModifydate(null);
						lead = modelMapper.map(clFresh, CLFresh.class); 
						lead.setModifydate(date);
						lead.setLeadId(leadId);
						lead.setCreatedate(new Date());
						lead.setLeadStatusName("new");
						lead.setNextCallTime(new Date());


                        dbLog.writeLeadStatusLog(userId, leadId, EnumType.LEAD_STATUS.NEW.getValue(),
                                "Create Lead By Excel");

//                        dbLog.writeLeadStatusLogV2(userId,lead ,leadId, EnumType.LEAD_STATUS.NEW.getValue(),
//                                "Create Lead By Excel");
                    }
                } catch (Exception ex) {
                    errorSb.append(clFresh.getPhone() + " " + dbResponse.getErrorMsg() + "\n");
                }

                // increase counter
                RedisHelper.incrementLead(stringRedisTemplate, Const.REDIS_PREFIX_STATIC + ":" + orgId);
            }
        } catch (EncryptedDocumentException e) {
            logger.error("EncryptedDocumentException: ", e);
            return TMSResponse.buildResponse(e.getMessage(), 0, e.getMessage(), 400);
        } catch (IOException e) {
            logger.error("IOException: ", e);
            return TMSResponse.buildResponse(e.getMessage(), 0, e.getMessage(), 400);
        }
        return TMSResponse.buildResponse(true, count, count + " rows import successfully.\n " + errorSb.toString(),
                200);
    }
}
