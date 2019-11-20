package com.incomm.vms.fileprocess.repository;

import com.incomm.vms.fileprocess.model.FileProcessReasonMaster;
import com.incomm.vms.fileprocess.model.LineItemDetail;
import com.incomm.vms.fileprocess.model.OrderDetailAggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderLineItemRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(OrderLineItemRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int update(OrderDetailAggregate orderDetailAggregate) {
        String sql = " UPDATE vms_order_lineitem SET "
                + " vol_order_status =  " +
                " CASE  WHEN ? = 0  THEN 'Rejected'" +
                "  WHEN ? = ? THEN  'Printer_Acknowledged'" +
                "   ELSE  vol_order_status" +
                "  END, "
                + " vol_return_file_msg = ? "
                + " WHERE vol_order_id = ? "
                + " AND vol_line_item_id = ? "
                + " AND vol_partner_id = ? "
                + " AND ( vol_order_status NOT IN ( 'Completed', 'Shipped', 'Rejected', 'Shipping' ) "
                + " OR vol_order_status IS NULL )";

        return jdbcTemplate.update(sql,
                orderDetailAggregate.getRejectCount(),
                orderDetailAggregate.getTotalCount(),
                orderDetailAggregate.getPrinterAcknowledgedCount(),
                orderDetailAggregate.getRejectReason(),
                orderDetailAggregate.getOrderId(),
                orderDetailAggregate.getLineItemId(),
                orderDetailAggregate.getPartnerId());
    }
}
