package cz.cvut.fel.b221.earomo.seminar.helpdesk.dto;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeReview;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.EmployeeReviewGrade;

public record EmployeeReviewListDTO(Long id, TicketDetailDTO ticket, EmployeeReviewGrade grade,
                                    String textReview, UserDTO writer) {
    public static EmployeeReviewListDTO fromEntity(EmployeeReview entity) {
        return new EmployeeReviewListDTO(
                entity.getEmployeeReviewId(),
                TicketDetailDTO.fromEntity(entity.getTicket()),
                entity.getGrade(),
                entity.getTextReview(),
                UserDTO.fromEntity(entity.getCustomer())
        );
    }
}