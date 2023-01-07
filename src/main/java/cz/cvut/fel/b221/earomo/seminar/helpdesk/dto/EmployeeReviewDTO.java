package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.EmployeeReviewGrade;

public record EmployeeReviewDTO(Long id, EmployeeReviewGrade grade,
                                    String textReview) {}
