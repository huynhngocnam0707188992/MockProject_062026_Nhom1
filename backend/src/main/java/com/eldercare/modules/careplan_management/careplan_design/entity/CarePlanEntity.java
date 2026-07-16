package com.eldercare.modules.careplan_management.careplan_design.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.eldercare.common.enums.CarePlanStatusEnum;

import com.eldercare.modules.careplan_management.careplan_design.entity.resident_info.CarePlanResidentInfoEntity;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CarePlanEntity {

    private int id;
    private CarePlanStatusEnum status = CarePlanStatusEnum.DRAFT;
    private Boolean significantFlag = false;
    private CarePlanResidentInfoEntity resident = null;
    private List<CareGoalEntity> listCareGoal = new ArrayList<>();
    private OffsetDateTime lastReviewDateTime = null;
    private String lastReviewBy = null;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean isDeleted;

    // ------------------------CARE PLAN BUSINESS
    // LOGIC---------------------------------//

    /**
     * Nurse submit a care plan and wait for DON review, the status change to
     * PENDING_REVIEW
     */
    public void submitForReview() {
        if (status != CarePlanStatusEnum.DRAFT && status != CarePlanStatusEnum.NEEDS_UPDATE) {
            throw new IllegalStateException(
                    "Only draft or needs update care plan can be submitted."
            );
        }
        this.status = CarePlanStatusEnum.PENDING_REVIEW;
        this.updatedAt = OffsetDateTime.now();
    }

    /***
     * DON want to approve the care plan, the status change to ACTIVE
     */
    public void approve(String lastReviewBy) {
        if (this.status != CarePlanStatusEnum.PENDING_REVIEW) {
            throw new IllegalStateException("This care plan is in status " + this.status + " that cannot be approved");
        }
        if (lastReviewBy == null || lastReviewBy.isBlank()) {
            throw new IllegalArgumentException("Reviewer is required.");
        }
        this.status = CarePlanStatusEnum.ACTIVE;
        this.updatedAt = OffsetDateTime.now();
        this.lastReviewDateTime = OffsetDateTime.now();
        this.lastReviewBy = lastReviewBy;
    }

    /**
     * DON want to reject the care plan, the status come back into DRAFT
     */
    public void reject() {
        if (this.status == CarePlanStatusEnum.PENDING_REVIEW) {
            this.status = CarePlanStatusEnum.DRAFT;
            this.updatedAt = OffsetDateTime.now();
        } else {
            throw new IllegalStateException("This care plan is in status " + this.status + " that cannot be rejected");
        }
    }

    /**
     * When the care plan due to the review, status change to REVIEW_DUE
     */
    public void markReviewDue() {
        if (this.status != CarePlanStatusEnum.ACTIVE) {
            throw new IllegalStateException("This care plan is in status " + this.status + " that cannot be mark review due");
        }
        this.status = CarePlanStatusEnum.REVIEW_DUE;
        this.updatedAt = OffsetDateTime.now();
    }

    /**
     * When we have an incident, care plan need to be update, status cahnge to
     * NEEDS_UPDATE
     */
    public void markSignificant() {

        this.significantFlag = true;
        this.status = CarePlanStatusEnum.NEEDS_UPDATE;
        this.updatedAt = OffsetDateTime.now();
    }

    /**
     * DON will do reassessment
     *
     */
    public void confirmNoChanges(String lastReviewBy) {
        if (status != CarePlanStatusEnum.REVIEW_DUE &&
                status != CarePlanStatusEnum.NEEDS_UPDATE) {
            throw new IllegalStateException("This care plan is in status " + status + " that cannot confirm no changes.");
        }

        if (lastReviewBy == null || lastReviewBy.isBlank()) {
            throw new IllegalArgumentException("Reviewer is required.");
        }

        this.status = CarePlanStatusEnum.ACTIVE;
        this.significantFlag = false;
        this.updatedAt = OffsetDateTime.now();
        this.lastReviewDateTime = OffsetDateTime.now();
        this.lastReviewBy = lastReviewBy;
    }

    /**
     * Resident discharde
     */
    public void archive() {
        this.status = CarePlanStatusEnum.ARCHIVED;
        this.updatedAt = OffsetDateTime.now();
        this.isDeleted = true; // not ready yet
    }

    /**
     * We want to calculate the time that care plan would be in REWIVEW_DUE state
     */
    public OffsetDateTime getNextReviewDateTime() {
        OffsetDateTime nexReviewDateTime = null;
        if (this.lastReviewDateTime == null) {
            return null;
        }
        nexReviewDateTime = this.lastReviewDateTime.plusDays(90);
        return nexReviewDateTime;
    }

// ------------------------CARE GOAL BUSINESS
// LOGIC---------------------------------//

    /**
     * The Care Plan need to add care goal, have to change the status into PENDING_REVIEW, need DON review
     *
     */
    public void addCareGoal(CareGoalEntity careGoalEntity) {
        if (this.status == CarePlanStatusEnum.ARCHIVED) {
            throw new IllegalStateException("This care plan is in state " + this.status + " that cannot be add goal");
        }
        if (careGoalEntity == null) {
            throw new IllegalArgumentException("Cannot be null");
        }
        this.listCareGoal.add(careGoalEntity);
        this.status = CarePlanStatusEnum.PENDING_REVIEW;
    }

    /**
     * The Care Plan need to be remove one care goal, have to change the status into PENDING_REVIEW, need DON review
     *
     */
    public void removeCareGoal(int id) {
        if (this.status == CarePlanStatusEnum.ARCHIVED) {
            throw new IllegalStateException("This care plan is in state " + this.status + " that cannot be add goal");
        }
        for (int i = 0; i < this.listCareGoal.size(); i++) {
            if (this.listCareGoal.get(i).getId() == id) {
                this.listCareGoal.remove(i);
                return;
            }
        }

        this.status = CarePlanStatusEnum.PENDING_REVIEW;
    }
}
