package com.tms.api.rest;

import com.tms.api.dto.ProdDto;
import com.tms.api.dto.ProdRespDto;
import com.tms.api.dto.ProductComboDto;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.Const;
import com.tms.api.helper.DateHelper;
import com.tms.api.helper.LogHelper;
import com.tms.api.response.TMSResponse;
import com.tms.config.DBConfigMap;
import com.tms.dto.*;
import com.tms.entity.PDProduct;
import com.tms.api.entity.PdCategory;
import com.tms.entity.ProductInStock;
import com.tms.entity.log.*;
import com.tms.service.impl.CLProductService;
import com.tms.service.impl.DeliveryOrderService;
import com.tms.service.impl.LogService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tms.api.repository.PdCategoryRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController extends BaseController {
    Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    CLProductService clProductService;
    @Autowired
    LogService logService;

    @Autowired
    DBConfigMap dbConfigMap;

    @Autowired
    DeliveryOrderService doService;

    @Autowired
    PdCategoryRepository pdCategoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public TMSResponse<List<PDProduct>> getListV2(GetProductV2 params) throws TMSException {
        logger.info("============= GOTO getListV2 =============");
        params.setOrgId(getCurrentOriganationId());
        int prodType = 1;
        if (params.getProductType() != null)
            prodType = params.getProductType();

        params.setProductType(prodType);
        DBResponse<List<PDProduct>> dbResponse = clProductService.getProductV2(SESSION_ID, params);
        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
    }

    @GetMapping("/{prodId}")
    public TMSResponse getProduct(@PathVariable Integer prodId) throws TMSException {
        GetProductV2 params = new GetProductV2();
        params.setProdId(prodId);
        DBResponse<List<PDProduct>> dbResponse = clProductService.getProductV2(SESSION_ID, params);

        if (dbResponse.getResult().isEmpty())
            return TMSResponse.buildResponse((DBResponse) null);

        List<PDProduct> products = dbResponse.getResult();
        List<GetProductComboResp> comboResps = null;
        int productType = 1;
        if (products.get(0).getProductType() != null)
            productType = products.get(0).getProductType();
        if (productType > 1) {
            GetProductCombo getProductCombo = new GetProductCombo();
            getProductCombo.setComboId(prodId);
            DBResponse<List<GetProductComboResp>> comboDb = clProductService.getProductCombo(SESSION_ID, getProductCombo);
            if (!comboDb.getResult().isEmpty())
                comboResps = comboDb.getResult();
        }
        List<ProdRespDto> lstReturns = new ArrayList();
        for (PDProduct pdProduct : products) {
            ProdRespDto tmp = new ProdRespDto(pdProduct);

            tmp.setItems(comboResps);
            lstReturns.add(tmp);
        }

        return TMSResponse.buildResponse(lstReturns);
    }

    @GetMapping("/StockByProduct")
    public TMSResponse<List<ProductInStock>> getStockByProduct(GetProductInStockParams params) throws TMSException {
        params.setOrgId(getCurrentOriganationId());

        DBResponse<List<ProductInStock>> dbResponse = clProductService.getProductInStock(SESSION_ID, params);
        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
    }

    @PutMapping
    public TMSResponse update(@RequestBody UpdProduct product) throws TMSException {
        product.setOrgId(getCurrentOriganationId());
        DBResponse response = logService.updProduct(SESSION_ID, product);
        if (response.getErrorCode() != Const.INS_DB_SUCCESS) {
            throw new TMSException(response.getErrorMsg());
        }
        return TMSResponse.buildResponse(response.getResult());
    }


    @PostMapping
    public TMSResponse create(@RequestBody InsProduct product) throws TMSException {
        product.setOrgId(getCurOrgId());
        DBResponse response = logService.insProduct(SESSION_ID, product);
        if (response.getErrorCode() != Const.INS_DB_SUCCESS) {
            throw new TMSException(response.getErrorMsg());
        }
        return TMSResponse.buildResponse(response.getResult());
    }

    private TMSResponse createComboProduct(ProductComboDto productComboDto, boolean isUpdate, int orgId) throws TMSException {
        if (productComboDto == null || productComboDto.getComboInfo() == null)
            throw new TMSException("Cannot allow NULL");
        DBResponse response = null;

        logger.info(LogHelper.toJson(productComboDto));
        if (!isUpdate) {
            InsProductV2 product = productComboDto.getComboInfo();
            product.setOrgId(orgId);
            response = logService.insProductV2(SESSION_ID, product);
        } else {
            UpdProductV2 updProductV2 = modelMapper.map(productComboDto.getComboInfo(), UpdProductV2.class);
            logger.info(LogHelper.toJson(updProductV2));
            updProductV2.setOrgId(orgId);
            response = logService.updProductV2(SESSION_ID, updProductV2);
        }
        if (response != null && response.getErrorCode() != Const.INS_DB_SUCCESS) {
            throw new TMSException(response.getErrorMsg());
        }

        if (productComboDto.getProds() != null && productComboDto.getProds().size() > 0) {
            Integer comboId = null;
            if (!isUpdate)
                comboId = Integer.parseInt(response.getErrorMsg().trim());
            else {
                comboId = productComboDto.getComboInfo().getProdId();
                //delete all product in COMBO
                logService.delProductCombo(SESSION_ID, comboId);
            }
            for (int i = 0; i < productComboDto.getProds().size(); i++) {
                ProdDto prodDto = productComboDto.getProds().get(i);

                if (prodDto.getPrdId() != null && prodDto.getQty() != null && prodDto.getQty() > 0 && prodDto.getPrdId() > 0) {
                    GetProductV2 paramV2 = new GetProductV2();
                    paramV2.setProdId(prodDto.getPrdId());
                    paramV2.setOrgId(orgId);
                    DBResponse<List<PDProduct>> dbResponse = clProductService.getProductV2(SESSION_ID, paramV2);

                    if (dbResponse.getResult().isEmpty()) {
                        throw new TMSException("Could not found product " + prodDto.getPrdId());
                    }
                    PDProduct pdProduct = dbResponse.getResult().get(0);

                    InsProductCombo insProductCombo = new InsProductCombo();
                    insProductCombo.setComboId(comboId);
                    insProductCombo.setProdId(prodDto.getPrdId());
                    insProductCombo.setQuantity(prodDto.getQty());
                    insProductCombo.setItemNumber(i + 1);
                    String[] prices = pdProduct.getPrice().split("\\|");
                    if (prodDto.getIndex() > prices.length - 1) {
                        throw new TMSException(ErrorMessage.BAD_REQUEST);
                    }
                    Double productPrice = Double.parseDouble(prices[prodDto.getIndex()]);
                    insProductCombo.setPrice(productPrice);
//                    insProductCombo.setListPrice(pdProduct.getPrice());
                    logService.insProductCombo(SESSION_ID, insProductCombo);
                }
            }
        }
        return TMSResponse.buildResponse(response.getResult());
    }

    @PutMapping("/v2")
    public TMSResponse update(@RequestBody ProductComboDto productComboDto) throws TMSException {
        return createComboProduct(productComboDto, true, getCurOrgId());
    }

    @PostMapping("/v2")
    public TMSResponse createV2(@RequestBody ProductComboDto productComboDto) throws TMSException {
        return createComboProduct(productComboDto, false, getCurOrgId());
    }

    @PostMapping("/createAttribute")
    public TMSResponse createAttribute(@RequestBody InsProductAttribute attribute) {
        DBResponse dbResponse = logService.insProductAttribute(SESSION_ID, attribute);
        return TMSResponse.buildResponse(dbResponse.getResult());
    }

    @PutMapping("/updateAttribute")
    public TMSResponse updateAttribute(@RequestBody InsProductAttribute attribute) {
        DBResponse dbResponse = logService.updProductAttribute(SESSION_ID, attribute);
        return TMSResponse.buildResponse(dbResponse.getResult());
    }

    @PutMapping("/updateAttributeV2")
    public TMSResponse updateAttributeV2(@RequestBody UpdProductV2 attribute) {
        DBResponse dbResponse = logService.updProductV2(SESSION_ID, attribute);
        return TMSResponse.buildResponse(dbResponse.getResult());
    }

    @DeleteMapping("/{prodId}")
    public TMSResponse delete(@PathVariable Integer prodId) throws TMSException {
        // TODO delete Product
       /* GetProductParams params = new GetProductParams();
        params.setProdId(prodId);
        DBResponse dbResponse = clProductService.getProduct(SESSION_ID, params);
        if (dbResponse.getErrorCode() != 1) {
            throw new TMSException(dbResponse.getErrorMsg());
        }
        List<PDProduct> lst = dbResponse.getResult();

        if(lst.isEmpty()){
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }*/
        UpdProduct p = new UpdProduct();
        p.setStatus(2); //delete status
        p.setProdId(prodId);

        DBResponse response = logService.updProduct(SESSION_ID, p);
        if (response.getErrorCode() == 1) {
            throw new TMSException(response.getErrorMsg());
        }
        return TMSResponse.buildResponse(response.getResult());
    }

    @GetMapping("province/{provinceId}")
    public TMSResponse<List<GetDefaultDOResp>> getProductByProvinceId(@PathVariable Integer provinceId) throws TMSException {
        GetDefaultDO params = new GetDefaultDO();
        params.setProvinceId(provinceId);
        params.setOrgId(getCurrentOriganationId());
        DBResponse<List<GetDefaultDOResp>> dbResponse = doService.getDefaultDO(SESSION_ID, params);
        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
    }

    @GetMapping("/category")
    public TMSResponse<List<PdCategory>> getCategory() {
        List<PdCategory> lstCategory = pdCategoryRepository.getByOrgId(getCurOrgId());
        return TMSResponse.buildResponse(lstCategory, lstCategory.size());
//        public List<PdCategory> findByCriteria(Integer orgId) {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Item> criteriaQuery = criteriaBuilder.createQuery(Item.class);
//        Root<Item> itemRoot = criteriaQuery.from(Item.class);


    }


    @PostMapping("/category")
    public TMSResponse createCategory(@RequestBody PdCategory input) {
        logger.info(input.getStatus().toString());
        if(input.getId() != null){
            return TMSResponse.buildResponse(null, 0, "Id must be null", 400);
        }
        if(input.getStatus() == null){
            return TMSResponse.buildResponse(null, 0, "Status can not be null", 400);
        }
        if(input.getStatus() != 1 && input.getStatus() != 2){
            return TMSResponse.buildResponse(null, 0, "Status must be 1 or 2. 1 is active, 2 is inactive", 400);
        }
        if (input != null){
            input.setOrgId(getCurOrgId());
            input.setModifyby(_curUser.getUserId());
            input.setModifydate(DateHelper.nowToTimestamp());
            PdCategory pdCategory = pdCategoryRepository.save(input);
            if(pdCategory.getId() > 0) {
                return TMSResponse.buildResponse(null, 0, "Create category is successful", 200);
            }
        }
        return TMSResponse.buildResponse(null, 0, "Create category is failed, please check your input", 400);
    }

    @PutMapping("/category")
    public TMSResponse updateCategory(@RequestBody PdCategory input){
        if(input.getId() == null){
            return TMSResponse.buildResponse(null, 0, "Id can not be null", 400);
        }
        if(input != null && input.getStatus() != 1 && input.getStatus() != 2){
            return TMSResponse.buildResponse(null, 0, "Status must be 1 or 2. 1 is active, 2 is inactive", 400);
        }
        if (input != null){
            try{
                PdCategory update = pdCategoryRepository.getById(input.getId());
                if (input != null){
                    update.setStatus(input.getStatus());
                }
                if(input.getCode() != null){
                    update.setCode(input.getCode());
                }
                if(input.getName() != null){
                    update.setName(input.getName());
                }
                if(input.getDscr() != null){
                    update.setDscr(input.getDscr());
                }
                update.setModifyby(_curUser.getUserId());
                update.setModifydate(DateHelper.nowToTimestamp());
                pdCategoryRepository.save(update);
                return TMSResponse.buildResponse(null, 0, "Update category is successful", 200);
            }catch (Exception e){
                return TMSResponse.buildResponse(null, 0, "Category is not found", 400);
            }
        }
        return TMSResponse.buildResponse(null, 0, "Create category is failed, please check your input", 400);
    }

    @DeleteMapping("/category/{id}")
    public TMSResponse deleteCategory (@PathVariable Integer id) {
        PdCategory category = pdCategoryRepository.getById(id);
        if (category == null) {
            return TMSResponse.buildResponse(null, 0, "Category is not found", 400);
        }
        category.setStatus(2);
        category.setModifyby(_curUser.getUserId());
        category.setModifydate(DateHelper.nowToTimestamp());
        pdCategoryRepository.save(category);
        return TMSResponse.buildResponse(null, 0, "Delete category is successful", 200);
    }

}
