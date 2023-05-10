package com.kbstar.mapper;

import com.kbstar.dto.Sales;
import com.kbstar.frame.KBMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SalesMapper extends KBMapper<String, Sales> {
    public List<Sales> monthly();
    public List<Sales> gender();
}
