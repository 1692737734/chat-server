package com.chat.dao;

import com.chat.vo.entity.Files;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FilesDao {
    void insert(Files files);
    void update(Files files);

    Files getFilesById(@Param("id") long id);

}
