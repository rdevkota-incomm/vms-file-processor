package com.incomm.vms.fileprocess.repository;

import com.incomm.vms.fileprocess.model.UploadDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public class UploadDetailRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(UploadDetailRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(UploadDetail uploadDetail) {
        String sql = " INSERT INTO vms_return_fileupload_dtls ( "
                + " VRF_INST_CODE, VRF_FILE_NAME, VRF_TOTAL_RECCOUNT, "
                + " VRF_SUCCESS_RECCOUNT, VRF_FAILURE_RECCOUNT, "
                + " VRF_FAILURE_DESC , VRF_FILE_STATUS, "
                + " VRF_INS_USER , VRF_INS_DATE, VRF_LUPD_USER, VRF_LUPD_DATE) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ? , SYSDATE) ";
        LOGGER.debug("Save sql being executed \n {}", sql);
        LOGGER.debug("Save sql Values:\n {}", uploadDetail.toString());
        return jdbcTemplate.update(sql,
                uploadDetail.getInstanceCode(), uploadDetail.getFileName(),
                uploadDetail.getTotalRecCount(), uploadDetail.getSuccessRecCount(),
                uploadDetail.getFailureRecCount(), uploadDetail.getFailureDesc(),
                uploadDetail.getFileStatus(), uploadDetail.getUser(), uploadDetail.getUser());
    }

    public int update(UploadDetail uploadDetail) {

        return 0;
    }

    public UploadDetail findByFileName(String filename) {
        return null;
    }
}
