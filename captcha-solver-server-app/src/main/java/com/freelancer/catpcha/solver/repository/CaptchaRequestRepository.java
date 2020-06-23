package com.freelancer.catpcha.solver.repository;

import com.freelancer.catpcha.solver.entity.CaptchaRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaptchaRequestRepository extends JpaRepository<CaptchaRequest, Long> {

    CaptchaRequest findFirstBySolutionTimeStampIsNullAndAssigned(boolean assigned);

    CaptchaRequest findOneByRequestId(Long requestId);

}
