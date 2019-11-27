package com.incomm.vms.fileprocess.repository;

import oracle.jdbc.OracleTypes;
import oracle.sql.ArrayDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DeleteCardRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(DeleteCardRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String delete(List<String> cardPanList) {
//        String outValue;
//        ArrayDescriptor  arrayDescriptor = ArrayDescriptor.createDescriptor("shuffle_array_typ");
        String[] panList = cardPanList.stream().toArray(String[]::new);
        SimpleJdbcCall jdbcCall =  new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("delete_cards")
                .withCatalogName("VMSB2BAPI")
                .declareParameters(
                        new SqlParameter("p_card_nos_in", OracleTypes.ARRAY, "shuffle_array_typ"),
                        new SqlOutParameter("p_resp_msg_out", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("p_card_nos_in", cardPanList );
        Map<String, Object> simpleJdbcCallResult = jdbcCall.execute(in);
        return simpleJdbcCallResult.get("p_resp_msg_out").toString();
    }

//    public String delete1(List<String> cardPanList) {
//
//        SqlParameter fNameParam = new SqlParameter(Types.ARRAY);
//        SqlOutParameter outParameter = new SqlOutParameter("p_resp_msg_out", Types.VARCHAR);
//
//        List<SqlParameter> paramList = new ArrayList();
//
//        paramList.add(fNameParam);
//        paramList.add(outParameter);
//
//        final String procedureCall = "{call vmsb2bapi.delete_cards(?, ?)}";
//        Map<String, Object> resultMap = jdbcTemplate.call(new CallableStatementCreator() {
//            @Override
//            public CallableStatement createCallableStatement(Connection connection) throws SQLException {
//                Array cardlistArray = connection.createArrayOf("VARCHAR", cardPanList.toArray());
//                CallableStatement callableStatement = connection.prepareCall(procedureCall);
//                callableStatement.setArray(1, cardlistArray);
//                callableStatement.registerOutParameter(3, Types.VARCHAR);
//                return callableStatement;
//
//            }
//        }, paramList);
//        System.out.println(resultMap.get("p_resp_msg_out"));
//        return resultMap.get("p_resp_msg_out").toString();
//    }
}
