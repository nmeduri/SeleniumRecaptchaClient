package com.freelancer.catpcha.solver.ui.communicator;

import com.freelancer.catpcha.solver.dto.CaptchaRequest;

public interface Communicator {

    void addRequest(CaptchaRequest request);

    void disableEverything();

    void enableEverything();

    void solutionSubmitted();

}
