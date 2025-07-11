package com.toeicify.toeic.mapper;

import com.toeicify.toeic.dto.response.exam.ExamResponse;
import com.toeicify.toeic.dto.response.exampart.ExamPartResponse;
import com.toeicify.toeic.entity.Exam;
import com.toeicify.toeic.entity.ExamPart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Created by hungpham on 7/10/2025
 */
@Mapper(componentModel = "spring")
public interface ExamMapper {
    @Mapping(source = "exam.examCategory.categoryId", target = "categoryId")
    @Mapping(source = "exam.examCategory.categoryName", target = "categoryName")
    @Mapping(source = "exam.createdBy.userId", target = "createdById")
    @Mapping(source = "exam.createdBy.username", target = "createdByName")
    @Mapping(source = "exam.status", target = "status")
    ExamResponse toExamResponse(Exam exam);

    @Mapping(source = "part.partId", target = "partId")
    ExamPartResponse toExamPartResponse(ExamPart part);

    List<ExamPartResponse> toExamPartResponseList(List<ExamPart> parts);
}
